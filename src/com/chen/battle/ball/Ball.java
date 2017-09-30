package com.chen.battle.ball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EBattleServerState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.utils.MessageUtil;

public class Ball 
{
	public Logger logger = LogManager.getLogger(Ball.class);
	public SSGameUnit theOwner;
	private SSGameUnit lastOccupyObj = null;
	public BattleContext battle;
	public CVector3D dropPos;
	public CVector3D bornPos;//随机出生地点
	public EBallState ballState = EBallState.Free;
	public float colliderRadius = 0.3f;
	private long stateTime;
	public boolean bIfStart = false;
	
	public Ball(BattleContext battleContext) 
	{
		this.battle = battleContext;
		this.ballState = EBallState.Free;
		bIfStart = false;
	}
	
	public void OnHeartBeat(long now,long tick)
	{
		if (battle.getBattleState() != EBattleServerState.eSSBS_Playing || !bIfStart)
		{
			return;
		}
		int tempState = ballState.value;
		do {
			if (ballState == EBallState.Free)
			{
				this.theOwner = battle.FindAroundGo(bornPos, this.colliderRadius);
				if (theOwner != null)
				{
					lastOccupyObj = theOwner;
					ballState = EBallState.InHero;
					stateTime = now;
				}
			}
			if (ballState == EBallState.InHero)
			{
				if (theOwner == null)
				{
					logger.error("球没有主人");
					break;
				}
				if (theOwner.IsDead() || theOwner.bExpire)
				{
					ballState = EBallState.OnDorping;
					stateTime = now;
					bornPos = theOwner.GetCurPos();
					break;
				}
				//每隔多长时间增加一次
				if (now - stateTime > 10000)
				{
					SSHero hero = (SSHero)theOwner;
					if (hero != null)
					{
						hero.occupyPercent += 5;
						stateTime = now;
						//logger.debug("该英雄"+hero.id+":"+hero.occupyPercent);
					}
				}
				if (lastOccupyObj == theOwner)
				{
					if (now - stateTime > 60000)
					{
						logger.debug("超过一分钟，重置位置");
						theOwner = null;
						lastOccupyObj = null;
						ballState = EBallState.Free;
						ResetPos();
						return;
					}
				}
			}
			if (ballState == EBallState.OnDorping)
			{
				//延迟一秒
				if (stateTime + 1000 > now)
				{
					break;
				}
				ballState = EBallState.Free;
				stateTime = now;
			}
		} while (false);
		if (tempState != ballState.value)
		{
			this.SyncState();
		}
	}
	public void Start()
	{
		logger.debug("法球开始");
		bIfStart = true;
		ResetPos();
	}
	public void ResetPos()
	{
		boolean v = ResetPos(new CVector3D(2,0,2), false);
		if (v)
		{
			this.SyncState();
		}
	}
	public boolean ResetPos(CVector3D pos,boolean bImpact)
	{
		if (bImpact)
		{
			
		}
		this.bornPos = pos;
		return true;
	}
	public void SyncState()
	{
		switch (ballState) {
		case Free:
			ResSyncBallFreeStateMessage freeStateMessage = new ResSyncBallFreeStateMessage();
			freeStateMessage.PosX = (int)(this.bornPos.x * 1000);
			freeStateMessage.PosZ = (int)(this.bornPos.z * 1000);
			MessageUtil.tell_battlePlayer_message(battle, freeStateMessage);
			break;
		case OnDorping:
			ResSyncBallOnDropingMessage dropStateMessage = new ResSyncBallOnDropingMessage();
			dropStateMessage.PosX = (int)(this.bornPos.x * 1000);
			dropStateMessage.PosZ = (int)(this.bornPos.z * 1000);
			MessageUtil.tell_battlePlayer_message(battle, dropStateMessage);
			break;
		case InHero:
			if (theOwner == null)
			{
				logger.error("没有英雄");
			}
			ResSyncBallInHeroMessage message = new ResSyncBallInHeroMessage();
			message.playerId = theOwner.id;
			MessageUtil.tell_battlePlayer_message(battle, message);
			break;
		}
	}
}
