package com.chen.player.manager;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.cache.impl.MemoryCache;
import com.chen.config.Config;
import com.chen.db.bean.Role;
import com.chen.db.dao.RoleDao;
import com.chen.login.bean.RoleBasicInfo;
import com.chen.player.structs.Player;

public class PlayerManager 
{
	private static Logger log = LogManager.getLogger(PlayerManager.class);
	private static Object obj = new Object();
	//玩家管理器的单例
	private static PlayerManager manager;
	//玩家数据缓存
	private static MemoryCache<Long, Player> players = new MemoryCache<Long, Player>();
	//玩家数据库操作
	private RoleDao dao = new RoleDao();
	private PlayerManager()
	{
		
	}
	public static PlayerManager getInstance()
	{
		synchronized (obj) 
		{
			if (null == manager)
			{
				manager = new PlayerManager();
			}
		}
		return manager;
	}
	/**
	 * 根据角色id，获取角色数据实体类
	 * @param roleId
	 * @return 角色数据实体类
	 */
	public Player getPlayer(long roleId)
	{
		return players.get(roleId);
	}
	/**
	 * 注册玩家，加入到玩家Map
	 * @param player
	 */
	public void registerPlayer(Player player)
	{
		players.put(player.getId(),player);
	}
	public Player createPlayer(String name,int icon,byte sex)
	{
		Player player = new Player();
		player.setId(Config.getId());
		player.setName(name);
		player.setLevel(1);
		player.setIcon(icon);
		player.setSex(sex);
		player.setMoney(0);
		return player;
	}
	/**
	 * 加载玩家
	 * @param playerId
	 * @return
	 */
	public Player loadPlayer(long playerId)
	{
		try {
			Role role = dao.selectById(playerId);
			if (role == null)
			{
				return null;
			}
			Player player = new Player();
			player.setId(playerId);
			player.setUserId(role.getUserId());
			player.setName(role.getName());
			player.setCreateServerId(role.getCreateServer());
			player.setLevel(role.getLevel());
			player.setIcon(role.getIcon());
			player.setMoney(role.getMoney());
			player.initHero();
			return player;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 插入玩家数据
	 * @param player
	 */
	public void insertPlayer(Player player)
	{
		Role role = new Role();
		role.setRoleId(player.getId());
		role.setUserId(player.getUserId());
		role.setCreateServer(player.getCreateServerId());
		role.setName(player.getName());
		role.setLevel(player.getLevel());
		role.setSex((int)player.getSex());
		role.setExp(player.getExp());
		role.setMoney(player.getMoney());
		role.setTicket(player.getTicket());
		role.setIcon(player.getIcon());
		role.setLoginIp(player.getLoginIp());
		role.setOnlineTime(player.getOnlineTime());
		role.setAddBlackCount(player.getAddBlackCount());
		dao.insert(role);
	}
	/**
	 * 取得玩家基本信息
	 * @param player
	 * @return
	 */
	public RoleBasicInfo getPlayerBasicInfo(Player player)
	{
		RoleBasicInfo basicInfo = new RoleBasicInfo();
		basicInfo.m_dwExp = (int)player.getExp();
		basicInfo.m_dwId = player.getId();
		basicInfo.m_dwLevel = player.getLevel();
		basicInfo.m_dwLoginTime = player.getLoginTime();
		basicInfo.m_dwMoney = player.getMoney();
		basicInfo.m_dwOnlineTime = player.getOnlineTime();
		basicInfo.m_dwTicket = player.getTicket();
		basicInfo.m_strAccount = player.getUserName();
		basicInfo.m_strIcon = player.getIcon();
		basicInfo.m_strName = player.getName();
		return basicInfo;
	}
}
