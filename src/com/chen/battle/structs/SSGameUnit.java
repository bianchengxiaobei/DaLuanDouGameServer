package com.chen.battle.structs;



import com.chen.battle.ai.SSAI;
import com.chen.battle.message.res.ResGoAppearMessage;
import com.chen.battle.message.res.ResIdleStateMessage;
import com.chen.battle.message.res.ResRunningStateMessage;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.config.SSSkillConfig;
import com.chen.battle.skill.message.res.ResLastingSkillStateMessage;
import com.chen.battle.skill.message.res.ResPrepareSkillStateMessage;
import com.chen.battle.skill.message.res.ResReleasingSkillStateMessage;
import com.chen.battle.skill.message.res.ResUsingSkillStateMessage;
import com.chen.battle.skill.structs.ISSMoveObjectHolder;
import com.chen.message.Message;
import com.chen.move.struct.ColSphere;
import com.chen.move.struct.ColVector;
import com.chen.parameter.structs.EParameterCate;
import com.chen.utils.MessageUtil;
import com.chen.utils.Tools;

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
	public void BeginActionControled()
	{
		SetGOActionState(EGOActionState.Controlled);
		if (battle != null)
		{
			battle.SyncState(this);
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
	
	public void BeginActionPrepareSkill(SSSkill skill,CVector3D dir,boolean asyn)
	{
		if (skill == null)
		{
			System.err.println("skill == null");
			return;
		}
		if (curActionInfo.eOAS.value != EGOActionState.PreparingSkill.value)
		{
			SetGOActionState(EGOActionState.PreparingSkill);
			this.curActionInfo.skillId = skill.skillConfig.skillId;
			this.curActionInfo.skillTargetId = skill.target == null ? 0 : skill.target.id;
			this.curActionInfo.dir = dir;
			if (asyn)
			{
				battle.SyncState(this);
			}
		}
	}
	public void BeginActionReleaseSkill(SSSkill skill,CVector3D dir,boolean asyn)
	{
		if (skill == null)
		{
			System.err.println("skill == null");
			return;
		}
		if (curActionInfo.eOAS.value != EGOActionState.ReleasingSkill.value)
		{
			SetGOActionState(EGOActionState.ReleasingSkill);
			this.curActionInfo.skillId = skill.skillConfig.skillId;
			this.curActionInfo.skillTargetId = skill.target == null ? 0 : skill.target.id;
			this.curActionInfo.dir = dir;
			if (asyn)
			{
				battle.SyncState(this);
			}
		}
	}
	public void BeginActionUsingSkill(SSSkill skill,CVector3D dir,boolean asyn)
	{
		if (skill == null)
		{
			System.err.println("skill == null");
			return;
		}
		if (curActionInfo.eOAS.value != EGOActionState.UsingSkill.value)
		{
			SetGOActionState(EGOActionState.UsingSkill);
			this.curActionInfo.skillId = skill.skillConfig.skillId;
			this.curActionInfo.skillTargetId = skill.target == null ? 0 : skill.target.id;
			this.curActionInfo.dir = dir;
			if (asyn)
			{
				battle.SyncState(this);
			}
		}
	}	
	public void BeginActionLastingSkill(SSSkill skill,CVector3D dir,boolean asyn)
	{
		if (skill == null)
		{
			System.err.println("skill == null");
			return;
		}
		if (curActionInfo.eOAS.value != EGOActionState.LastingSkill.value)
		{
			SetGOActionState(EGOActionState.LastingSkill);
			this.curActionInfo.skillId = skill.skillConfig.skillId;
			this.curActionInfo.skillTargetId = skill.target == null ? 0 : skill.target.id;
			this.curActionInfo.dir = dir;
			if (asyn)
			{
				battle.SyncState(this);
			}
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
			res.posX = (int)(this.curActionInfo.pos.x * 1000);
			res.posZ = (int)(this.curActionInfo.pos.z * 1000);
			res.angle = Tools.GetDirAngle(this.curActionInfo.dir);
			return res;
		case Running:
			ResRunningStateMessage message = new ResRunningStateMessage();
			message.playerId = this.id;
			message.posX = (int)(this.curActionInfo.pos.x * 1000);
			message.posZ = (int)(this.curActionInfo.pos.z * 1000);			
			message.angle = Tools.GetDirAngle(this.curActionInfo.dir);
			message.moveSpeed = (int)(GetSpeed() * 1000);
			return message;
		case PreparingSkill:
			ResPrepareSkillStateMessage resPrepareSkillStateMessage = new ResPrepareSkillStateMessage();
			resPrepareSkillStateMessage.playerId = this.id;
			resPrepareSkillStateMessage.PosX = (int)(this.curActionInfo.pos.x * 1000);
			resPrepareSkillStateMessage.PosZ = (int)(this.curActionInfo.pos.z * 1000);
			resPrepareSkillStateMessage.dirAngle = Tools.GetDirAngle(this.curActionInfo.dir);
			resPrepareSkillStateMessage.targetId = this.curActionInfo.skillTargetId;
			resPrepareSkillStateMessage.skillId = this.curActionInfo.skillId;
			resPrepareSkillStateMessage.speed = (int)(this.GetSpeed() * 1000);
			return resPrepareSkillStateMessage;
		case ReleasingSkill:
			ResReleasingSkillStateMessage releasingSkillStateMessage = new ResReleasingSkillStateMessage();
			releasingSkillStateMessage.playerId = this.id;
			releasingSkillStateMessage.PosX = (int)(this.curActionInfo.pos.x * 1000);
			releasingSkillStateMessage.PosZ = (int)(this.curActionInfo.pos.z * 1000);
			releasingSkillStateMessage.dirAngle = Tools.GetDirAngle(this.curActionInfo.dir);
			releasingSkillStateMessage.targetId = this.curActionInfo.skillTargetId;
			releasingSkillStateMessage.skillId = this.curActionInfo.skillId;
			return releasingSkillStateMessage;
		case UsingSkill:
			ResUsingSkillStateMessage usingSkillStateMessage = new ResUsingSkillStateMessage();
			usingSkillStateMessage.playerId = this.id;
			usingSkillStateMessage.PosX = (int)(this.curActionInfo.pos.x * 1000);
			usingSkillStateMessage.PosZ = (int)(this.curActionInfo.pos.z * 1000);
			usingSkillStateMessage.dirAngle = Tools.GetDirAngle(this.curActionInfo.dir);
			usingSkillStateMessage.targetId = this.curActionInfo.skillTargetId;
			usingSkillStateMessage.skillId = this.curActionInfo.skillId;
			return usingSkillStateMessage;
		case LastingSkill:
			ResLastingSkillStateMessage lastingSkillStateMessage = new ResLastingSkillStateMessage();
			lastingSkillStateMessage.playerId = this.id;
			lastingSkillStateMessage.PosX = (int)(this.curActionInfo.pos.x * 1000);
			lastingSkillStateMessage.PosZ = (int)(this.curActionInfo.pos.z * 1000);
			System.out.println(this.curActionInfo.dir == null);
			lastingSkillStateMessage.dirAngle = Tools.GetDirAngle(this.curActionInfo.dir);
			lastingSkillStateMessage.targetId = this.curActionInfo.skillTargetId;
			lastingSkillStateMessage.skillId = this.curActionInfo.skillId;
			return lastingSkillStateMessage;
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
	public float GetColRadius()
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
	public CVector3D GetEmitPos()
	{
		return this.curActionInfo.pos;
	}
	public int GetFPData(EParameterCate cate)
	{
		return 3;
	}
	public abstract float GetColliderRadius();
	public abstract int OnHeartBeat(long now,long tick);
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
	public void RemoveMoveEffect(int effectId)
	{
		if (this.moveEffectId == effectId && battle != null)
		{
			battle.effectManager.RemoveEffect(effectId);
			this.moveEffectId = 0;
		}
	}
}
