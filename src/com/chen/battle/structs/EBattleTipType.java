package com.chen.battle.structs;

public enum EBattleTipType 
{
	eTips_None(0),
	eTips_ObjAppear(1),
	eTips_CountDown(2);//倒计时
	public int value;
	private EBattleTipType(int value)
	{
		this.value = value;
	}
}
