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
	Reliving(8),
	Dead(9),
	End(10);
	public int value;
	private EGOActionState(int value) 
	{
		this.value = value;
	}
}
