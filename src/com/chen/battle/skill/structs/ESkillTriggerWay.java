package com.chen.battle.skill.structs;

public enum ESkillTriggerWay
{
	None(0),
	Active(1),
	Passive(2),
	WhileAttack(3),
	WhileBeAttack(4),
	WhileBeHurt(5),
	WhileDie(6),
	WhileUse(7),
	WhileImpact(8);
	public int value;
	private ESkillTriggerWay(int value)
	{
		this.value = value;
	}
}
