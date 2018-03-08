package com.chen.battle.skill.structs;

public enum ESkillAOEType 
{
	None(0),
	SelfCenter(1),
	TargetCenter(2),
	FixDist(3),
	CustomPos(4);
	public int value;
	private ESkillAOEType(int value) 
	{
		this.value = value;
	}
	
}
