package com.chen.battle.ball;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.DebugGraphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.ball.message.res.ResBattleBallSorceChangeMessage;
import com.chen.battle.skill.passiveSkill.EPassiveSkillTriggerType;
import com.chen.battle.skill.passiveSkill.PassiveSkillManager;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.BattleUserInfo;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EBattleServerState;
import com.chen.battle.structs.EGameObjectCamp;
import com.chen.battle.structs.IBattleContextMode;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.data.manager.DataManager;
import com.chen.match.structs.EBattleModeType;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
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
	private long waitTime = 0;
	public boolean bIfStart = false;
	Random random;
	public Map<EGameObjectCamp, BallSorce> campSorceTime = new HashMap<EGameObjectCamp, BallSorce>();
	public Ball(BattleContext battleContext) 
	{
		this.battle = battleContext;
		this.ballState = EBallState.Free;
		bIfStart = false;
		waitTime = 10000;
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
					for(Integer id : theOwner.GetPassiveSkill(EPassiveSkillTriggerType.EPassiveSkillTriggerType_Ball))
					{
						battle.passiveSkillManager.Trigger(id, theOwner, 0, 0, 0);
					}
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
					for(Integer id : theOwner.GetPassiveSkill(EPassiveSkillTriggerType.EPassiveSkillTriggerType_Ball))
					{
						battle.passiveSkillManager.DisTrigger(id);
					}
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
						this.campSorceTime.get(hero.camp).thePlayers.get(hero.player.player.getId()).sorce += addTime;
						if (this.campSorceTime.get(hero.camp).allTheTime >= this.campSorceTime.get(hero.camp).notifyTime)
						{
							this.campSorceTime.get(hero.camp).notifyTime += 4000;
							ResBattleBallSorceChangeMessage message = new ResBattleBallSorceChangeMessage();
							message.teamId = hero.camp.value;
							message.time = (int)this.campSorceTime.get(hero.camp).allTheTime;
							MessageUtil.tell_battlePlayer_message(this.battle, message);
							//logger.debug("通知一次："+this.campSorceTime.get(hero.camp).allTheTime);
							if (this.campSorceTime.get(hero.camp).allTheTime >= 4000*100)
							{
								battle.winCampId = hero.camp.value;
								//说明游戏结束
								this.battle.Finish(true);
							}
						}
						occupyStateTime = now;
						//logger.debug("该英雄"+hero.id+":"+hero.occupyPercent);
					}
				}
				if (lastOccupyObj == theOwner)
				{
					if (now - stateTime > 60000)
					{
						for(Integer id : theOwner.GetPassiveSkill(EPassiveSkillTriggerType.EPassiveSkillTriggerType_Ball))
						{
							battle.passiveSkillManager.DisTrigger(id);
						}
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
		if (bIfStart)
		{
			return;
		}
		logger.debug("法球开始");
		for(SSGameUnit player : this.battle.gameObjectMap.values())
		{
			if (player.IfHero())
			{
				battle.passiveSkillManager.AddPassiveSkill(player, 1);
			}
		}
		for (BattleUserInfo info : this.battle.m_battleUserInfo)
		{
			if (info != null)
			{
				if (!campSorceTime.containsKey(info.camp))
				{
					//System.out.println(info.camp.toString());
					BallSorce ballSorce = new BallSorce();
					campSorceTime.put(info.camp, ballSorce);
					campSorceTime.get(info.camp).thePlayers.put(info.sPlayer.player.getId(), new Sorce());
				}
				else
				{
					campSorceTime.get(info.camp).thePlayers.put(info.sPlayer.player.getId(), new Sorce());
				}				
			}
		}
		bIfStart = true;
		ResetPos();
	}
	public void ResetPos()
	{
		int rand = 0;
		if(battle.battleType != EBattleModeType.Game_Mode_Guide)
		{
			if(random == null)
				random = new Random();
			rand = random.nextInt(4);
		}		
		boolean v = ResetPos(DataManager.getInstance().ballBornPosConfig.bornPos[rand], false);
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
			//MessageUtil.tell_player_message(((SSHero)theOwner).player.player, message);
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

	@Override
	public boolean GetBWaitStart() 
	{		
		return true;
	}
	/**
	 * 等待10秒钟开始刷球
	 * @return
	 */
	@Override
	public long GetWaitTime() 
	{		
		return waitTime;
	}
	public void SetWaitTime(long va)
	{
		this.waitTime = va;
	}

	@Override
	public int CheckBattleFinish() 
	{
		long max = 0;
		EGameObjectCamp maxCamp = EGameObjectCamp.eGOCamp_AllEnemy;
		for (Entry<EGameObjectCamp, BallSorce> current : this.campSorceTime.entrySet())
		{			
			if (current.getValue().allTheTime > max)
			{
				max = current.getValue().allTheTime;
				maxCamp = current.getKey();
			}
		}
		return maxCamp.value;
	}

	@Override
	public void SyncReconnectState(Player player)
	{
		if (player == null)
		{
			return;
		}
		switch (ballState)
		{
		case Free:
			ResSyncBallFreeStateMessage freeStateMessage = new ResSyncBallFreeStateMessage();
			freeStateMessage.PosX = (int)(this.bornPos.x * 1000);
			freeStateMessage.PosZ = (int)(this.bornPos.z * 1000);
			MessageUtil.tell_player_message(player, freeStateMessage);
			break;
		case OnDorping:
			ResSyncBallOnDropingMessage dropStateMessage = new ResSyncBallOnDropingMessage();
			dropStateMessage.PosX = (int)(this.bornPos.x * 1000);
			dropStateMessage.PosZ = (int)(this.bornPos.z * 1000);
			MessageUtil.tell_player_message(player, dropStateMessage);
			break;
		case InHero:
			if (theOwner == null)
			{
				logger.error("没有英雄");
				return;
			}
			ResSyncBallInHeroMessage message = new ResSyncBallInHeroMessage();
			message.playerId = theOwner.id;
			MessageUtil.tell_player_message(player, message);
			break;
		}		
	}

	@Override
	public Map<Long, Integer> CaculateRank()
	{
		if (battle != null && battle.getBattleState() == EBattleServerState.eSSBS_Finished && battle.battleType != EBattleModeType.Game_Mode_Guide)
		{
			Map<Long, Integer> players = new HashMap<>();
			if (battle.winCampId == -1)
			{
				for (Entry<EGameObjectCamp, BallSorce> all :this.campSorceTime.entrySet())
				{
					long allWinSorce =  all.getValue().allTheTime;
					for (Entry<Long, Sorce> win : all.getValue().thePlayers.entrySet())
					{
						if (allWinSorce == 0)
						{
							allWinSorce = 1;
						}
						float rate = (float)win.getValue().sorce / (float)allWinSorce;
						int rank =  Math.round((1 + rate) * 40);
						players.put(win.getKey(), rank);
					}				
				}
				return players;
			}
			long allWinSorce = 0;
			try 
			{
				 allWinSorce = this.campSorceTime.get(EGameObjectCamp.values()[battle.winCampId+1]).allTheTime;			 
			}
			catch (Exception e) 
			{
				System.err.println(battle.winCampId);
				logger.error(e);
				System.err.println(e);
			}			
			for (Entry<EGameObjectCamp, BallSorce> all :this.campSorceTime.entrySet())
			{
				if(all.getKey().value == battle.winCampId)
				{
					for (Entry<Long, Sorce> win : all.getValue().thePlayers.entrySet())
					{
						float rate = (float)win.getValue().sorce / (float)allWinSorce;
						int rank =  Math.round((1 + rate) * 40);
						players.put(win.getKey(), rank);
					}
				}
				else
				{
					long allFailedSorce = all.getValue().allTheTime;
					for (Entry<Long, Sorce> failed : all.getValue().thePlayers.entrySet())
					{
						Player player = PlayerManager.getInstance().getPlayer(failed.getKey());
						if (player != null)
						{
							//小于白银一下的失败也加分
							if (player.getRank() <= 2000)
							{
								float rate = (float)failed.getValue().sorce / (float)allWinSorce;
								int rank =  Math.round((1 + rate) * 40);
								players.put(failed.getKey(), rank);
							}
							else
							{
								float rate = (float)failed.getValue().sorce / (float)(allFailedSorce+allFailedSorce*0.2);
								float readRank = (1 - rate) * 80;
								int rank =  -Math.round(readRank);
								players.put(failed.getKey(), rank);
							}
						}
						else
						{
							logger.error("Player == null");
						}
					}
				}
			}
			return players;
		}
		return null;
	}	
}
