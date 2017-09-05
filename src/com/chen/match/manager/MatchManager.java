package com.chen.match.manager;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBattleType;
import com.chen.match.structs.EBattleMatchType;
import com.chen.match.structs.MatchList;
import com.chen.match.structs.MatchList_AI;
import com.chen.match.structs.MatchList_Ladder;
import com.chen.match.structs.MatchList_Normal;
import com.chen.match.structs.MatchPlayer;
import com.chen.match.structs.MatchTeam;

public class MatchManager
{
	private Logger log = LogManager.getLogger(MatchManager.class);
	private static ConcurrentHashMap<Integer,MatchTeam> allTeamMap;
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, MatchList>> allMatchList;
	private static Object obj = new Object();
	private static MatchManager manager;
	private MatchManager()
	{
		allMatchList = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, MatchList>>();
		allTeamMap = new ConcurrentHashMap<Integer, MatchTeam>();
	}
	public static MatchManager getInstance()
	{
		synchronized (obj)
		{
			if (manager == null)
			{
				manager = new MatchManager();
			}
		}
		return manager;
	}
	/**
	 * 创建匹配组队，先添加自己玩家进入
	 * @param player
	 * @param matchType
	 * @param mapId
	 * @return
	 */
	public MatchTeam UserCreateTeam(MatchPlayer player,EBattleMatchType matchType,int mapId)
	{
		MatchTeam team = new MatchTeam(matchType, mapId);
		allTeamMap.put(team.getTeamId(), team);
		boolean success = team.addOneUser(player);
		if (success == false)
		{
			log.error("匹配组队添加玩家出错");
		}
		return team;
	}
	/**
	 * 移除匹配队员
	 * @param player
	 * @return
	 */
	public boolean removeMatchTeam(MatchPlayer player)
	{
		if (player.getPlayer().getBattleInfo().battleTyoe != EBattleType.eBattleType_Match)
		{
			return false;
		}
		if (player.getPlayer().getBattleInfo().battleState != EBattleState.eBattleState_Wait)
		{
			return false;
		}
		if (!allTeamMap.containsKey(player.getMatchTeamId()))
		{
			return false;
		}
		MatchTeam team = allTeamMap.get(player.getMatchTeamId());
		//如果是房主
		if (player.isMonster())
		{
			if (team.dissolve(player))
			{
				allTeamMap.remove(team);
				team = null;
				return true;
			}
		}
		//如果不是房主
		else if(team.RemoveOneUser(player))
		{
			return true;
		}
		return false;
	}
	/**
	 * 开始匹配玩家
	 * @param player
	 * @return
	 */
	public int TeamStartMatch(MatchPlayer player)
	{
		if (player.getPunishLeftTime() > 0)
		{
			//发送匹配失败的结果
			log.error("匹配失败，玩家"+player.getPlayer().getId()+"强退被惩罚");
		}
		/*if (!allTeamMap.containsKey(player.getMatchTeamId()))
		{
			return -1;
		}*/
		MatchTeam team = allTeamMap.get(player.getMatchTeamId());
		boolean isPunished = this.checkPunishTime(team);
		if (isPunished)
		{
			return 0;
		}
	/*	if (team.getStopedPlayers().size() > 0)
		{
			
		}*/
		MatchList list = null;
		ConcurrentHashMap<Integer, MatchList> map = allMatchList.get((int)team.getMatchType().getValue());
		if (map != null)
		{
			list = map.get(team.getMapId());
		}	
		if (null == list)
		{
			switch (team.getMatchType())
			{
				case MATCH_MODE_NORMAL:
					list = new MatchList_Normal(team.getMapId());			
					break;
				case MATCH_MODE_QUICK_AI:
					list = new MatchList_AI(team.getMapId());
					break;
				case MATCH_MODE_LADDER:
					list = new MatchList_Ladder(team.getMapId());
					break;
				default:
					break;
			}
			ConcurrentHashMap<Integer, MatchList> matchlist = new ConcurrentHashMap<Integer, MatchList>();
			matchlist.put(team.getMapId(), list);
			allMatchList.put((int)team.getMatchType().getValue(),matchlist);
		}	
		boolean success = list.addOneTeam(team);
		return success?1:0;
	}
	public boolean TeamStopMatch(MatchPlayer player)
	{
		if (!this.allTeamMap.containsKey(player.getMatchTeamId()))
		{
			return false;
		}
		MatchTeam team = this.allTeamMap.get(player.getMatchTeamId());
		if (team.isInMatch() == false)
		{
			return false;
		}
		MatchList list = this.allMatchList.get((int)(team.getMatchType().getValue())).get(team.getMapId());
		if (list != null)
		{
			return list.removeOneTeam(team);
		}
		else
		{
			return false;
		}
	}
	public void update()
	{
		for (int i=0; i<EBattleMatchType.MATCH_MODE_TOTAL.getValue(); i++)
		{
			ConcurrentHashMap<Integer, MatchList> listMap = allMatchList.get(i);
			if (listMap == null)
			{
				continue;
			}
			Iterator<MatchList> iter = listMap.values().iterator();
			if (null == iter)
			{
				continue;		
			}
			while (iter.hasNext()) {
				MatchList matchList = iter.next();
				matchList.update();
			}
		}
	}
	private boolean checkPunishTime(MatchTeam team)
	{
		int maxPunishTime = 0;
		long maxPunishPlayerId = 0;
		for (int i=0; i<team.getPlayerCount(); i++)
		{
			MatchPlayer player2 = team.getPlayers().get(i);
			int time = player2.getPunishLeftTime();
			if (time > 0)
			{
				if (time > maxPunishTime)
				{
					maxPunishTime = time;
					maxPunishPlayerId = player2.getPlayer().getId();
				}
			}
		}
		if (maxPunishTime > 0)
		{
			//发送匹配失败的消息
			return true;
		}
		return false;
	}
}
