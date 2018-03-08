package com.chen.battle.skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.chen.battle.skill.config.SkillModelMoveConfig;
import com.chen.battle.skill.message.res.ResSkillModelStartForceMoveMessage;
import com.chen.battle.skill.message.res.ResSkillModelStopForceMoveMessage;
import com.chen.battle.skill.structs.ESkillEffectMoveToTargetType;
import com.chen.battle.skill.structs.ESkillEffectMoveType;
import com.chen.battle.skill.structs.ESkillEffectMovedTargetType;
import com.chen.battle.skill.structs.ISSMoveObjectHolder;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.utils.MessageUtil;
import com.chen.utils.Tools;

public class SSSkillEffect_Move extends SSSkillEffect implements ISSMoveObjectHolder
{
	public class HitPlayer
	{
		public boolean bIsHitted;
		public SSGameUnit target;
		public HitPlayer(SSGameUnit target)
		{
			this.target = target;
		}
	}
	public SkillModelMoveConfig moveConfig;
	public SSGameUnit theMoveOwner;
	public boolean bIfEnd;
	public boolean bIfStartForceMove;
	public CVector3D moveDir = new CVector3D();
	public CVector3D lastMovePos = new CVector3D();
	public CVector3D moveTargetPos = new CVector3D();
	public long beginTime;
	public long lastMoveTime;
	public long endTime;
	public Map<SSGameUnit, Boolean> impactPlayers = new HashMap<>();
	@Override
	public void End()
	{
		if (theMoveOwner != null)
		{
			theMoveOwner.RemoveMoveEffect(id);
			StopMove();
			if (bIfEnd)
			{
				this.NotifyStopMove(theMoveOwner, theMoveOwner.GetCurPos());
			}
		}
	}

	@Override
	public void OnStopMove() 
	{
		this.End();
	}

	@Override
	public boolean Begin() 
	{
		this.moveConfig = (SkillModelMoveConfig)this.config;
		theMoveOwner = this.moveConfig.eMovedTargetType == ESkillEffectMovedTargetType.Self ? theOwner : target;
		if (theMoveOwner == null || theMoveOwner.IsDead())
		{
			return false;
		}
		lastMovePos.zero();
		moveDir.zero();
		moveTargetPos.zero();
		this.beginTime = 0;
		lastMoveTime = 0;
		endTime = 0;
		bIfEnd = false;
		switch (this.moveConfig.eMoveType) 
		{
		case Absolute:
			if (target == null)
			{
				return false;
			}
			if (theMoveOwner == null || theMoveOwner.IfCanBeTarget() == false)
			{
				return false;
			}
			if (moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.Self)
			{
				moveTargetPos = theMoveOwner.GetCurPos();
			}
			else if(moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.Target)
			{
				moveTargetPos = target.GetCurPos();
			}
			else if (moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.SkillTarget)
			{
				moveTargetPos = skill.targetPos;
			}
			CVector3D nowPos = theMoveOwner.GetCurPos();
			CVector3D nowDist = CVector3D.Sub(moveTargetPos, nowPos) ;
			if (this.moveConfig.distance > 0 && nowDist.SqrtLength() > this.moveConfig.distance * this.moveConfig.distance)
			{
				//超过最大位移技能，返回
				return false;
			}
			this.moveDir = nowDist;
			this.moveDir.normalized();
			//如果是瞬移，直接强制移动位置
			if (moveConfig.speed <= 0)
			{
				ResetPos(theMoveOwner, moveTargetPos, moveDir, false, false, false);
				bIfEnd = true;
			}
			else
			{
				//否则，进入开始移动状态
				StartMove(nowPos);
			}
			break;
		case Opposite:
			if (theMoveOwner == null || theMoveOwner.IfCanBeTarget() == false)
			{
				return false;
			}
			CVector3D cNowPos = theMoveOwner.GetCurPos();
//			if (cNowPos.IsZero())
//			{
//				return false;
//			}
			if (moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.Hit_Angle)
			{
				moveDir = dir;
			}
			else if(moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.Target_Angle)
			{
				moveDir = theMoveOwner.GetCurDir();
			}
			else if(moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.TheOwner_Angle)
			{
				moveDir = theOwner.GetCurDir();
			}
			else if(moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.SkillTarget)
			{
				moveDir = theOwner.curActionInfo.skillParams;
			}
			moveDir.RotateAngle(moveConfig.angle);			
			if (moveConfig.speed <= 0)
			{
				//瞬移
				CVector3D targetPos = CVector3D.Add(cNowPos, CVector3D.Mul(moveDir, (float)(moveConfig.distance * 0.001)));
			}
			else
			{
				StartMove(cNowPos);
			}
			break;
		}
		if (bIfEnd)
		{
			battle.effectManager.AddEffectsFromConfig(this.config.skillModelList,
					theMoveOwner, target, theMoveOwner.GetCurPos(), theMoveOwner.GetCurDir(), skill, theMoveOwner.battle.battleHeartBeatTime, null);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean ResetPos(SSGameUnit _theOwner,CVector3D pos, CVector3D dir,boolean bIfEnd,boolean bIfCheckCollider,boolean bFindForward)
	{
		if (_theOwner == null)
		{
			return false;
		}
		if (bIfEnd)
		{
			battle.ResetPos(_theOwner, pos, false);
			return true;
		}
		boolean result = true;
		//如果需要检查碰撞，则根据碰撞系统，寻找一个符合条件的空白点
		if (bIfCheckCollider)
		{
			
		}
		else
		{
			_theOwner.curActionInfo.pos = pos;
		}
		theMoveOwner.OnTeleport();
		return result;
	}
	@Override
	public boolean Update(long now, long tick)
	{
		if (bIfEnd)
		{
			theMoveOwner.EndActionController(true);
			return false;
		}
		if (theMoveOwner == null || theMoveOwner.curActionInfo.eOAS != EGOActionState.Controlled)
		{
			bIfEnd = true;
			return false;
		}
		switch (this.moveConfig.eMoveType) 
		{
		case Absolute:
			float canMoveDist = (float) (this.moveConfig.speed *(0.001) * tick * 0.001);
			CVector3D moveVec = CVector3D.Sub(this.moveTargetPos , theMoveOwner.GetCurPos());
			float dist = moveVec.Length();
			if (dist <= canMoveDist * 0.5 || moveVec.x * moveDir.x < 0)
			{
				theMoveOwner.curActionInfo.pos = moveTargetPos;
				bIfEnd = true;
				return false;
			}
			break;	
		case Opposite:
			if (this.moveConfig.bIsImpact)
			{
				Iterator<Map.Entry<SSGameUnit, Boolean>> iterable = impactPlayers.entrySet().iterator();
				while (iterable.hasNext())
				{
					Entry<SSGameUnit, Boolean> entry = iterable.next();
					if (entry.getValue() == false)
					{
						SSGameUnit unit = entry.getKey();
						if (unit.IsDead() == false && unit.bExpire == false)
						{
							System.out.println("开始碰到敌人");
							battle.effectManager.AddEffectsFromConfig(moveConfig.impactEvents, theMoveOwner, unit, unit.GetCurPos(), moveDir, skill, theMoveOwner.battle.battleHeartBeatTime, null);
							//触发技能命中的被动技能
						}
						entry.setValue(true);
					}
				}
//				for (HitPlayer unit : impactPlayers)
//				{
//					if (unit.bIsHitted == false)
//					{
//						if (unit.target.IsDead() == false && unit.target.bExpire == false)
//						{
//							System.out.println("开始碰到敌人");
//							battle.effectManager.AddEffectsFromConfig(moveConfig.impactEvents, theMoveOwner, unit, unit.GetCurPos(), moveDir, skill, System.currentTimeMillis(), null);
//							//触发技能命中的被动技能
//						}
//						unit.bIsHitted = true;
//					}
//				}
			    return this.OnHeartBeatCheckEntityImpact(theMoveOwner, now, tick);
			}
			else
			{
				if (now > endTime)
				{
					bIfEnd = true;
					StopMove();
					return false;
				}
			}
			break;
		}
		return true;
	}
	public boolean OnHeartBeatCheckEntityImpact(SSGameUnit theOwner,long now ,long tick)
	{
		if (now - endTime > 0)
		{
			bIfEnd = true;
			StopMove();
			return false;
		}
		CVector3D selfPos = theOwner.GetCurPos();
		CVector3D dir = theOwner.GetCurDir();
		float canMoveDist = (float) (this.moveConfig.speed *(0.001) * tick * 0.001);
		CheckEntityImpact(theOwner, impactPlayers, moveConfig.bIsPenetrate);
		return true;
	}
	private void CheckEntityImpact(SSGameUnit theOwner,Map<SSGameUnit, Boolean> players,boolean bPen)
	{
		Set<SSGameUnit> objs = new HashSet<>();
		battle.FindAroundGo(theOwner.GetCurPos(), theOwner.GetColliderRadius(), objs);
		for (SSGameUnit obj : objs)
		{
			if (theOwner == obj)
			{
				continue;
			}
			if (obj.IsDead() || obj.bExpire)
			{
				continue;
			}
			//检查阵营和
			if (!players.containsKey(obj))
			{
				players.put(obj, false);
			}
			//如果是穿透的
			if (bPen == false)
			{
				break;
			}			
		}
	}
	public void StopMove()
	{
		if (bIfStartForceMove)
		{
			bIfStartForceMove = false;
			theMoveOwner.moveHolder = null;
			theMoveOwner.battle.AskStopMoveObjectForceMove(theMoveOwner);
			if (bIfEnd)
			{
				//通知客户端停止强制移动
				this.NotifyStopMove(theMoveOwner, theMoveOwner.GetCurPos());
			}
			theMoveOwner.EndActionController(true);
			if (bIfEnd)
			{
				theMoveOwner.battle.effectManager.AddEffectsFromConfig(this.moveConfig.skillModelList, theMoveOwner, target, theMoveOwner.GetCurPos(), theMoveOwner.GetCurDir(), skill, battle.battleHeartBeatTime, null);
			}
			bIfEnd = false;
		}
	}
	/**
	 * 开始移动
	 * @param startPos
	 */
	public void StartMove(CVector3D startPos)
	{
		bIfStartForceMove = true;
		lastMovePos = startPos;
		//避免强制移动重叠
		theMoveOwner.SetMoveEffect(id, true);
		beginTime = theMoveOwner.battle.battleHeartBeatTime;
		float time = (float)moveConfig.distance / (float)moveConfig.speed;
		endTime = beginTime + (long)(time*1000);
		lastMoveTime = beginTime;
		theMoveOwner.BeginActionControled();
		theMoveOwner.moveHolder = this;
		theMoveOwner.battle.AskStartMoveForced(theMoveOwner, moveDir, (float)(this.moveConfig.speed * 0.001),this.moveConfig.eMoveType == ESkillEffectMoveType.Opposite,endTime);
		//通知客户端开始技能强制移动
		this.NotifyStartMove(theMoveOwner, startPos, moveDir, this.moveConfig.speed);
	}
	private void NotifyStartMove(SSGameUnit obj, CVector3D pos, CVector3D dir, int speed)
	{
		if (obj == null)
		{
			return ;
		}
		ResSkillModelStartForceMoveMessage message = new ResSkillModelStartForceMoveMessage();
		message.playerId = obj.id;
		message.skillId = moveConfig.skillModelId;
		message.posX = (int)(pos.x * 1000);
		message.posY = (int)(pos.y * 1000);
		message.posZ = (int)(pos.z * 1000);
		message.dirAngle = Tools.GetDirAngle(dir);
		message.speed = speed;
		MessageUtil.tell_battlePlayer_message(battle, message);
	}
	private void NotifyStopMove(SSGameUnit obj, CVector3D pos)
	{
		//System.out.println("NOtify StopMove");
		if (obj == null)
		{
			return ;
		}
		ResSkillModelStopForceMoveMessage message = new ResSkillModelStopForceMoveMessage();
		message.playerId = obj.id;
		message.posX = (int)(pos.x * 1000);
		message.posY = (int)(pos.y * 1000);
		message.PosZ = (int)(pos.z * 1000);
		message.effectId = moveConfig.skillModelId;
		MessageUtil.tell_battlePlayer_message(battle, message);
	}
}
