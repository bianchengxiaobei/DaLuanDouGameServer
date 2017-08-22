package com.chen.battle.structs;



import com.chen.battle.ai.SSAI;
import com.chen.battle.message.res.ResGoAppearMessage;
import com.chen.battle.message.res.ResIdleStateMessage;
import com.chen.battle.message.res.ResRunningStateMessage;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.structs.ISSMoveObjectHolder;
import com.chen.battle.skill.structs.SSSkillConfig;
import com.chen.message.Message;
import com.chen.move.struct.ColSphere;
import com.chen.move.struct.ColVector;
import com.chen.parameter.structs.EParameterCate;
import com.chen.utils.MessageUtil;

public abstract class SSGameUnit extends SSMoveObject
{
	public long id;
	public BattleContext battle;
	public SSAI ai;
	public SGOActionStateInfo curActionInfo;
	public long enterBattleTime;
	public boolean bExpire;
	public ISSMoveObjectHolder moveHolder;
	public SSSkill normalAttackSkill;
	public int moveEffectId;//移动效果
	public boolean bIfActiveMove;//是否激活移动
	public SSGameUnit(long playerId,BattleContext battle)
	{
		this.id = playerId;
		this.battle = battle;
		this.curActionInfo = new SGOActionStateInfo();
	}
	public void BeginActionIdle(boolean asyn)
	{
		if (EGOActionState.Idle != curActionInfo.eOAS)
		{
			SetGOActionState(EGOActionState.Idle);
			if (asyn && battle != null)
			{
				battle.SyncState(this);
			}
		}
	}
	public void BeginActionMove(CVector3D dir,boolean asyn)
	{
		SetGOActionState(EGOActionState.Running);;
		curActionInfo.dir = dir;
		curActionInfo.distMove = 0;
		if (asyn && battle != null)
		{
			battle.SyncState(this);
		}
	}
	/**
	 * 结束控制状态，转成Idle，并通知客户端进入Idle
	 * @param asyn
	 */
	public void EndActionController(boolean asyn)
	{
		if (this.curActionInfo.eOAS == EGOActionState.Controlled)
		{
			BeginActionIdle(asyn);
			this.curActionInfo.time = System.currentTimeMillis();
		}
	}
	
	public void SetGOActionState(EGOActionState state)
	{
		this.curActionInfo.eOAS = state;
		this.curActionInfo.time = System.currentTimeMillis();
	}
	/**
	 * 设置移动效果，如果当前有移动先移除
	 * @param moveEffectId
	 * @param active
	 */
	public void SetMoveEffect(int moveEffectId,boolean active)
	{
		if (moveEffectId != 0)
		{
			battle.effectManager.RemoveEffect(moveEffectId);
			this.moveEffectId = moveEffectId;
			bIfActiveMove = active;
		}
	}
	/**
	 * 构建角色状态消息
	 * @return
	 */
	public Message ConstructObjStateMessage()
	{
		switch (this.curActionInfo.eOAS) 
		{
		case Idle:
		case Controlled:
			ResIdleStateMessage res = new ResIdleStateMessage();
			res.playerId = this.id;
			res.posX = this.curActionInfo.pos.x;
			res.posY = this.curActionInfo.pos.y;
			res.posZ = this.curActionInfo.pos.z;
			res.dirX = this.curActionInfo.dir.x;
			res.dirY = this.curActionInfo.dir.y;
			res.dirZ = this.curActionInfo.dir.z;
			return res;
		case Running:
			ResRunningStateMessage message = new ResRunningStateMessage();
			message.playerId = this.id;
			message.posX = this.curActionInfo.pos.x;
			message.posY = this.curActionInfo.pos.y;
			message.posZ = this.curActionInfo.pos.z;
			message.dirX = this.curActionInfo.dir.x;
			message.dirY = this.curActionInfo.dir.y;
			message.dirZ = this.curActionInfo.dir.z;
			message.moveSpeed = (int)GetSpeed();
			return message;
		}
		return null;
	}
	public void SendAppearMessage()
	{
		ResGoAppearMessage message = new ResGoAppearMessage();
		message.playerId = this.id;
		message.dirX = (int)this.GetCurDir().x;
		message.dirY = (int)this.GetCurDir().y;
		message.posX = (int)this.GetCurPos().x;
		message.posY = (int)this.GetCurPos().y;
		message.hp = this.GetCurHp();
		MessageUtil.tell_battlePlayer_message(this.battle, message);
	}
	@Override
	public boolean IfCanImpact()
	{
		return IsDead() == false;
	}
	/**
	 * 是否能成为被攻击对象(没有死亡)
	 * @return
	 */
	public boolean IfCanBeTarget()
	{
		return curActionInfo.eOAS != EGOActionState.Dead;
	}
	public boolean IsDead()
	{
		return this.curActionInfo.eOAS == EGOActionState.Dead || bExpire == true;
	}
	@Override
	public ColVector GetColVector()
	{
		ColVector vector = new ColVector(GetCurPos().x, GetCurPos().y,GetCurPos().z);
		return vector;
	}
	@Override
	public ColSphere GetColSphere()
	{
		ColSphere sphere = new ColSphere(GetColVector(), GetColRadius());
		return sphere;
	}
	@Override
	public int GetColRadius()
	{
		return this.GetColliderRadius();
	}
	public CVector3D GetCurPos()
	{
		return this.curActionInfo.pos;
	}
	public CVector3D GetCurDir()
	{
		return this.curActionInfo.dir;
	}
	public int GetCurHp()
	{
		return 100;
	}
	public int GetFPData(EParameterCate cate)
	{
		return 3;
	}
	public abstract int GetColliderRadius();
	/*
	 * 发送移动消息
	 * @see com.chen.battle.structs.SSMoveObject#OnStartMove(com.chen.move.struct.ColVector)
	 */
	@Override
	public void OnStartMove(ColVector dir)
	{
		CVector3D cDir = new CVector3D(0, 0, 0);
		cDir.x = dir.x;
		cDir.y= dir.y;
		cDir.z = dir.z;
		if (curActionInfo.eOAS.value < EGOActionState.PassiveState.value)
		{
			BeginActionMove(cDir, true);
		}
		//可能有被动技能触发
		//OnPassitiveSkillCalled(EPassiveSkillTriggerType_Move, this);
	}
	@Override
	public void OnChangeDir(ColVector dir)
	{
		CVector3D cDir = new CVector3D(0, 0, 0);
		cDir.x = dir.x;
		cDir.y = dir.y;
		cDir.z = dir.z;
		curActionInfo.dir = cDir;
	}
	@Override
	public void OnMoveBlock()
	{
		if (this.curActionInfo.eOAS.value < EGOActionState.PassiveState.value)
		{
			this.CheckBeginActionFree(true);
		}
		//技能停止移动
//		if(m_moveHolder != NULL){
//			m_moveHolder->OnStopMove();
		this.ai.OnMoveBlock();
	}
	public void OnTeleport()
	{
		this.ai.OnTeleport();
	}
	private void CheckBeginActionFree(boolean asyn)
	{
		if (this.curActionInfo.eOAS.value >= EGOActionState.PassiveState.value)
		{
			return;
		}
		BeginActionIdle(asyn);
	}
	public boolean IfInReleaseSkillRange(SSGameUnit target,SSSkillConfig config,float addDist)
	{
		if (config == null)
		{
			return false;
		}
		if (target != null)
		{
			if (config.bIsNormalAttack)
			{
				return false;
			}
			else
			{
				return GetCurPos().CanWatch(config.releaseDis + addDist, target.GetCurPos());
			}
		}
		return true;
	}
}
