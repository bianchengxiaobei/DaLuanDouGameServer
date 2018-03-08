package com.chen.battle.skill.passiveSkill;

public enum EPassiveSkillTargetType 
{
	EPassiveSkillTargetType_Self(0),
	EPassiveSkillTargetType_Trigger(1),
	EPassiveSkillTargetType_Both(2);
	public int value;
	private EPassiveSkillTargetType(int value)
	{
		this.value = value;
	}
}
