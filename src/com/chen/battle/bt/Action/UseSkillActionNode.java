package com.chen.battle.bt.Action;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTActionNode;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.structs.ESkillReleaseWay;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;

public class UseSkillActionNode extends BTActionNode
{

	public UseSkillActionNode(int id, int[] params) 
	{
		super(id, params);
	}

	@Override
	public boolean Action(SSAI_Robot robot)
	{
		System.err.println("ffeff");
		SSHero hero = robot.hero;
		robot.lastTimeAction = hero.battle.battleHeartBeatTime;
		SSSkill skill = robot.GetPrepareSkill();
		if (skill == null)
		{
			return false;
		}
		boolean ifUseSkill = false;
		long oldLockedId = hero.lockedTargetId;
		switch (params[0])
		{
		case 1:
			hero.lockedTargetId = hero.id;
			ifUseSkill = true;
			break;
		case 2:
			SSGameUnit target = hero.battle.GetGameObjectById(hero.lockedTargetId);
			if (target == null || hero.IfEnemy(target) == false)
			{
				return false;
			}
			ifUseSkill = true;
			if (skill.skillConfig.eReleaseWay == ESkillReleaseWay.No_Target_Pos)
			{
				robot.hero.curActionInfo.skillParams = target.GetCurPos().clone();
			}
			else if(skill.skillConfig.eReleaseWay == ESkillReleaseWay.No_Target_Dir)
			{
				robot.hero.curActionInfo.skillParams = CVector3D.Sub(target.GetCurPos(), hero.GetCurPos()).Normalize();;
			}
			break;
		default:
			break;
		}
		if (ifUseSkill)
		{
			robot.AskUseSkill(skill.skillConfig.skillId);
			if (oldLockedId > 0 && oldLockedId != hero.lockedTargetId)
			{
				hero.lockedTargetId = oldLockedId;
			}
			return true;
		}
		return false;
	}

}
