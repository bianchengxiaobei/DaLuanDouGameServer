package com.chen.move.struct;

public enum EAskStopMoveType
{
	All(0),
	Dir(1),
	Target(2),
	ForceMove(3);
	public int value;
	EAskStopMoveType(int value) 
	{
		this.value = value;
	}
}
