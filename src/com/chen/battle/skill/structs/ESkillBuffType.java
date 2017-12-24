package com.chen.battle.skill.structs;

public enum ESkillBuffType
{
	Other(0),
	Positive(1),
	Negative(2);
	ESkillBuffType(int value)
	{
		this.value = value;
	}
	public int value;
}
