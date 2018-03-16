package com.chen.battle.bt.Condition;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.bt.BTNode;
import com.chen.battle.structs.SSHero;

public class InStartTimeConditionNode extends BTConditionNode
{
	public InStartTimeConditionNode(int id, int[] params)
	{
		super(id, params);	
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		SSHero hero = robot.hero;
		if (hero != null)
		{
			if (robot.waitStartTime == 0)
			{
				robot.waitStartTime = (long)((Math.random() * 1000 + 1000)*6);
			}
			boolean value = hero.battle.battleHeartBeatTime - hero.battle.battleStateTime < robot.waitStartTime;
			return value;
		}
		return true;
	}

}
