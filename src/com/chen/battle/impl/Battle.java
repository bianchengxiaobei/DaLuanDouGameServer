package com.chen.battle.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.ai.structs.AIRobot;
import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.res.ResBattleFinishedAwardMessage;
import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBattleType;
import com.chen.data.bean.MapBean;
import com.chen.data.manager.DataManager;
import com.chen.match.manager.MatchManager;
import com.chen.match.structs.EBattleModeType;
import com.chen.player.structs.Player;
import com.chen.utils.MessageUtil;

public class Battle 
{
	private Logger log = LogManager.getLogger(Battle.class);
	private long battleId;
	private int serverId;
	private int mapId;
	private EBattleType battleType;
	private EBattleModeType matchType;
	protected Map<Integer, Player> userMap;
	protected Map<Integer, AIRobot> robotMap;
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
	public EBattleModeType getMatchType() {
		return matchType;
	}
	public void setMatchType(EBattleModeType matchType) {
		this.matchType = matchType;
	}
	public Map<Integer, Player> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<Integer, Player> userMap) {
		this.userMap = userMap;
	}
	public MapBean getMapBean() {
		return mapBean;
	}
	public void setMapBean(MapBean mapBean) {
		this.mapBean = mapBean;
	}
	
	public Battle(EBattleModeType match_type,EBattleType type,
			long battleId,int mapId,Map<Integer, Player> userList,Map<Integer, Integer> robot)
	{
		this.matchType = match_type;
		this.battleType = type;
		this.battleId = battleId;
		this.mapId = mapId;
		this.mapBean = DataManager.getInstance().mapContainer.getMap().get(mapId);
		this.robotMap = new HashMap<>();
		log.info("开始创建战场："+this.battleId);
		userMap = userList;		
		for (Map.Entry<Integer, Player> entry : userList.entrySet())
		{
			entry.getValue().getBattleInfo().battleCampType = entry.getKey();
		}
		if (robot != null)
		{
			long startId = 1 << 48;
			for (Entry<Integer, Integer> robotEntry : robot.entrySet())
			{
				AIRobot aiRobot = new AIRobot();
				this.robotMap.put(robotEntry.getKey(), aiRobot);
				aiRobot.id = startId++;
				aiRobot.campId = robotEntry.getValue();
				aiRobot.headId = 1;
				aiRobot.nickName = "机器人";
			}
		}
	}
	public void start()
	{
		if (battleType != EBattleType.eBattleType_Room)
		{			
			BattleManager.getInstance().createBattle(this.userMap,this.robotMap,this.battleId,this.matchType.getValue(),this.mapId);
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
	public void CaculateResult(int winCampId,Map<Long, Integer> sorce)
	{
		int gold = 0;
		int exp = 0;
		int rankSorce = 0;
		for (Entry<Integer, Player> usEntry : this.userMap.entrySet())
		{
			boolean win;
			Player player = usEntry.getValue();
			if (player.getBattleInfo().battleCampType == winCampId)
			{
				win = true;
				exp = this.caculateExp(win);
				//这里首胜什么的暂时不做
				gold = this.caculateGold(win);
			}
			else
			{
				win = false;
				exp = this.caculateExp(win);
				gold = this.caculateGold(win);
			}
			rankSorce = sorce.get(usEntry.getValue().getId());
			player.setRank(player.getRank() + rankSorce);
			//改变数据库,先改缓存等一起写进去
			player.setMoney(player.getMoney() + gold);
			player.SyncCurGold();
			int needExp = DataManager.getInstance().playerExpConfigXMLLoader.levelToExpConfig.levelToExp.get(player.getLevel());
			int curNeedExp = player.CheckUpgrade(exp,needExp);
			ResBattleFinishedAwardMessage message = new ResBattleFinishedAwardMessage();
			message.curExp = player.getExp();
			message.curLevel = player.getLevel();
			message.getExp = exp;
			message.getGold = gold;
			message.curNeedExp = curNeedExp;
			message.needExp = needExp;
			message.rank = rankSorce;
			MessageUtil.tell_player_message(player, message);
		}
		sorce = null;
	}
	public void OnFinish(int winCampId,Map<Long, Integer> sorce)
	{
		if (sorce != null)
		{
			this.CaculateResult(winCampId,sorce);
		}
		for (Entry<Integer, Player> usEntry : this.userMap.entrySet())
		{
			//这里还要判断是否是离线
			usEntry.getValue().getBattleInfo().reset();
		}
		this.ReCreateMatch();
	}
	public void ReCreateMatch()
	{
		if (battleType == EBattleType.eBattleType_Match)
		{
			for (Entry<Integer, Player> usEntry : this.userMap.entrySet())
			{
				//这里还要判断是否是离线
				MatchManager.getInstance().UserStopTeam(usEntry.getValue().getMatchPlayer());
				usEntry.getValue().getBattleInfo().changeTypeWithState(EBattleType.eBattleType_Match, EBattleState.eBattleState_Wait);
			}
		}
	}
	private int caculateExp(boolean win)
	{
		int exp = 0;
		if(win)
		{
			exp = 60;
		}
		else
		{
			exp = 40;
		}
		return exp;
		//这里还要判断是否有经验加成什么的道具
	}
	private int caculateGold(boolean win)
	{
		int gold = 0;
		if(win)
		{
			gold = 60;
		}
		else
		{
			gold = 40;
		}
		return gold;
		//这里还要判断是否有经验加成什么的道具
	}
}
