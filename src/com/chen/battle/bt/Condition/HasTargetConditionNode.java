package com.chen.battle.bt.Condition;

import java.util.HashSet;
import java.util.Set;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.bt.BTNode;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;

public class HasTargetConditionNode extends BTConditionNode
{

	public HasTargetConditionNode(int id, int[] params)
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot) 
	{
		SSHero hero = robot.hero;
		float radius = params[0] * 0.001f;
		Set<SSGameUnit> targets = new HashSet<>();
		hero.battle.FindAroundGo(hero.GetCurPos(), radius, targets);
		for (SSGameUnit go : targets)
		{
			if (go.IsDead())
			{
				continue;
			}
			if (go.IfHero() && go.IfEnemy(hero))
			{
				hero.lockedTargetId = go.id;
				return true;
			}
		}
		return false;
	}

}
