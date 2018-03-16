package com.chen.battle.bt.Condition;

import java.util.Iterator;
import java.util.List;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.parameter.structs.EParameterCate;

public class CheckSkillInDistConditionNode extends BTConditionNode
{

	public CheckSkillInDistConditionNode(int id, int[] params)
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		SSHero hero = robot.hero;
		SSGameUnit target = hero.battle.GetGameObjectById(hero.lockedTargetId);
		if (target == null)
		{
			return false;
		}
		//普通攻击
		if (params[0] == 1)
		{
			float dist = hero.GetFPData(EParameterCate.AttackDist) * 0.001f;
			return hero.GetCurPos().CanWatch(dist, target.GetCurPos());
		}
		else if(params[0] == 2)
		{
			List<SSSkill> skillList = robot.skillList;
			if (skillList != null)
			{
				Iterator<SSSkill> iterator = skillList.iterator();
				while (iterator.hasNext())
				{
					SSSkill skill = iterator.next();
					float dist = skill.skillConfig.releaseDis;
					if (hero.GetCurPos().CanWatch(dist, target.GetCurPos()))
					{
						continue;
					}
					else
					{
						iterator.remove();
					}
				}		
				return skillList.isEmpty() == false;
			}
			return false;
		}
		return false;
	}
	
}
