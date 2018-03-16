package com.chen.battle.bt;

public abstract class BTConditionNode extends BTNode
{
	protected int[] params;
	public BTConditionNode(int id,int[] params)
	{
		super(id,EBTNodeType.Condition);
		this.params = params;
	}	
}	
