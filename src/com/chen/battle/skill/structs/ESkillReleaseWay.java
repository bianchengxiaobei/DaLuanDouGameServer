package com.chen.battle.skill.structs;

public enum ESkillReleaseWay
{
	No_Target(0),
	Need_Target_No_Turn(1),
	Need_Target(2),
	Auto(3);
	public int value;
	private ESkillReleaseWay(int value) 
	{
		this.value = value;
	}
}
