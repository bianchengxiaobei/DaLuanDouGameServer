package com.chen.battle.bt.Condition;

import java.util.HashSet;
import java.util.Set;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.parameter.structs.EParameterCate;

public class HasFriendHeroBeHurtedConditionNode extends BTConditionNode
{

	public HasFriendHeroBeHurtedConditionNode(int id, int[] params)
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		SSHero hero = robot.hero;
		Set<SSGameUnit> targets = new HashSet<>();
		float radius = params[0] * 0.001f;
		hero.battle.FindAroundGo(hero.GetCurPos(), radius, targets);
		for (SSGameUnit target : targets)
		{
			if (target == hero || target.IsDead() || target.IfEnemy(hero))
			{
				continue;
			}
			if (target.GetFPData(EParameterCate.CurHp) < 100)
			{
				return true;
			}
		}
		return false;
	}

}
