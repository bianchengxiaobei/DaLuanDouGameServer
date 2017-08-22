package com.chen.battle.skill.structs;

public enum ESkillTargetType 
{
	None(0),
	Self(1),
	TeamMember(2),
	TeamMember_Without_Self(3),
	Enemy(4);
	public int value;
	private ESkillTargetType(int value)
	{
		this.value = value;
	}
}
