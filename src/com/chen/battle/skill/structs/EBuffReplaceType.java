package com.chen.battle.skill.structs;

public enum EBuffReplaceType 
{
	None(0),
	Forbit(1),
	Reset(2),
	SingleCaculate(3);
	public int value;
	private EBuffReplaceType(int value)
	{
		this.value = value;
	}
}
