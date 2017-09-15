package com.chen.battle.structs;

public enum EGameObjectCamp 
{
	eGOCamp_AllEnemy(-1),
	eGOCamp_AllPeace(0),
	eGOCamp_1(1),
	eGOCamp_2(2),
	eGOCamp_3(3),
	eGOCamp_4(4),
	eGOCamp_5(5),
	eGOCamp_6(6);
	public int value;
	private EGameObjectCamp(int value) 
	{
		this.value = value;
	}
}
