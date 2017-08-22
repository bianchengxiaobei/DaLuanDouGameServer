package com.chen.battle.skill.structs;

public enum ESkillEffectMoveType 
{
	Teleport(0),
	Absolute(1),
	Opposite(2);
	public int value;
	private ESkillEffectMoveType(int value) 
	{
		this.value = value;
	}
}
