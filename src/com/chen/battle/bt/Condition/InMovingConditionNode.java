package com.chen.battle.bt.Condition;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.bt.BTNode;

public class InMovingConditionNode extends BTConditionNode
{

	public InMovingConditionNode(int id, int[] params) 
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		return robot.bIsMoving;
	}

}
