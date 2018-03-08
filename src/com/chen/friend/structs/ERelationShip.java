package com.chen.friend.structs;

public enum ERelationShip 
{
	Friend((byte)0),
	Black((byte)1),
	NoneFriend((byte)2);
	public byte value;
	private ERelationShip(byte value)
	{
		this.value = value;
	}
}
