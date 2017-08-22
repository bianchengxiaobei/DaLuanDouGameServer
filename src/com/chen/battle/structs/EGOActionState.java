package com.chen.battle.structs;

public enum EGOActionState 
{
	Idle(0),
	Running(1),
	PreparingSkill(2),
	ReleasingSkill(3),
	UsingSkill(4),
	LastingSkill(5),
	PassiveState(6),
	Controlled(7),
	Dead(8),
	End(9);
	public int value;
	private EGOActionState(int value) 
	{
		this.value = value;
	}
}
