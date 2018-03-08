package com.chen.map;

public enum EBlockType 
{
	None((byte)0),
	Block((byte)1),
	Water((byte)2);
	public byte value;
	private EBlockType(byte value) 
	{
		this.value = value;
	}
}
