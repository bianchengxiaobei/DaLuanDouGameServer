package com.chen.match.structs;

public enum EBattleModeType 
{
	Game_Mode_INVALID((byte)0),
	Game_Mode_Ball((byte)1),
	Game_Mode_Flag((byte)2),
	Game_Mode_Guide((byte)3),
	MATCH_MODE_TOTAL((byte)4);
	private byte value;
	EBattleModeType(byte value)
	{
		this.value = value;
	}
	public byte getValue() {
		return value;
	}
}
