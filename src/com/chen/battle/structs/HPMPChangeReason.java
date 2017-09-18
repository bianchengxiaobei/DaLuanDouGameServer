package com.chen.battle.structs;

public enum HPMPChangeReason 
{
	NormalHurt(0),
	SkillHurt(1),
	Recover(2),
	SkillConsume(3),
	BuffEffectReason(4),
	CritHurt(5);
	public int value;
	private HPMPChangeReason(int value) 
	{
		this.value = value;
	}
}
