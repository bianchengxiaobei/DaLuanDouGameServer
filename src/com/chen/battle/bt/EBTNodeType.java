package com.chen.battle.bt;

public enum EBTNodeType 
{
	Selector(0),
	Sequence(1),
	Condition(2),
	Action(3);
	public int value;
	private EBTNodeType(int value)
	{
		this.value = value;
	}
}
