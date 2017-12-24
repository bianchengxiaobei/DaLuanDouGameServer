package com.chen.battle.structs;

public interface IBattleContextMode 
{
	void OnHeartBeat(long now,long tick);
	void Start();
	void SyncState();
	void SyncScoreState();
}
