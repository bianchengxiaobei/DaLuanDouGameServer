package com.chen.battle.ai;

import com.chen.battle.ai.structs.EAttackState;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.parameter.structs.EParameterCate;

public class SSAI
{
	public SSGameUnit theOwner;
	public SSGameUnit attackTarget;//自动攻击的目标
	public boolean bIsMoving;
	public long lastTryMoveTime;//上次尝试移动失败的时间
	public CVector3D moveTarPos = new CVector3D();//当前前进的目标地址
	public long lastCheckMoveTarTime;//上一次检查目标坐标的时间
	public SSSkill attackSkill;
	public EAttackState eAttackState;
	public boolean bIsStandAttack = false;//站立攻击
	public SSAI(SSGameUnit _theOwner)
	{
		this.theOwner = _theOwner;
	}
	public boolean IfPassitiveState()
	{
		if (theOwner == null)
		{
			return true;
		}
		if (theOwner.curActionInfo.eOAS.value > EGOActionState.PassiveState.value)
		{
			return true;
		}
		//如果是昏迷状态
		if (theOwner.GetFPData(EParameterCate.Dizziness) > 0)
		{
			return true;
		}
		return false;
	}
	public void OnMoveBlock()
	{
		bIsMoving = false;
	}
	public void CancleAttack()
	{
		//this.moveTarPos.zero();
		if (eAttackState == EAttackState.Pursue)
		{
			//停止移动到目的地
		}
		if (this.attackSkill != null)
		{
			this.attackSkill.TryCancle();
		}
	}
	public void StopAttack()
	{
		this.CancleAttack();
		this.SetAttackTarget(null);
		this.moveTarPos.zero();
		lastCheckMoveTarTime = 0;
	}
	public void TryFree()
	{
		if (theOwner.curActionInfo.eOAS.value < EGOActionState.PassiveState.value)
		{
			theOwner.BeginActionIdle(true);
		}	
	}
	public void HeartBeat(long now,long tick)
	{
		
	}
	public void OnTeleport()
	{
		
	}
	public void SetAttackTarget(SSGameUnit target)
	{
		this.attackTarget = target;
		if (this.attackSkill != null)
		{
			this.attackSkill.target = this.attackTarget;
		}
	}
	public boolean AttackHeartBeat(long now,long tick)
	{
		if (attackSkill == null)
		{
			return false;
		}
		if (IfPassitiveState())
		{
			if (bIsMoving)
			{
				theOwner.battle.AskStopMoveDir(theOwner);
			}
			attackSkill.End();
			return false;
		}
		if (this.attackTarget == null || this.attackTarget.IsDead() || this.attackTarget.curActionInfo.eOAS == EGOActionState.Reliving)
		{
			this.SetAttackTarget(null);
			if (bIsMoving)
			{
				theOwner.battle.AskStopMoveDir(theOwner);
			}
			this.eAttackState = EAttackState.Pursue;
			attackSkill.End();
			return false;
		}
		if (eAttackState == EAttackState.Pursue)
		{
			//检查是否到了施法距离,如果不在就开始移动
			if (theOwner.IfInReleaseSkillRange(this.attackTarget, this.attackSkill.skillConfig, 0))
			{
				eAttackState = EAttackState.UseSkill;
				theOwner.battle.AskStopMoveDir(theOwner);
				theOwner.battle.AskStopMoveTarget(theOwner);
			}
			else
			{
				long lastCheckTickSpan = now - lastCheckMoveTarTime;
				//每隔1000ms检测是否朝玩家也移动
				if (lastCheckTickSpan >= 1000)
				{
					if (!moveTarPos.equals(this.attackTarget.GetCurPos()))
					{
						this.moveTarPos = this.attackTarget.GetCurPos();
						this.MoveToTargetPos(this.moveTarPos, false, 0);
					}
					this.lastCheckMoveTarTime = now;
				}
				//如果停下来了，重新启动移动
				if (bIsMoving == false)
				{
					this.MoveToTargetPos(this.moveTarPos, false, now);
				}
			}
		}
		if (eAttackState == EAttackState.UseSkill)
		{
			if(bIsMoving)
			{
				theOwner.battle.AskStopMoveDir(theOwner);
				theOwner.battle.AskStopMoveTarget(theOwner);
			}
			if (this.attackSkill != null)
			{
				if (this.attackSkill.bIsRunning == false)
				{
					if(theOwner.IfInReleaseSkillRange(this.attackTarget, this.attackSkill.skillConfig, 0))
					{
						if (attackSkill.target != this.attackTarget)
						{
							attackSkill.target = this.attackTarget;						
						}
						this.attackSkill.Start();
					}
					else
					{
						eAttackState = EAttackState.Pursue;
						return true;
					}
				}
				else
				{
					int rst = this.attackSkill.HeartBeat(now, tick);
					if (rst != 1)
					{
						if (rst != ESkillState.End.value && theOwner.curActionInfo.eOAS != EGOActionState.Idle)
						{
							theOwner.BeginActionIdle(true);
						}
						this.attackSkill.End();
						return false;
					}
				}
			}
		}
		return true;
	}
	public void MoveToTargetPos(CVector3D pos,boolean ifMoveToBlackPoint,long now)
	{
		if (this.lastTryMoveTime != 0 && now - lastTryMoveTime < 300)
		{
			return;
		}
		if (theOwner.battle.AskMoveToTargetPos(theOwner,pos, ifMoveToBlackPoint,true))
		{
			bIsMoving = true;
			lastTryMoveTime = 0;
		}
		else
		{
			bIsMoving = false;
			theOwner.BeginActionIdle(true);
			this.lastTryMoveTime = theOwner.battle.battleHeartBeatTime;
		}
	}
	
}
