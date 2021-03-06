package com.chen.battle.structs;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chen.battle.ai.SSAI;
import com.chen.battle.message.res.ResDeadStateMessage;
import com.chen.battle.message.res.ResGoAppearMessage;
import com.chen.battle.message.res.ResIdleStateMessage;
import com.chen.battle.message.res.ResRunningStateMessage;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.config.SSSkillConfig;
import com.chen.battle.skill.message.res.ResLastingSkillStateMessage;
import com.chen.battle.skill.message.res.ResPrepareSkillStateMessage;
import com.chen.battle.skill.message.res.ResReleasingSkillStateMessage;
import com.chen.battle.skill.message.res.ResUsingSkillStateMessage;
import com.chen.battle.skill.passiveSkill.EPassiveSkillTriggerType;
import com.chen.battle.skill.structs.ESkillEffectType;
import com.chen.battle.skill.structs.ElementArray;
import com.chen.battle.skill.structs.ISSMoveObjectHolder;
import com.chen.message.Message;
import com.chen.move.struct.ColSphere;
import com.chen.move.struct.ColVector;
import com.chen.move.struct.SSMoveObject;
import com.chen.parameter.manager.SSParameterManager;
import com.chen.parameter.structs.EParameterCate;
import com.chen.player.structs.Player;
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
	public EGameObjectCamp camp;
	public ElementArray<Integer> bufferArray;
	public Map<EPassiveSkillTriggerType, List<Integer>> passiveSkillArray;
	public SSParameterManager fpManager;
	public SSGameUnit(long playerId,BattleContext battle,EGameObjectCamp _camp)
	{
		this.id = playerId;
		this.battle = battle;
		this.camp = _camp;
		this.bufferArray = new ElementArray<>(16);
		this.passiveSkillArray = new HashMap<EPassiveSkillTriggerType, List<Integer>>();
		this.curActionInfo = new SGOActionStateInfo();
		this.fpManager = new SSParameterManager();
		this.fpManager.SetOwner(this);
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
			this.curActionInfo.time = this.battle.battleHeartBeatTime;
		}
	}
	
	public void SetGOActionState(EGOActionState state)
	{
		this.curActionInfo.eOAS = state;
		this.curActionInfo.time = this.battle.battleHeartBeatTime;
	}
	/**
	 * 设置移动效果，如果当前有移动先移除
	 * @param _moveEffectId
	 * @param active
	 */
	public void SetMoveEffect(int _moveEffectId,boolean active)
	{
		if (this.moveEffectId != 0)
		{
			logger.error("当前还有移动特效，移除现在");
			battle.effectManager.RemoveEffect(this.moveEffectId);
			this.moveEffectId = _moveEffectId;
			bIfActiveMove = active;
		}
		else
		{
			this.moveEffectId = _moveEffectId;
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
		message.dirAngle = Tools.GetDirAngle(this.GetCurDir());
		message.posX = (int)(this.GetCurPos().x * 1000);
		message.posZ = (int)(this.GetCurPos().z * 1000);
		message.hp = this.GetCurHp();
		message.hp = this.GetCurHp();
		MessageUtil.tell_battlePlayer_message(this.battle, message);
	}
	public void SendToOhterAppearMessage(Player player)
	{
		ResGoAppearMessage message = new ResGoAppearMessage();
		message.playerId = this.id;
		message.dirAngle = Tools.GetDirAngle(this.GetCurDir());
		message.posX = (int)(this.GetCurPos().x * 1000);
		message.posZ = (int)(this.GetCurPos().z * 1000);
		message.hp = this.GetCurHp();
		MessageUtil.tell_player_message(player, message);
	}
	public void FullHp()
	{
		this.ChangeCurHp(this, HPMPChangeReason.Recover, GetFPData(EParameterCate.MaxHp) - GetFPData(EParameterCate.CurHp), 0, EParameterCate.None);
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
	public int GetFPData(EParameterCate cate)
	{
		return this.fpManager.GetValue(cate.value);
	}
	public void ChangeFPData(EParameterCate eEffectCate,int changeValue,int percent,boolean IfAdd)
	{
		if (changeValue == 0 && percent == 0)
		{
			return;
		}
		if (IfAdd)
		{
			if (changeValue != 0)
			{
				fpManager.AddBaseValue(eEffectCate.value, changeValue);
			}
			if (percent != 0)
			{
				fpManager.AddPercentValue(eEffectCate.value, percent);
			}
		}
		else
		{
			if (changeValue != 0)
			{
				fpManager.RemoveBaseValue(eEffectCate.value, changeValue);
			}
			if (percent != 0)
			{
				fpManager.RemovePercentValue(eEffectCate.value, percent);
			}
		}
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
	public CVector3D GetCurSkillDir()
	{
		return this.curActionInfo.skillDir;
	}
	public int GetCurHp()
	{
		return 100;
	}
	public CVector3D GetEmitPos()
	{
		return this.curActionInfo.pos;
	}
	public abstract void BeginActionDead(long skillerId,boolean asyn);
	public abstract float GetColliderRadius();
	public abstract boolean IfHero();
	public abstract void ChangeCurHp(SSGameUnit obj,HPMPChangeReason reason,int changeValue,int skillId,EParameterCate cate);
	public abstract int OnHeartBeat(long now,long tick);
	public abstract void CheckDeadStateToReborn();
	public void OnDizziness()
	{
		if (curActionInfo.eOAS.value < EGOActionState.PassiveState.value)
		{
			BeginActionIdle(true);
		}
	}
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
		if(this.moveHolder != null)
		{
			this.moveHolder.OnStopMove();
		}
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
				//普攻有
				return GetCurPos().CanWatch(
						GetFPData(EParameterCate.AttackDist) * 0.001f + target.GetColliderRadius() + addDist, target.GetCurPos());
			}
			else
			{
				//技能没有包围合一说
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
	public boolean IfEnemy(SSGameUnit obj)
	{
		if (obj == null)
		{
			return false;
		}
		return Tools.IfEnemy(this.camp.value, obj.camp.value);
	}
	/**
	 * 该角色是否能再添加Buff，默认16个
	 * @return
	 */
	public boolean CanAddBuff()
	{
		return this.bufferArray.curSize < this.bufferArray.GetMaxSize();
	}
	/**
	 * 缓存Buff到角色
	 * @param buffId
	 * @return
	 */
	public boolean AddBuffEffect(int buffId)
	{
		if (buffId == 0)
		{
			return false;
		}
		this.bufferArray.AddElement(buffId);
		return true;
	}
	/**
	 * 移除buff
	 * @param buffId
	 */
	public void RemoveBuffEffect(int buffId)
	{
		if (buffId == 0)
		{
			return;
		}
		this.bufferArray.RemoveElement(buffId);
	}
	public void AddPassiveSkill(EPassiveSkillTriggerType type, int id)
	{
		if (this.passiveSkillArray.containsKey(type))
		{
			this.passiveSkillArray.get(type).add(id);
		}
		else
		{
			List<Integer> ids = new ArrayList<>();
			ids.add(id);
			this.passiveSkillArray.put(type, ids);			
		}
	}
	public void RemovePassiveSkill(EPassiveSkillTriggerType type,int id)
	{
		this.passiveSkillArray.remove(type);
	}
	public List<Integer> GetPassiveSkill(EPassiveSkillTriggerType type)
	{
		if (this.passiveSkillArray.containsKey(type))
		{
			return this.passiveSkillArray.get(type);
		}
		return null;
	}
	public int ApplyHurt(SSGameUnit enemyObj,int realHurt,EParameterCate hurtType,boolean IfNormalAttack,boolean bIfCrit)
	{
		if (IsDead() || GetFPData(EParameterCate.CurHp) <= 0)
		{
			return 0;
		}
		float fDef = 0;
		float fDefPass = 0;
		float fDefPassPercent = 0;
		float fDmgReduce = 0;
		float fDmgReducePercent = 0;
		if (hurtType == EParameterCate.PhyHurt)
		{
			fDef = GetFPData(EParameterCate.PhyDefense);
			fDefPass = enemyObj.GetFPData(EParameterCate.PhyPass);
			fDefPassPercent = enemyObj.GetFPData(EParameterCate.PhyPassPercent);
			fDmgReduce = GetFPData(EParameterCate.PhyDmgReduce);
			fDmgReducePercent = GetFPData(EParameterCate.DmgReducePercent);
		}
		else if(hurtType == EParameterCate.MagicHurt)
		{
			fDef = GetFPData(EParameterCate.MagicDefense);
			fDefPass = GetFPData(EParameterCate.MagicPass);
			fDefPassPercent = GetFPData(EParameterCate.MagicPassPercent);
			fDmgReduce = GetFPData(EParameterCate.MagicDmgReduce);
			fDmgReducePercent = GetFPData(EParameterCate.DmgReducePercent);
		}
		
		fDef = (fDef - fDefPass) * (1-fDefPassPercent*0.01f);

		fDef = 100 / (100 + fDef);
		
		int finalHurtValue = (int)(realHurt * fDef - fDmgReduce);
		
		if (finalHurtValue <= 0)
		{
			finalHurtValue = 0;
		}
		else
		{
			finalHurtValue *= 1 - fDmgReducePercent * 0.01f;
		}
		if (finalHurtValue <= 0)
		{
			return 0;
		}
		HPMPChangeReason reason = HPMPChangeReason.SkillHurt;
		if (IfNormalAttack)
		{
			if (bIfCrit)
			{
				reason = HPMPChangeReason.CritHurt;//暴击
			}
			else
			{
				reason = HPMPChangeReason.NormalHurt;
			}
		}
		else
		{
			reason = HPMPChangeReason.SkillHurt;
		}
		this.ChangeCurHp(enemyObj, reason, -finalHurtValue, 0, EParameterCate.None);
		//添加被动技能
		return finalHurtValue;
	}
}
