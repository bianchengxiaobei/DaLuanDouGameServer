package com.chen.player.manager;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.structs.EBattleState;
import com.chen.cache.impl.MemoryCache;
import com.chen.config.Config;
import com.chen.db.bean.Role;
import com.chen.db.dao.RoleDao;
import com.chen.login.bean.RoleBasicInfo;
import com.chen.login.message.res.ResRemoveCharacterToGateMessage;
import com.chen.player.structs.Player;
import com.chen.player.structs.PlayerState;
import com.chen.utils.MessageUtil;

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
		try {
			return players.get(roleId);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		
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
	 * 退出流程
	 * @param player
	 * @param bForce
	 */
	public void quitting(Player player,int bForce)
	{
		log.debug("玩家:"+player.getUserName()+"开始退出游戏服务器");
		player.Offline();
		this.quit(player);
	}
	public void quit(Player player)
	{
		//player.setState(PlayerState.Quit.value);
		//发送消息给网关移除角色
		ResRemoveCharacterToGateMessage message = new ResRemoveCharacterToGateMessage();
		message.playerId = player.getId();
		MessageUtil.send_to_gate(player.getGateId(), player.getId(), message);
		//好友下线通知
		//同步玩家信息到数据库
		if (player.getBattleInfo().battleState.value < EBattleState.eBattleState_Async.value)
		{
			removePlayer(player);
		}
	}
	/**
	 * 移除在线玩家
	 * @param player
	 */
	public void removePlayer(Player player)
	{
		players.remove(player.getId());
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
