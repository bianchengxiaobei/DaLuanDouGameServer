package com.chen.battle.structs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.ball.Ball;
import com.chen.match.structs.EBattleModeType;
import com.chen.parameter.structs.EParameterCate;

public class GuideBattleContext extends BattleContext
{
	private Logger logger = LogManager.getLogger(GuideBattleContext.class);
	public GuideBattleContext(EBattleModeType type, long battleId, int mapId)
	{
		super(type, battleId, mapId);
		this.battleAllTime = 5 * 1000 * 60;
		this.setBattleState(EBattleServerState.eSSBS_Loading, false);
	}
	@Override
	public void OnChangeBattleState()
	{
		this.setBattleState(getBattleState(), true);
	}
	@Override
	public boolean CheckPlayTimeout(long now)
	{
		boolean bAllUserOffline = true;
		for (int i=0;i<memberCount;i++)
		{
			if (this.m_battleUserInfo[i] != null)
			{
				SSPlayer player = this.m_battleUserInfo[i].sPlayer;
				//如果有一个人连上去的话，就没有所有人断线
				if (player != null && player.bIfConnect == true)
				{
					bAllUserOffline = false;
					break;
				}
			}		
		}
		if (bAllUserOffline)
		{
			logger.debug("所有玩家离线，战斗结束");
			Finish(false);
			return true;
		}
		return false;
	}
	public void StartGameMode(SSHero hero,EGuideStepType stepType)
	{
		switch (stepType) 
		{
		case eType_StartGameMode:
			if (gameMode == null)
			{
				gameMode = new Ball(this);
			}
			gameMode.SetWaitTime(0);
			break;
		case eType_SpawHero:
			this.AddGuideHero(hero);
			break;
		}
	}
	public void AddGuideHero(SSHero hero)
	{
		CVector3D bornPos = new CVector3D(5.22f,0,13.14f);
		CVector3D bornDir = hero.GetCurDir();
		SSHero aiHero = AddHero(1L, bornPos, bornDir, null, 3, EGameObjectCamp.eGOCamp_2,true);
		if (aiHero == null)
		{
			return;
		}
		aiHero.bornPos = bornPos;
		aiHero.fpManager.AddPercentValue(EParameterCate.ReliveTime.value, 2000);
	}
}
