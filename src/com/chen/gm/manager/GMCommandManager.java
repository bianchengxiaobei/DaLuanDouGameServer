package com.chen.gm.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.bean.GameMaster;
import com.chen.db.dao.GameMasterDao;
import com.chen.player.structs.Player;

public class GMCommandManager
{
	private Logger logger = LogManager.getLogger(GMCommandManager.class);
	private GameMasterDao gameMasterDao;
	private static Object lock = new Object();
	private static GMCommandManager m_oInstance;
	private GMCommandManager()
	{
		gameMasterDao = new GameMasterDao();
	}
	public static GMCommandManager getInstance() 
	{
		synchronized (lock) 
		{
			if (m_oInstance == null)
			{
				m_oInstance = new GMCommandManager();
			}
		}
		return m_oInstance;
	}
	public void LoadGMLevel(Player player)
	{
		try 
		{
			long userId = Long.valueOf(player.getUserId());
			GameMaster gameMaster = gameMasterDao.selectByUserId(userId);
			if(gameMaster != null)
			{
				player.setGmlevel(gameMaster.getGmLevel());
			}
			else
			{
				player.setGmlevel(0);
			}
		} 
		catch (Exception e) 
		{
			logger.error(e);
		}
	}
}
