package com.chen.battle.skill.structs;

public enum ESkillRangeShapeType
{
	None(0),
	Circle(1),
	Rectangle(2),
	Sector(3);
	public int value;
	private ESkillRangeShapeType(int valuev)
	{
		this.value = valuev;
	}
}
