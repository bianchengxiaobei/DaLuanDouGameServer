package com.chen.player.manager;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.structs.EBattleState;
import com.chen.cache.impl.MemoryCache;
import com.chen.collection.config.DailySignConfig;
import com.chen.collection.manager.CollectionManager;
import com.chen.collection.message.res.ResDailySignResultMessage;
import com.chen.collection.structs.CollectionItem;
import com.chen.config.Config;
import com.chen.data.manager.DataManager;
import com.chen.db.bean.Friend;
import com.chen.db.bean.Hero;
import com.chen.db.bean.Mail;
import com.chen.db.bean.Role;
import com.chen.db.dao.CollectionDao;
import com.chen.db.dao.FriendDao;
import com.chen.db.dao.HeroDao;
import com.chen.db.dao.RoleDao;
import com.chen.login.bean.CollectionInfo;
import com.chen.login.bean.DailySignInfo;
import com.chen.login.bean.EmailInfo;
import com.chen.login.bean.FriendData;
import com.chen.login.bean.FriendListInfo;
import com.chen.login.bean.HeroData;
import com.chen.login.bean.HeroInfo;
import com.chen.login.bean.RoleBasicInfo;
import com.chen.login.manager.LoginManager;
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
	public RoleDao dao = new RoleDao();
	public HeroDao heroDao = new HeroDao();
	public FriendDao friendDao = new FriendDao();
	public CollectionDao collectionDao = new CollectionDao();
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
	 *取得缓存中所有的玩家
	 * @return
	 */
	public MemoryCache<Long, Player> getPlayers()
	{
		return players;
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
		player.setMoney(2000);
		player.setTicket(50);
		player.setDailyCount(1);
		player.setDailyTime(0);
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
		this.UpdatePlayer(player);
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
			player.setTicket(role.getTicket());
			player.setExp(role.getExp());
			player.setFinishedGuideStep(role.getFinishedGuideStep());
			player.setDailyCount(role.getDailyCount());
			player.setDailyTime(role.getDailyTime());
			player.setRank(role.getRank());
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
		role.setLoginip(player.getLoginIp());
		role.setOnlinetime(player.getOnlineTime());
		role.setAddblackcount(player.getAddBlackCount());
		role.setFinishedGuideStep(player.getFinishedGuideStep());
		role.setDailyCount(player.getDailyCount());
		role.setDailyTime(player.getDailyTime());
		role.setRank(player.getRank());
		dao.insert(role);
	}
	/**
	 * 更新玩家数据库
	 */
	public void UpdatePlayer(Player player)
	{
		Role role = new Role();
		role.setRoleId(player.getId());
		role.setLevel(player.getLevel());
		role.setExp(player.getExp());
		role.setRank(player.getRank());
		role.setMoney(player.getMoney());
		role.setTicket(player.getTicket());
		role.setLoginip(player.getLoginIp());
		role.setOnlinetime(player.getOnlineTime());
		role.setFinishedGuideStep(player.getFinishedGuideStep());
		role.setDailyCount(player.getDailyCount());
		role.setDailyTime(player.getDailyTime());
		dao.update(role);
	}
	/**
	 * 取得玩家基本信息
	 * @param player
	 * @return
	 */
	public RoleBasicInfo getPlayerBasicInfo(Player player) {
		RoleBasicInfo basicInfo = new RoleBasicInfo();
		basicInfo.m_dwExp = player.getExp();
		basicInfo.m_dwNeedExp = DataManager.getInstance().playerExpConfigXMLLoader.levelToExpConfig.levelToExp.get(player.getLevel());
		basicInfo.m_dwId = player.getId();
		basicInfo.m_dwLevel = player.getLevel();
		basicInfo.m_dwLoginTime = player.getLoginTime();
		basicInfo.m_dwMoney = player.getMoney();
		basicInfo.m_dwOnlineTime = player.getOnlineTime();
		basicInfo.m_dwTicket = player.getTicket();
		basicInfo.m_strAccount = player.getUserName();
		basicInfo.m_strIcon = player.getIcon();
		basicInfo.m_strName = player.getName();
		basicInfo.m_dwRank = player.getRank();
		return basicInfo;
	}
	public HeroInfo GetPlayerHeroInfo(Player player)
	{
		HeroInfo heroInfo = new HeroInfo();
		for (Hero hero : player.getHeroList())
		{
			HeroData data = new HeroData();
			data.heroId = hero.getHeroId();
			heroInfo.heros.put(hero.getHeroId(), data);
		}
		return heroInfo;
	}
	public FriendListInfo GetPlayerFriendInfo(Player player)
	{
		FriendListInfo info = new FriendListInfo();
		for (Friend friend : player.friendList.values())
		{
			FriendData data = new FriendData();
			data.playerId = friend.getFriendId();
			data.icon = friend.getIcon();
			data.name = friend.getName();
			data.status = this.GetPlayerStatus(friend.getFriendId());
			info.dicFriendList.put(friend.getFriendId(), data);
		}
		return info;
	}
	public DailySignInfo GetPlayerSignInfo(Player player)
	{
		DailySignInfo info = new DailySignInfo();
		info.dailyCount = player.getDailyCount();
		if (player.getDailyTime() == 0)
		{
			info.leftTime = 0;
		}
		else
		{
			long time = 86400000 - (System.currentTimeMillis() - player.getDailyTime());
			if (time < 0)
			{
				time = 0;
			}
			info.leftTime = time;
		}
		return info;
	}
	public EmailInfo GetPlayerEmailInfo(Player player)
	{
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.mails = new Mail[player.mailList.size()];
		emailInfo.mails = player.mailList.values().toArray(emailInfo.mails);
		return emailInfo;
	}
	public CollectionInfo GetPlayerCollectionInfo(Player player)
	{
		try
		{
			CollectionInfo info = new CollectionInfo();
			if(info.collectionItems == null)
			{
				System.err.println(player.collectionManager.collectionItems.values() == null);
				info.collectionItems = new CollectionItem[player.collectionManager.collectionItems.values().size()];
				info.collectionItems = player.collectionManager.collectionItems.values().toArray(info.collectionItems);
			}			
			return info;
		} 
		catch (Exception e) 
		{
			log.error(e);
			return null;
		}

	}
	public void AskDailySign(long playerId)
	{
		Player player = this.getPlayer(playerId);
		ResDailySignResultMessage message = new ResDailySignResultMessage();
		if (player != null)
		{
			//判断是否在时间范围内
			long curTime = System.currentTimeMillis();
			if (curTime - player.getDailyTime() >= 86400000)
			{
				boolean success = player.collectionManager.UpdateDaliyAward();
				if (success)
				{
					//取出配置表，增加奖励
					player.setDailyTime(curTime);
					player.setDailyCount(player.getDailyCount() + 1); 
					message.errorCode = 1;
				}
				else
				{
					message.errorCode = 0;
				}
			}
			else
			{
				message.errorCode = 2;			
			}
		}
		MessageUtil.tell_player_message(player, message);
	}
	public byte GetPlayerStatus(long playerId)
	{
		Player player = this.getPlayer(playerId);
		if (player == null)
		{
			return 0;//离线
		}
		else
		{
			boolean isInBattle = player.getBattleInfo().getBattleState().value < EBattleState.eBattleState_Async.value ? false : true;
			if (isInBattle)
			{
				return 2;//在线战斗中
			}
			else
			{
				return 1;//普通在线
			}
		}
	}
}
