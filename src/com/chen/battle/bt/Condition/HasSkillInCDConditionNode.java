package com.chen.battle.bt.Condition;

import java.util.Iterator;
import java.util.List;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.bt.BTNode;
import com.chen.battle.skill.SSSkill;

public class HasSkillInCDConditionNode extends BTConditionNode
{

	public HasSkillInCDConditionNode(int id, int[] params)
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		List<SSSkill> skillList = robot.skillList;
		if (skillList != null)
		{
			Iterator<SSSkill> iterator = skillList.iterator();
			while (iterator.hasNext())
			{
				SSSkill skill = iterator.next();
				if (skill.IfInCd() == false)
				{
					continue;
				}
				else
				{
					iterator.remove();
				}
			}
		}
		return skillList.isEmpty() == false;
	}
}
