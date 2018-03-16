package com.chen.battle.bt.Condition;

import java.util.List;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.structs.SSHero;

public class HasPrepareSkillConditionNode extends BTConditionNode
{

	public HasPrepareSkillConditionNode(int id, int[] params)
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		SSHero hero = robot.hero;
		List<SSSkill> skills = robot.skillList;
		skills.clear();
		SSSkill[] skillArray = hero.skillArray;
		for (SSSkill skill : skillArray)
		{
			if (skill != null && skill.skillConfig != null && skill.skillConfig.bIsNormalAttack == false)
			{
				skills.add(skill);
			}
		}
		return skills.isEmpty() == false;
	}
	
}
