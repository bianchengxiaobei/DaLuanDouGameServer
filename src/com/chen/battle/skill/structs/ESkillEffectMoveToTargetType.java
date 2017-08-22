package com.chen.battle.skill.structs;

public enum ESkillEffectMoveToTargetType 
{
	Hit_Angle(0),
	Self(1),
	Target(2),
	Target_Angle(3),
	TheOwner_Angle(4),
	SkillTarget(5);
	public int value;
	private ESkillEffectMoveToTargetType(int value) 
	{
		this.value = value;
	}
}
