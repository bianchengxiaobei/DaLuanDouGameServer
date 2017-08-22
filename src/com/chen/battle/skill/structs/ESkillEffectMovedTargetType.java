package com.chen.battle.skill.structs;

public enum ESkillEffectMovedTargetType 
{
	Target(0),
	Self(1);
	public int value;
	private ESkillEffectMovedTargetType(int value) 
	{
		this.value = value;
	}
}
