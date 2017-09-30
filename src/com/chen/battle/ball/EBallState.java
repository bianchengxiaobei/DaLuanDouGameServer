package com.chen.battle.ball;

public enum EBallState
{
	Free(0),
	OnDorping(1),
	InHero(2);
	public int value;
	private EBallState(int value) 
	{
		this.value = value;
	}
}
