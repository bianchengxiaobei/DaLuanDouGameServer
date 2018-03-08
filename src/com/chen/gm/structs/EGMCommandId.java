package com.chen.gm.structs;

public enum EGMCommandId 
{
	Exp(0),
	Gold(1),
	Rank(2),
	SendMail(3);
	public int value;
	private EGMCommandId(int value)
	{
		this.value = value;
	}
}
