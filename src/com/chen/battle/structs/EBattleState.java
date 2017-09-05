package com.chen.battle.structs;

public enum EBattleState
{
	eBattleState_Free(0),		//自由中//
	eBattleState_Wait(1),		//等待中//
	eBattleState_Async(2),		//异步中//
	eBattleState_Play(3);		//战斗中//
	
	public int value;
	private EBattleState(int value) 
	{
		this.value = value;
	}
}