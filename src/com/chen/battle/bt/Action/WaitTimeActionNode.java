package com.chen.battle.bt.Action;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTActionNode;

public class WaitTimeActionNode extends BTActionNode
{
	public WaitTimeActionNode(int id, int[] params) 
	{
		super(id, params);
	}

	@Override
	public boolean Action(SSAI_Robot robot)
	{		
		return true;
	}
	
}
