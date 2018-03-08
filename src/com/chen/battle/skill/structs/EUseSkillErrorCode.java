package com.chen.battle.skill.structs;

public enum EUseSkillErrorCode 
{
	IsStandAttack(0),
	Controller(1),
	NoSkill(2),
	InCD(3),
	Repeat(4),
	Running(5),
	ToFar(6),
	Other(7);
	public int value;
	private EUseSkillErrorCode(int code)
	{
		this.value = code;
	}
}
