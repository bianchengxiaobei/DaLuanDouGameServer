package com.chen.battle.bt;


import com.chen.battle.ai.SSAI_Robot;

public abstract class BTActionNode extends BTNode
{
	protected int[] params;
	public BTActionNode(int id, int[] params)
	{
		super(id, EBTNodeType.Action);
		this.params = params;
	}
	@Override
	public boolean Tick(SSAI_Robot robot) 
	{		
		return true;
	}
	public abstract boolean Action(SSAI_Robot robot);
	public boolean IfNeedTime(SSAI_Robot robot)
	{
		return true;
	}
}
