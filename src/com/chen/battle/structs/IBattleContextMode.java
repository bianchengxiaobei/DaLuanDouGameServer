package com.chen.battle.structs;

import java.util.Map;

import com.chen.player.structs.Player;

public interface IBattleContextMode 
{
	void OnHeartBeat(long now,long tick);
	void Start();
	void SyncState();
	void SyncScoreState();
	void SyncReconnectState(Player player);
	boolean GetBWaitStart();
	void SetWaitTime(long va);
	long GetWaitTime();
	int CheckBattleFinish();
	public Map<Long, Integer> CaculateRank();
}
