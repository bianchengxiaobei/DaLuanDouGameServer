package com.chen.battle.structs;

public enum EGuideStepType
{
	eType_StartGameMode(7001),
	eType_SpawHero(7002);
	public int value;
	private EGuideStepType(int value)
	{
		this.value = value;
	}
}
