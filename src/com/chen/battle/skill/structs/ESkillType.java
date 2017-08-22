package com.chen.battle.skill.structs;

public enum ESkillType
{
	Cure(0),//治疗
	Hurt(1),
	Control(2),
	Move(3);
	public int value;
	private ESkillType(int value) 
	{
		this.value = value;
	}
}
