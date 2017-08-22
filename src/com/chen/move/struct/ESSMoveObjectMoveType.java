package com.chen.move.struct;

public enum ESSMoveObjectMoveType
{
	None(0),
	Dir(1),
	Target(2),
	ForceMove(3);
	public int value;
	ESSMoveObjectMoveType(int value)
	{
		this.value = value;
	}
}
