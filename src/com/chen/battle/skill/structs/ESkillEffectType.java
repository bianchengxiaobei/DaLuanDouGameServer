package com.chen.battle.skill.structs;

public enum ESkillEffectType 
{
	None(0),
	Caculate(1),
	Emit(2),
	Range(3),
	Leading(4),
	Summon(5),
	Move(6),
	Switch(7),
	Purification(8),
	Link(9),
	Buffer(10);
	public int value;
	private ESkillEffectType(int value) 
	{
		this.value = value;
	}
}
