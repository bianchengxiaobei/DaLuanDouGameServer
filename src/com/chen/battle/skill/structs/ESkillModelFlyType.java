package com.chen.battle.skill.structs;

public enum ESkillModelFlyType 
{
	Follow(0),
	Direct(1),
	AutoFind(2);
	public int value;
	ESkillModelFlyType(int value) 
	{
		this.value = value;
	}
}
