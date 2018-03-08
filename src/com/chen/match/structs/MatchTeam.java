package com.chen.match.structs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBattleType;
import com.chen.data.bean.MapBean;
import com.chen.data.manager.DataManager;
import com.chen.match.message.res.ResMatchStartMessage;
import com.chen.match.message.res.ResMatchTeamBaseInfoMessage;
import com.chen.match.message.res.ResMatchTeamPlayerInfoMessage;
import com.chen.player.structs.Player;
import com.chen.utils.MessageUtil;

public class MatchTeam 
{
	public static int TeamId = 0;
	private MapBean mapBean;
	private Vector<MatchPlayer> players = new Vector<MatchPlayer>();
	private int mapId;
	private int teamId;
	private EBattleModeType matchType;
	private boolean isInMatch;
	private HashMap<Long, Boolean> stopedPlayers = new HashMap<Long, Boolean>();
	
	public MatchTeam(EBattleModeType matchType,int mapId)
	{
		this.teamId = ++TeamId;
		this.matchType = matchType;
		this.mapId = mapId;
		this.mapBean = DataManager.getInstance().mapContainer.getList().get(mapId);
		this.isInMatch = false;
	}

	public MapBean getMapBean() {
		return mapBean;
	}

	public void setMapBean(MapBean mapBean) {
		this.mapBean = mapBean;
	}

	public Vector<MatchPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(Vector<MatchPlayer> players) {
		this.players = players;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public EBattleModeType getMatchType() {
		return matchType;
	}

	public void setMatchType(EBattleModeType matchType) {
		this.matchType = matchType;
	}

	public boolean isInMatch() {
		return isInMatch;
	}

	public void setInMatch(boolean isInMatch) {
		this.isInMatch = isInMatch;
	}

	public HashMap<Long, Boolean> getStopedPlayers() {
		return stopedPlayers;
	}

	public void setStopedPlayers(HashMap<Long, Boolean> stopedPlayers) {
		this.stopedPlayers = stopedPlayers;
	}
	
	
	public boolean addOneUser(MatchPlayer player)
	{
		if (this.isInMatch)
		{
			return false;
		}

		//发送给玩家的信息
		ResMatchTeamBaseInfoMessage msg1 = new ResMatchTeamBaseInfoMessage();
		msg1.teamId = this.teamId;
		msg1.matchType = this.matchType.getValue();
		msg1.mapId = this.mapId;
		MessageUtil.tell_player_message(player.getPlayer(), msg1);
		ResMatchTeamPlayerInfoMessage msg2 = new ResMatchTeamPlayerInfoMessage();
		msg2.pos = (byte)players.size();
		msg2.icon = player.getPlayer().getIcon();
		msg2.nickName = player.getPlayer().getName();
		msg2.isInsert = true;
		byte pos = 0;
		ResMatchTeamPlayerInfoMessage msg3 = new ResMatchTeamPlayerInfoMessage();
		Iterator<MatchPlayer> playerIter = players.iterator();
		while (playerIter.hasNext())
		{
			MatchPlayer p = playerIter.next();
			msg3.pos = pos;
			msg3.nickName = p.getPlayer().getUserName();
			msg3.icon = 0;
			msg3.isInsert = true;
			MessageUtil.tell_player_message(player.getPlayer(), msg3);
			MessageUtil.tell_player_message(p.getPlayer(), msg2);
		}
		MessageUtil.tell_player_message(player.getPlayer(),msg2);
		players.add(player);
		player.setMatchTeamId(this.teamId);
		if (players.size() == 1)
		{
			player.setMonster(true);
		}
		player.getPlayer().getBattleInfo().changeTypeWithState(EBattleType.eBattleType_Match, EBattleState.eBattleState_Wait);
		return true;
	}
	public boolean RemoveOneUser(MatchPlayer player)
	{
		if (isInMatch)
		{
			return false;
		}
		Iterator<MatchPlayer> iterator = players.iterator();
		int pos = 0;
		while (iterator.hasNext())
		{
			MatchPlayer player2 = iterator.next();
			if (player2.equals(player))
			{
				ResMatchTeamBaseInfoMessage message = new ResMatchTeamBaseInfoMessage();
				message.teamId = 0;
				message.matchType = EBattleModeType.Game_Mode_INVALID.getValue();
				message.mapId = 0;
				//MessageUtil.tell_player_message(player2.getPlayer(),message);
				player2.setMatchTeamId(0);
				iterator.remove();
				ResMatchTeamPlayerInfoMessage message2 = new ResMatchTeamPlayerInfoMessage();
				message2.pos = (byte)pos;
				message2.nickName = player2.getPlayer().getUserName();
				message2.icon = 0;
				for (MatchPlayer player3 : players)
				{
					//MessageUtil.tell_player_message(player3.getPlayer(), message2);
				}
				player.getPlayer().getBattleInfo().reset();
				this.stopedPlayers.remove(player.getPlayer().getId());
				return true;
			}
		}
		return false;
	}
	/**
	 * 取得匹配队伍里面的玩家数量
	 * @return
	 */
	public int getPlayerCount()
	{
		return this.players.size();
	}
	/**
	 * 开始寻找队友和敌方对于
	 * @param isMatch
	 */
	public void search(boolean isMatch)
	{
		this.isInMatch = isMatch;
		//给客户端发送开始搜索队友的消息，和计时等待时间
		ResMatchStartMessage msg = new ResMatchStartMessage();
		if (isMatch)
		{
			msg.m_reason = true;
		}else
		{
			msg.m_reason = false;
		}
		//根据匹配人数动态调整
		msg.m_waitTime = 90;
		for (int i=0; i<this.players.size(); i++)
		{
			MessageUtil.tell_player_message(this.players.get(i).getPlayer(),msg);
		}
	}
	/**
	 * 解散匹配队员
	 * @param player
	 * @return
	 */
	public boolean dissolve(MatchPlayer player)
	{
		if (this.isInMatch)
		{
			return false;
		}
		ResMatchTeamBaseInfoMessage message = new ResMatchTeamBaseInfoMessage();
		message.teamId = 0;
		message.mapId = 0;
		message.matchType = EBattleModeType.Game_Mode_INVALID.getValue();
		Iterator<MatchPlayer> iterator = players.iterator();
		while (iterator.hasNext())
		{
			MatchPlayer current = iterator.next();
			current.setMatchTeamId(0);
			current.setMonster(false);
			current.setPunishLeftTime(0);
			System.out.println("发送基础");
			current.getPlayer().getBattleInfo().reset();
			MessageUtil.tell_player_message(current.getPlayer(), message);
			stopedPlayers.remove(current.getPlayer().getId());
		}
		return true;
	}
	public void Stop(MatchPlayer player)
	{
		this.stopedPlayers.put(player.getPlayer().getId(), true);
	}
}
