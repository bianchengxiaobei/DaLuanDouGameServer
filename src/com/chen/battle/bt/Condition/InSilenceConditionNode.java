package com.chen.battle.bt.Condition;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTConditionNode;
import com.chen.battle.bt.BTNode;
import com.chen.parameter.structs.EParameterCate;

public class InSilenceConditionNode extends BTConditionNode
{

	public InSilenceConditionNode(int id, int[] params) 
	{
		super(id, params);
	}

	@Override
	public boolean Tick(SSAI_Robot robot)
	{
		return robot.hero.GetFPData(EParameterCate.Silence) <= 0;
	}

}
