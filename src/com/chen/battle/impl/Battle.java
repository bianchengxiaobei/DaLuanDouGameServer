package com.chen.battle.impl;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBattleType;
import com.chen.data.bean.MapBean;
import com.chen.data.manager.DataManager;
import com.chen.match.structs.EBattleMatchType;
import com.chen.player.structs.Player;

public class Battle 
{
	private Logger log = LogManager.getLogger(Battle.class);
	private long battleId;
	private int serverId;
	private int mapId;
	private EBattleType battleType;
	private EBattleMatchType matchType;
	private HashMap<Integer, Player> userMap;
	private MapBean mapBean;
	public long getBattleId() {
		return battleId;
	}
	public void setBattleId(long battleId) {
		this.battleId = battleId;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public EBattleType getBattleType() {
		return battleType;
	}
	public void setBattleType(EBattleType battleType) {
		this.battleType = battleType;
	}
	public EBattleMatchType getMatchType() {
		return matchType;
	}
	public void setMatchType(EBattleMatchType matchType) {
		this.matchType = matchType;
	}
	public HashMap<Integer, Player> getUserMap() {
		return userMap;
	}
	public void setUserMap(HashMap<Integer, Player> userMap) {
		this.userMap = userMap;
	}
	public MapBean getMapBean() {
		return mapBean;
	}
	public void setMapBean(MapBean mapBean) {
		this.mapBean = mapBean;
	}
	
	public Battle(EBattleMatchType match_type,EBattleType type,
			long battleId,int mapId,HashMap<Integer, Player> userList)
	{
		this.matchType = match_type;
		this.battleType = type;
		this.battleId = battleId;
		this.mapId = mapId;
		this.mapBean = DataManager.getInstance().mapContainer.getMap().get(mapId);
		log.info("开始创建战场："+this.battleId);
		userMap = userList;				
	}
	public void start()
	{
		System.out.println("Battle Start");
		if (battleType != EBattleType.eBattleType_Room)
		{			
			BattleManager.getInstance().createBattle(this.userMap,this.battleId,this.matchType.getValue(),this.mapId);
		}
	}
	public void onCreate()
	{
		Iterator<Player> iter = this.userMap.values().iterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			player.getBattleInfo().setBattleState(EBattleState.eBattleState_Play);
		}
	}
}
