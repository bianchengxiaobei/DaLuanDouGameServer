package com.chen.match.structs;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MatchList_Ball extends MatchList
{
	private Logger logger = LogManager.getLogger(MatchList_Ball.class);
	private ConcurrentHashMap<Integer, MatchRoom_Normal> roomList;
	public MatchList_Ball(int mapId)
	{
		super(mapId);
		roomList = new ConcurrentHashMap<Integer, MatchRoom_Normal>();
	}
	@Override
	public boolean addOneTeam(MatchTeam team)
	{
		Iterator<MatchRoom_Normal> iter = roomList.values().iterator();
		while (iter.hasNext()) {
			MatchRoom_Normal matchRoom_Normal = iter.next();
			if (matchRoom_Normal != null)
			{
				if (matchRoom_Normal.addOneTeam(team))
				{
					return true;
				}
			}
			else
			{
				logger.error("Room == null");
			}					
		}
		MatchRoom_Normal room = new MatchRoom_Normal(this.mapBean.getM_nMapId());
		boolean success = room.addOneTeam(team);
		roomList.put(room.getRoomId(),room);
		return success;
	}
	@Override
	public boolean removeOneTeam(MatchTeam team)
	{
		for (MatchRoom_Normal room_Normal : roomList.values())
		{
			room_Normal.removeOneTeam(team);
			return true;
		}
		return false;
	}
	@Override
	public void update() {
		MatchRoom_Normal room = null;
		Iterator<MatchRoom_Normal> iter = roomList.values().iterator();
		while (iter.hasNext()) {
			room = iter.next();
			if (room == null)
			{
				logger.error("Room == null(Update)");
			}
			if (room.update())
			{
				room.Remove();
				System.out.println("移除匹配房间");
				iter.remove();			
			}
		}
	}
	@Override
	public boolean addInvitePlayer(MatchPlayer player, int matchRoomId,
			boolean isAccept) {
		// TODO Auto-generated method stub
		return false;
	}
}
