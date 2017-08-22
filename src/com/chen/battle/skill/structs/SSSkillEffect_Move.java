package com.chen.battle.skill.structs;

import com.chen.battle.skill.SSSkillEffect;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;

public class SSSkillEffect_Move extends SSSkillEffect implements ISSMoveObjectHolder
{
	public SkillModelMoveConfig moveConfig;
	public SSGameUnit theOwner;
	public boolean bIfEnd;
	public boolean bIfStartForceMove;
	public CVector3D moveDir;
	public CVector3D lastMovePos;
	public CVector3D moveTargetPos;
	public long beginTime;
	public long lastMoveTime;
	public long endTime;
	@Override
	public void End()
	{
			
	}

	@Override
	public void OnStopMove() 
	{
				
	}

	@Override
	public boolean Begin() 
	{
		this.moveConfig = (SkillModelMoveConfig)this.config;
		theOwner = this.moveConfig.eMovedTargetType == ESkillEffectMovedTargetType.Self ? theOwner : target;
		if (theOwner == null || theOwner.IsDead())
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
			if (theOwner == null || theOwner.IfCanBeTarget() == false)
			{
				return false;
			}
			if (moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.Self)
			{
				moveTargetPos = theOwner.GetCurPos();
			}
			else if(moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.Target)
			{
				moveTargetPos = target.GetCurPos();
			}
			else if (moveConfig.eMoveToTargetType == ESkillEffectMoveToTargetType.SkillTarget)
			{
				moveTargetPos = skill.targetPos;
			}
			CVector3D nowPos = theOwner.GetCurPos();
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
				ResetPos(theOwner, moveTargetPos, moveDir, false, false, false);
				bIfEnd = true;
			}
			else
			{
				
			}
			break;
		}
		if (bIfEnd)
		{
			
		}
		return true;
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
		theOwner.OnTeleport();
		return result;
	}
	@Override
	public boolean Update(long now, long tick)
	{
		if (bIfEnd)
		{
			theOwner.EndActionController(true);
			return false;
		}
		if (theOwner == null || theOwner.curActionInfo.eOAS != EGOActionState.Controlled)
		{
			bIfEnd = true;
			return false;
		}
		switch (this.moveConfig.eMoveType) 
		{
		case Absolute:
			float canMoveDist = this.moveConfig.speed * (float)(tick * 0.001);
			CVector3D moveVec = CVector3D.Sub(this.moveTargetPos , theOwner.GetCurPos());
			float dist = moveVec.Length();
			if (dist <= canMoveDist * 0.5 || moveVec.x * moveDir.x < 0)
			{
				theOwner.curActionInfo.pos = moveTargetPos;
				bIfEnd = true;
				return false;
			}
			break;

		
		}
		return true;
	}
	public void StopMove()
	{
		if (bIfStartForceMove)
		{
			bIfStartForceMove = false;
			theOwner.moveHolder = null;
			theOwner.battle.AskStopMoveObjectForceMove(theOwner);
			if (bIfEnd)
			{
				//通知客户端停止强制移动
				//NotifyStopMoveToGC(m_pMoveTarget, m_pMoveTarget->GetCurPos());
			}
			theOwner.EndActionController(true);
			if (bIfEnd)
			{
				theOwner.battle.effectManager.AddEffectsFromConfig(this.config.skillModelList, theOwner, target, theOwner.GetCurPos(), theOwner.GetCurDir(), skill, System.currentTimeMillis(), null);
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
		theOwner.SetMoveEffect(id, true);
		beginTime = System.currentTimeMillis();
		endTime = beginTime + (moveConfig.distance / moveConfig.speed)*1000;
		lastMoveTime = beginTime;
	}
}
