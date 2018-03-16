package com.chen.battle.bt.Action;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTActionNode;

public class NormalAttackActionNode extends BTActionNode
{

	public NormalAttackActionNode(int id, int[] params) 
	{
		super(id, params);
	}

	@Override
	public boolean Action(SSAI_Robot robot)
	{
		robot.lastTimeAction = robot.hero.battle.battleHeartBeatTime;
		return robot.AskStartAutoAttack();
	}

}
