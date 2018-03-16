package com.chen.battle.bt.Condition;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.bt.BTNode;
import com.chen.battle.structs.EGOActionState;

public class InControllerConditionNode extends BTConditionNode
{

	public InControllerConditionNode(int id, int[] params)
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		return robot.hero.curActionInfo.eOAS.value >= EGOActionState.PassiveState.value;
	}

}
