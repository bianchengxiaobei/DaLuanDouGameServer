package com.chen.battle.structs;

public enum EBuyType 
{
	Gold((byte)0),
	Diamond((byte)1);
	public byte value;
	private EBuyType(byte value) 
	{
		this.value = value;
	}
}
