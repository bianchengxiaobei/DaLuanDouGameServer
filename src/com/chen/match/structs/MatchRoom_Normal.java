package com.chen.match.structs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.data.bean.MapBean;
import com.chen.data.manager.DataManager;


public class MatchRoom_Normal 
{
	public Logger logger = LogManager.getLogger(MatchRoom_Normal.class);
	public MapBean getMapBean() {
		return mapBean;
	}

	public void setMapBean(MapBean mapBean) {
		this.mapBean = mapBean;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public boolean isInvalid() {
		return isInvalid;
	}

	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	public HashMap<Integer, Vector<MatchTeam>> getTeamMap() {
		return teamMap;
	}

	public void setTeamMap(HashMap<Integer, Vector<MatchTeam>> teamMap) {
		this.teamMap = teamMap;
	}

	public static int RoomId = 0;
	//地图配置
	private MapBean mapBean;
	//玩家数量
	private int userCount;
	//房间id
	private int roomId;
	//是否有效，当没有玩家的时候设置为无效
	private boolean isInvalid = false;
	//队伍集合
	private HashMap<Integer, Vector<MatchTeam>> teamMap;
	
	public MatchRoom_Normal(int mapId)
	{
		this.roomId = ++RoomId;
		this.userCount = 0;
		teamMap = new HashMap<Integer, Vector<MatchTeam>>();
		mapBean = DataManager.getInstance().mapContainer.getMap().get(mapId);
		this.isInvalid = false;
	}
	
	public boolean addOneTeam(MatchTeam team)
	{
		if (isInvalid)
		{
			logger.error("无效匹配房间");
			return false;
		}
		//分阵营
		for (int i=1;i<=this.mapBean.getPlayerModels().size();i++)
		{
			int curTeamSize = 0;
			Vector<MatchTeam> teams = this.teamMap.get(i);
			if (teams == null)
			{
				Vector<MatchTeam> myTeam = new Vector<MatchTeam>();
				myTeam.add(team);
				this.teamMap.put(i, myTeam);
				this.userCount += team.getPlayerCount();
				team.search(true);
				return true;
			}
			Iterator<MatchTeam> iter = teams.iterator();
			while (iter.hasNext()) {
				MatchTeam matchTeam = iter.next();
				curTeamSize += matchTeam.getPlayerCount();
			}
			if (curTeamSize+team.getPlayerCount() <= mapBean.getPlayerModels().get(i-1))
			{
				teams.add(team);
				this.userCount += team.getPlayerCount();
				//向客户端发送计时匹配界面开始
				team.search(true);
				//向客户端发送队友匹配数量变化的消息
				return true;
			}
		}
		return false;
	}
	public boolean removeOneTeam(MatchTeam team)
	{
		for (Vector<MatchTeam> t : this.teamMap.values())
		{
			if (t.contains(team))
			{
				t.remove(team);
				userCount -= team.getPlayerCount();
				if (userCount == 0)
				{
					isInvalid = true;
				}
				team.search(false);
				return true;
			}
		}
		return false;
	}
	public boolean update()
	{
		if (this.userCount == 0 || isInvalid)
			return true;
		if (userCount == mapBean.getMaxCount())
		{
			BattleManager.getInstance().onBattleMached(EBattleModeType.Game_Mode_Ball, mapBean.getM_nMapId(), teamMap);
			return true;
		}
		return false;
	}
}





