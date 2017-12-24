package com.chen.battle.ball;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DebugGraphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.ball.message.res.ResBattleBallSorceChangeMessage;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.BattleUserInfo;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EBattleServerState;
import com.chen.battle.structs.EGameObjectCamp;
import com.chen.battle.structs.IBattleContextMode;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.utils.MessageUtil;

public class Ball implements IBattleContextMode
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
	private long occupyStateTime;
	public boolean bIfStart = false;
	public Map<EGameObjectCamp, BallSorce> campSorceTime = new HashMap<EGameObjectCamp, BallSorce>();
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
					occupyStateTime = now;
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
				if (now - occupyStateTime > 0)
				{
					SSHero hero = (SSHero)theOwner;
					if (hero != null)
					{
						long addTime = now - occupyStateTime;
						hero.occupyTime += addTime;
						this.campSorceTime.get(hero.camp).allTheTime += addTime;
						if (this.campSorceTime.get(hero.camp).allTheTime >= this.campSorceTime.get(hero.camp).notifyTime)
						{
							this.campSorceTime.get(hero.camp).notifyTime += 4000;
							ResBattleBallSorceChangeMessage message = new ResBattleBallSorceChangeMessage();
							message.teamId = hero.camp.value;
							message.time = (int)this.campSorceTime.get(hero.camp).allTheTime;
							MessageUtil.tell_battlePlayer_message(this.battle, message);
							//logger.debug("通知一次："+this.campSorceTime.get(hero.camp).allTheTime);
						}
						occupyStateTime = now;
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
		for (BattleUserInfo info : this.battle.getM_battleUserInfo())
		{
			if (info != null)
			{
				if (!campSorceTime.containsKey(info.camp))
				{
					//System.out.println(info.camp.toString());
					BallSorce ballSorce = new BallSorce();
					campSorceTime.put(info.camp, ballSorce);
					//campSorceTime.get(info.camp).thePlayers.add(info.sHero);
				}
				else
				{
					//campSorceTime.get(info.camp).thePlayers.add(info.sHero);
				}				
			}
		}
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
		switch (ballState)
		{
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
				return;
			}
			ResSyncBallInHeroMessage message = new ResSyncBallInHeroMessage();
			message.playerId = theOwner.id;
			MessageUtil.tell_player_message(((SSHero)theOwner).player.player, message);
			MessageUtil.tell_battlePlayer_message(battle, message);
			break;
		}
	}
	public void SyncScoreState()
	{
		for (Entry<EGameObjectCamp, BallSorce> current : this.campSorceTime.entrySet())
		{
			ResBattleBallSorceChangeMessage message = new ResBattleBallSorceChangeMessage();
			message.teamId = current.getKey().value;
			message.time = (int)current.getValue().allTheTime;
			MessageUtil.tell_battlePlayer_message(this.battle, message);
			//logger.debug("通知一次："+current.getValue().allTheTime);
		}
	}
}
