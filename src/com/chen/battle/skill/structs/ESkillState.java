package com.chen.battle.skill.structs;

public enum ESkillState 
{
	Free(0),
	Preparing(1),
	Releasing(2),
	Using(3),
	Lasting(4),
	End(5);
	public int value;
	private ESkillState(int value)
	{
		this.value = value;
	}
}
