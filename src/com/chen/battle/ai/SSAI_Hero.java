package com.chen.battle.ai;

import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;

public class SSAI_Hero extends SSAI
{
	public boolean bIsMoveDir;
	public SSSkill nowSkill;
	public SSSkill wantUseSkill;
	public SSSkill nextSkill;
	public SSAI_Hero(SSGameUnit _theOwner) 
	{
		super(_theOwner);
	}
	public void AskMoveDir(CVector3D dir)
	{
		if(IfPassitiveState() == true)
		{
			return;
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
		//是否在不能操作状态
		if (IfPassitiveState() == true)
		{
			return;
		}
		//是否在沉默状态
		//技能是否存在
		SSSkill skill = ((SSHero)theOwner).GetSkillById(skillId);
		if (skill == null)
		{
			System.err.println("不存在该技能");
			return;
		}
		//是否符合技能使用条件
		if (!skill.IfSkillUsable())
		{
			return;
		}
		//检查是否是正在运行的技能，不能重复s
		if (this.nowSkill != null && this.nowSkill.skillConfig.skillId == skillId)
		{
			return;
		}
		//首先判断是否在施放技能中。如果在施放技能中，则后面的技能都进行记录，等待施放
		if (IfUsingSkill() && (this.nowSkill.IfSkillBeforeRelease() || this.nowSkill.eSkillState == ESkillState.Using))
		{
			nextSkill = skill;
			return;
		}
		else
		{
			//先检测技能是否够距离施放	
			int rts = skill.IfSkillUsableWithNowTarget();
			if (rts == 2)
			{
				//如果技能射程不够，则启动自动追踪释放技能功能
				bIsMoveDir = false;
				wantUseSkill = skill;
				moveTarPos = wantUseSkill.target.GetCurPos();
				lastCheckMoveTarTime = System.currentTimeMillis();
				//MoveToTar
				return;
			}
			else if(rts != 1)
			{
				return;
			}
			else
			{
				//如果是非瞬发技能
				if (skill.IfImpactSkill() == false || 
						(skill.skillConfig.lastTime > 0 
								&& bIsMoving == false && 
								attackSkill.bIsRunning == false))
				{
					//停止攻击
					this.CancleAttack();
					//停止移动
					bIsMoveDir = false;
					theOwner.battle.AskStopMoveDir(theOwner);
					//开始使用技能，停止其他技能
					this.StopAllSkills(false);
					this.nowSkill = skill;
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
		if (nowSkill != null)
		{
			System.out.println("NOwSkill");
			UseSkillHeartBeat(now, tick);
		}
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
}
