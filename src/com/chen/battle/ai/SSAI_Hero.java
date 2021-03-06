package com.chen.battle.ai;

import com.chen.battle.ai.structs.EAttackState;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.message.res.ResUseSkillResultMessage;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.battle.skill.structs.EUseSkillErrorCode;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.utils.MessageUtil;

public class SSAI_Hero extends SSAI
{
	public SSHero hero;
	public boolean bIsMoveDir;
	public SSSkill nowSkill;
	public SSSkill wantUseSkill;
	public SSSkill nextSkill;
	public boolean bAutoAttack = false;
	public SSAI_Hero(SSGameUnit _theOwner) 
	{
		super(_theOwner);
		hero = (SSHero)theOwner;
	}
	public void AskMoveDir(CVector3D dir)
	{
		//如果是普通攻击
		if (bIsStandAttack)
		{
			System.err.println("还在前摇中");	
			return;
		}
		if(IfPassitiveState() == true)
		{
			return;
		}
		if (this.StopAllSkills(false) == false)
		{
			if (this.nowSkill == null || this.nowSkill.IfMasterMoveable() == false)
			{
				return;
			}
		}		
		if (theOwner.battle.AskMoveDir(theOwner, dir) == false)
		{
			theOwner.BeginActionIdle(true);
			return;
		}
		bIsMoveDir = true;
		bIsMoving = true;
	}
	public void AskStopMove()
	{
		if(IfPassitiveState() == true)
		{
			return;
		}
		theOwner.battle.AskStopMoveDir(theOwner);
		bIsMoveDir = false;
	}
	/**
	 * 请求使用技能
	 * @param skillId
	 */
	public void AskUseSkill(int skillId)
	{
		//如果是普通攻击
		if (bIsStandAttack)
		{
			this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.IsStandAttack.value);
			return;
		}
		//是否在不能操作状态
		if (IfPassitiveState() == true)
		{
			System.err.println("在不能操作的阶段");
			this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.Controller.value);
			return;
		}
		//是否在沉默状态
		//技能是否存在
		SSSkill skill = ((SSHero)theOwner).GetSkillById(skillId);
		if (skill == null)
		{
			System.err.println("不存在该技能");
			this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.NoSkill.value);
			return;
		}
		//是否符合技能使用条件
		if (!skill.IfSkillUsable())
		{
			System.err.println("在技能CD当中");
			this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.InCD.value);
			return;
		}
		//检查是否是正在运行的技能，不能重复s
		if (this.nowSkill != null && this.nowSkill.skillConfig.skillId == skillId)
		{
			System.err.println("重复技能："+skillId);
			this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.Repeat.value);
			return;
		}
		//首先判断是否在施放技能中。如果在施放技能中，则后面的技能都进行记录，等待施放
		if (IfUsingSkill() && (this.nowSkill.IfSkillBeforeRelease() || this.nowSkill.eSkillState == ESkillState.Using))
		{
			System.err.println("技能运行中："+skillId);
			this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.Running.value);
			nextSkill = skill;
			return;
		}
		else
		{
			//先检测技能是否够距离施放	
			int rts = skill.IfSkillUsableWithNowTarget();
			if (rts == 2)
			{
				System.err.println("技能释放距离不够："+skillId);
				this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.ToFar.value);
				//如果技能射程不够，则启动自动追踪释放技能功能
				bIsMoveDir = false;
				wantUseSkill = skill;
				moveTarPos = wantUseSkill.target.GetCurPos();
				lastCheckMoveTarTime = theOwner.battle.battleHeartBeatTime;
				//MoveToTar
				return;
			}
			else if(rts != 1)
			{
				System.err.println("其他原因导致技能不能释放："+skillId);
				this.NotifyUseSkillResult(skillId,EUseSkillErrorCode.Other.value);
				return;
			}
			else
			{
				//如果是非瞬发技能
				if (skill.IfImpactSkill() == false || 
						(skill.skillConfig.lastTime > 0 
								&& bIsMoving == false)&& 
								attackSkill.bIsRunning == false)
				{
					//停止攻击
					this.CancleAttack();
					//停止移动
					bIsMoveDir = false;
					theOwner.battle.AskStopMoveObjectAll(theOwner);
					//开始使用技能，停止其他技能
					this.StopAllSkills(false);
					this.nowSkill = skill;
					if (!skill.IfImpactSkill())
					{
						bIsStandAttack = true;
					}
				}
				skill.Start();
			}
		}
	}
	/**
	 * 是否在使用技能过程中
	 * @return
	 */
	public boolean IfUsingSkill()
	{
		return this.nowSkill != null && this.nowSkill.bIsRunning;
	}
	/**
	 * 停止所有技能
	 * @param bIsForceStop
	 * @return
	 */
	public boolean StopAllSkills(boolean bIsForceStop)
	{
		if (nowSkill != null)
		{
			if (this.nowSkill.TryCancle() == false)
			{
				return false;
			}
			this.nowSkill = null;
		}
		StopWantUseSkill(true);
		nextSkill = null;
		return true;
	}
	public void StopWantUseSkill(boolean isStopMove)
	{
		if (this.wantUseSkill != null)
		{
			this.wantUseSkill = null;
			if (isStopMove && bIsMoving)
			{
				theOwner.battle.AskStopMoveTarget(theOwner);
			}
		}
		nextSkill = null;
	}
	@Override
	public void OnTeleport()
	{
		if (this.nowSkill != null)
		{
			this.nowSkill.TryCancle();
		}
	}
	@Override
	public void HeartBeat(long now,long tick)
	{
		if (IfPassitiveState() == true)
		{
			StopAllSkills(true);
			CancleAttack();
			StopWantUseSkill(true);
			nextSkill = null;
			bIsMoveDir = false;
			bIsStandAttack = false;
			if (bIsMoving)
			{
				theOwner.battle.AskStopMoveDir(theOwner);
				theOwner.battle.AskStopMoveTarget(theOwner);
			}
			return ;
		}
		if (nowSkill != null)
		{
			UseSkillHeartBeat(now, tick);
		}
		else if (bAutoAttack)
		{
			if (attackTarget == null || attackTarget.id != hero.lockedTargetId)
			{
				if (this.CheckAttackTarget() == false)
				{
					CancleAttack();
					bAutoAttack = false;
					TryFree();
					return ;
				}
				eAttackState = EAttackState.Pursue;
				this.lastCheckMoveTarTime = 0;
			}
			else if (attackTarget.IsDead() || attackTarget.curActionInfo.eOAS == EGOActionState.Reliving)
			{
				TryFree();
				CancleAttack();
				return;
			}
			AttackHeartBeat(now, tick);
		}
//		else if(bIsMoving == false)
//		{
//			this.DoStandNormalAttack(now, tick);
//		}
	}
	public void UseSkillHeartBeat(long now,long tick)
	{
		int rst = this.nowSkill.HeartBeat(now, tick);
		if (rst != 1)
		{
			if (rst != 3)
			{
				this.nowSkill.TryCancle();
			}
			this.nowSkill = null;
			TryFree();
			if (nextSkill != null)
			{
				TryUseSkillWithAnyType(nextSkill);
			}
		}
	}
	public void TryUseSkillWithAnyType(SSSkill skill)
	{
		AskUseSkill(skill.skillConfig.skillId);
	}
	public void DoStandNormalAttack(long now,long tick)
	{
		long lockId = ((SSHero)theOwner).GetLockTargetId();
		if (lockId > 0)
		{
			SSGameUnit target = theOwner.battle.GetGameObjectById(lockId);
			//如果普攻正在攻击
			if (this.attackSkill.bIsRunning)
			{
				if (target == null || target.IsDead() || target.curActionInfo.eOAS == EGOActionState.Reliving)
				{
					bIsStandAttack = false;
					TryFree();
				}
				else
				{
					int rst = attackSkill.HeartBeat(now, tick);
					if (rst == 2)//NullPoint
					{
						bIsStandAttack = false;
						TryFree();
					}
				}
			}
			else
			{
				if (attackSkill.IfSkillUsableWithNowTarget() == 1)
				{
					if (attackSkill.IfSkillUsable() == true)
					{
						if (theOwner.IfInReleaseSkillRange(target, attackSkill.skillConfig, 0))
						{
							attackSkill.target = target;
							bIsStandAttack = true;
							attackSkill.Start();
						}
						else
						{
							if (bIsStandAttack)
							{
								bIsStandAttack = false;
								TryFree();
							}
						}
					}
				}
			}
		}
		else if(bIsStandAttack == false && now + 100 > attackSkill.cooldownTime)
		{
			bIsStandAttack = false;
			TryFree();
		}
	}
	public boolean AskStartAutoAttack()
	{
		if (IfPassitiveState() == true)
		{
			return true;
		}
		if (bAutoAttack)
		{
			return true;
		}
		if (this.bIsMoveDir == true)
		{
			return true;
		}
		if (this.CheckAttackTarget() == false)
		{
			return false;
		}
		if (this.StopAllSkills(true) == false)
		{
			return false;
		}
		bIsMoveDir = false;
		bIsStandAttack = false;
		bAutoAttack = true;
		eAttackState = EAttackState.Pursue;
		lastCheckMoveTarTime = 0;
		AttackHeartBeat(this.theOwner.battle.battleHeartBeatTime, 0);
		return true;
	}
	private void NotifyUseSkillResult(int skillId, int errorCode)
	{
		ResUseSkillResultMessage message = new ResUseSkillResultMessage();
		message.errorCode = errorCode;
		message.skillId = skillId;
		SSHero hero = (SSHero)theOwner;
		if (hero != null)
		{
			MessageUtil.tell_player_message(hero.player.player, message);
		}

	}
	public boolean CheckAttackTarget()
	{
		SSGameUnit target = hero.battle.GetGameObjectById(hero.lockedTargetId);
		if (hero.lockedTargetId <= 0 ||target  == null)
		{
			return false;
		}
		this.SetAttackTarget(target);
		if (hero.IfEnemy(target) == false)
		{
			this.SetAttackTarget(null);
			return false;
		}
		return true;
	}
}
