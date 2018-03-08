package com.chen.battle.skill.structs;

public enum ESkillEmitToTargetType 
{
	Self(0),
	Custom(1);
	public int value;
	private ESkillEmitToTargetType(int value) 
	{
		this.value = value;
	}
}
