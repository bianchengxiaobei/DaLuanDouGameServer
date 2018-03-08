package com.chen.battle.structs;

import com.chen.battle.ball.Ball;
import com.chen.match.structs.EBattleModeType;

public class BattleBallContext extends BattleContext
{
	public BattleBallContext(EBattleModeType type, long battleId, int mapId)
	{
		super(type, battleId, mapId);
		this.battleAllTime = 60 * 1000 * 2;
		this.gameMode = new Ball(this);
	}
}
