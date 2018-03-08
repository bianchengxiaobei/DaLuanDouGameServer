package com.chen.player.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBuyType;
import com.chen.battle.structs.PlayerBattleInfo;
import com.chen.collection.manager.CollectionManager;
import com.chen.data.manager.DataManager;
import com.chen.db.bean.Friend;
import com.chen.db.bean.Hero;
import com.chen.db.bean.Mail;
import com.chen.friend.message.res.ResAddFriendResultMessage;
import com.chen.friend.message.res.ResFrientListRemoveMessage;
import com.chen.friend.message.res.ResNotifyBecomeFriendMessage;
import com.chen.friend.structs.ERelationShip;
import com.chen.login.bean.FriendData;
import com.chen.login.message.res.ResGetHeroMessage;
import com.chen.login.message.res.ResGuideStepInfoMessage;
import com.chen.login.message.res.ResNotifyGoldMessage;
import com.chen.match.structs.MatchPlayer;
import com.chen.player.manager.PlayerManager;
import com.chen.utils.Tools;
import com.chen.utils.MessageUtil;


public class Player
{
	private Logger log = LogManager.getLogger(Player.class);
	//玩家id
	private long id;
	//创建的服务器
	private int createServerId;
	//用户id
	private String userId;
	//用户名字
	private String userName;
	//服务器中文名
	private String serverName;
	//所在平台名字
	private String webName;
	//登陆类型
	private int loginType;
	//玩家头像
	private int icon;
	//玩家昵称
	private String name;
	//玩家性别
	private byte sex;
	//玩家等级
	private int level;
	//玩家天梯分数
	private int rank;
	//游戏所在网关id编号
	private int gateId;
	//玩家登陆的时间
	private long loginTime;
	//玩家登陆的ip
	private String loginIp;
	//玩家在线时间长度
	private long onlineTime;
	//是否被封号
	private boolean forbid;
	//拥有的金币
	private int money;
	//拥有的点券
	private int ticket;
	//被加入黑名单次数
	private int addBlackCount;
	//经验值
	private int exp;
	//是否是重新连接
	private boolean isReconnect;
	//已经完成的向导id
	private String finishedGuideStep;
	//日常奖励时间点
	private long dailyTime;
	//日常奖励次数
	private int dailyCount;
	private transient int state;
	//GM等级 
	private transient int gmlevel;
	//-------------------------------战斗---------------------
	//玩家匹配信息
	private MatchPlayer matchPlayer;	
	//玩家战斗信息
	private PlayerBattleInfo battleInfo;
	
	public CollectionManager collectionManager;
	private List<Hero> heroList;
	public Map<Long, Friend> friendList;
	public transient Map<Long, Mail> mailList;
	public static final byte MaxFriendCount = 64;
	public Player()
	{
		matchPlayer = new MatchPlayer(this);	
		battleInfo = new PlayerBattleInfo();
		heroList = new ArrayList<>();
		friendList = new HashMap<>();
		mailList = new HashMap<>();
		collectionManager = new CollectionManager(this);
	}
	public boolean isForbid() {
		return forbid;
	}
	public void setForbid(boolean forbid) {
		this.forbid = forbid;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getTicket() {
		return ticket;
	}
	public void setTicket(int ticket) {
		this.ticket = ticket;
	}
	public int getAddBlackCount() {
		return addBlackCount;
	}
	public void setAddBlackCount(int addBlackCount) {
		this.addBlackCount = addBlackCount;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getCreateServerId() {
		return createServerId;
	}
	public void setCreateServerId(int createServerId) {
		this.createServerId = createServerId;
	}
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getGateId() {
		return gateId;
	}
	public void setGateId(int gateId) {
		this.gateId = gateId;
	}
	public byte getSex() {
		return sex;
	}
	public void setSex(byte sex) {
		this.sex = sex;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public long getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}
	public MatchPlayer getMatchPlayer() {
		return matchPlayer;
	}
	public void setMatchPlayer(MatchPlayer matchPlayer) {
		this.matchPlayer = matchPlayer;
	}
	public boolean isReconnect() {
		return isReconnect;
	}
	public void setReconnect(boolean isReconnect) {
		this.isReconnect = isReconnect;
	}
	public int getState() {
		return state;
	}
	public void setState(PlayerState state) {
		this.state = state.value;
	}
	public int getServerId() {
		// TODO Auto-generated method stub
		return createServerId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public PlayerBattleInfo getBattleInfo() {
		return battleInfo;
	}
	public void setBattleInfo(PlayerBattleInfo battleInfo) {
		this.battleInfo = battleInfo;
	}
	public String getFinishedGuideStep() {
		return finishedGuideStep;
	}
	public void setFinishedGuideStep(String finishedGuideStep) {
		this.finishedGuideStep = finishedGuideStep;
	}
	public List<Hero> getHeroList() {
		return heroList;
	}
	public void setHeroList(List<Hero> heroList) {
		this.heroList = heroList;
	}
	public void InitHero()
	{
		if (this.id != 0)
		{
			this.heroList.clear();
			List<Hero> heros = PlayerManager.getInstance().heroDao.selectById(this.id);
			this.heroList.addAll(heros);
		}
	}
	public void InitFriend()
	{
		this.friendList.clear();
		List<Friend> friends = PlayerManager.getInstance().friendDao.selectById(this.id);
		if (friends == null)
		{
			return;
		}
		for (Friend friend : friends)
		{
			this.friendList.put(friend.getFriendId(), friend);
		}
	}
	public void AddHero(int heroId)
	{
		if (this.id != 0)
		{
			log.debug("fefe:"+heroId);
			Hero hero = new Hero();
			hero.setHeroId(heroId);
			hero.setRoleId(this.id);
			hero.setServer(this.createServerId);
			//更新缓存
			this.getHeroList().add(hero);
			//更新数据库
			PlayerManager.getInstance().heroDao.insert(hero);
			//通知客户端购买成功
			ResGetHeroMessage message = new ResGetHeroMessage();
			message.heroId = heroId;
			MessageUtil.tell_player_message(this, message);
		}	
	}
	public void Offline()
	{
		//this.setState(PlayerState.Quit);
		if (this.battleInfo.battleState.value < EBattleState.eBattleState_Async.value)
		{
			switch (this.battleInfo.battleTyoe) {
			case eBattleType_Match:
				BattleManager.getInstance().removeMatchUser(this);
				break;
			case eBattleType_Room:
				
				break;
			}
		}
		else
		{
			BattleContext battleContext = BattleManager.getInstance().getBattleContext(this);
			if (battleContext != null)
			{
				battleContext.OnUserOffline(this);
			}
			else
			{
				log.error("找不到战斗线程");
			}
		}
	}
	public void PostGuideStep()
	{
		ResGuideStepInfoMessage message = new ResGuideStepInfoMessage();
		if (this.finishedGuideStep == null || this.finishedGuideStep.equals(""))
		{
			message.allComp = false;
			MessageUtil.tell_player_message(this, message);
			return;
		}
		if (this.finishedGuideStep.indexOf("ok") != -1)
		{
			message.allComp =true;
		}else
		{
			message.allComp = false;
			String[] steps = this.finishedGuideStep.split(",");
			int size = steps.length;
			message.finishedList = new int[size];
			for(int i=0; i<size; i++)
			{
				message.finishedList[i] = Integer.parseInt(steps[i]);
			}
		}
		MessageUtil.tell_player_message(this, message);
	}
	/**
	 * 购买商品
	 * @param goodId
	 * @param buyType
	 * @param num
	 */
	public void AskBuyGood(int goodId,byte buyType,byte num)
	{
		int cost = 0;
		boolean buySuccess = false;
		int goodType = 0;
		//判断商品类型
		if (Tools.IsHeroGood(goodId))
		{
			goodType = 1;
			//如果已经有改角色
			if (this.HasHero(goodId))
			{
				return ;
			}
			cost = this.GetHeroGoodCost(goodId, buyType);
		}
		do 
		{
			switch (EBuyType.values()[buyType])
			{
			case Gold:	
				if (cost <= this.money)
				{
					//改数据库数据
					int left = this.money-cost;
					this.setMoney(left);
					buySuccess = true;
					this.SyncCurGold();
					log.debug("购买角色花费："+cost+",剩余:"+left);
				}
				else
				{
					return;
				}
				break;
			case Diamond:
				
				break;
			}
		} while (false);
		if (buySuccess)
		{
			if (goodType == 1)
			{
				this.AddHero(goodId);
			}
		}
	}
	public boolean HasHero(int heroId)
	{
		for (Hero hero : heroList)
		{
			if (hero.getHeroId() == heroId)
			{
				return true;
			}
		}
		return false;
	}
	public int GetHeroGoodCost(int heroId,byte buyType)
	{
		int cost = 0;
		switch (EBuyType.values()[buyType])
		{
		case Gold:	
			cost = DataManager.getInstance().heroConfigXMLLoader.heroConfigMap.get(heroId).costGold;
			break;
		case Diamond:
			cost = DataManager.getInstance().heroConfigXMLLoader.heroConfigMap.get(heroId).costDimaond;
			break;
		}
		return cost;
	}
	
	public void SyncCurGold()
	{
		ResNotifyGoldMessage message = new ResNotifyGoldMessage();
		message.gold = this.money;
		MessageUtil.tell_player_message(this, message);
	}
	public int CheckUpgrade(int exp,int needExp1)
	{
		int allExp = this.exp + exp;
		int needExp = needExp1;
		while (allExp >= needExp)
		{
			allExp -= needExp;
			this.level++;
			needExp = DataManager.getInstance().playerExpConfigXMLLoader.levelToExpConfig.levelToExp.get(this.level);
		}
		this.exp = allExp;
		return needExp;
	}
	public void UpdateDataBase()
	{
		
	}
	/**
	 * 添加好友
	 * @param friendId
	 * @param relationShip
	 */
	public void AddFriendById(long friendId,byte relationShip)
	{
		if (relationShip == ERelationShip.Friend.value)
		{
			Player player = PlayerManager.getInstance().getPlayer(friendId);
			if (player != null)
			{
				this.AddToFriendList(player, relationShip);
			}
		}
		else
		{
			//添加黑名单
			log.debug("添加黑名单：thisId:"+this.id+",friend:"+friendId);
		}
	}
	/**
	 * 删除好友
	 * @param friendId
	 * @param relationShip
	 */
	public void DeleteFriend(long friendId,byte relationShip)
	{
		ERelationShip ship = ERelationShip.values()[relationShip];
		this.RemoveFriendCache(friendId, ship);
		//如果玩家在线，如果不在线呢？
		Player player = PlayerManager.getInstance().getPlayer(friendId);
		if (player != null)
		{
			player.RemoveFriendCache(this.getId(), ship);
		}
	}
	/**
	 * 接受或者拒绝成为好友
	 * @param playerId
	 * @param accept
	 */
	public void RepalyBecomeFriend(long playerId,boolean accept)
	{
		Player player = PlayerManager.getInstance().getPlayer(playerId);
		if (player == null)
		{
			log.error("回复添加好友的时候对方下线");
			return;
		}
		if (player == this)
		{
			log.error("不能是自己");
		}
		if (accept)
		{
			if (this.friendList.containsKey(playerId))
			{
				log.error("该玩家已经是你的好友了:"+playerId);
				return;
			}
			boolean bError = true;
			//双方都添加
			//先自己
			Friend friend = new Friend();
			friend.setFriendId(playerId);
			friend.setIcon((byte)player.getIcon());
			friend.setName(player.getName());
			friend.setRelationship(ERelationShip.Friend.value);
			friend.setRoleId(this.id);
			bError = this.AddFriendToCache(friend);	
			if(bError)
			{
				PlayerManager.getInstance().friendDao.insert(friend);
				this.NotifyAddFriend(playerId, ERelationShip.Friend);
			}
			
			//后对方
			Friend friend1 = new Friend();
			friend1.setFriendId(this.id);
			friend1.setIcon((byte)this.getIcon());
			friend1.setName(this.getName());
			friend1.setRelationship(ERelationShip.Friend.value);
			friend1.setRoleId(playerId);
			bError = player.AddFriendToCache(friend1);
			//数据库客户端
			if(bError)
			{
				PlayerManager.getInstance().friendDao.insert(friend1);
				player.NotifyAddFriend(this.id, ERelationShip.Friend);
			}
		}
		else
		{
			//拒绝就不做处理，其实可以返回给客户端通知下，但是没必要
			log.error("拒绝成为朋友");
		}
	}
	public boolean AddFriendToCache(Friend friend)
	{
		if (this.friendList.containsKey(friend.getRoleId()))
		{
			log.error("已经是你的好友:"+friend.getRoleId());
			return false;
		}
		else
		{
			this.friendList.put(friend.getFriendId(), friend);
			return true;
		}
	}

	private void AddToFriendList(Player friend,byte relaion)
	{
		if (friend == null)
		{
			log.debug("缓存中找不到该角色:"+friend.id);
			return;
		}
		if (friend.id == this.id)
		{
			log.error("不能自己添加自己:"+friend.id);
			return;
		}
		if (friend.getState() == PlayerState.Quit.value)
		{
			log.error("玩家没有在线:"+friend.id);
			return;
		}
		if (friend.getBattleInfo().battleId != 0)
		{
			log.error("玩家在战斗中:"+friend.id);
			return;
		}
		if (this.friendList.containsKey(friend.id))
		{
			log.error("该玩家已经是你的好友了:"+friend.id);
			return;
		}
		if (this.friendList.size() >= MaxFriendCount)
		{
			log.error("已经超过最多玩家数量，不能再添加了:"+friend.id);
			return;
		}
		//发送给好友通知
		ResNotifyBecomeFriendMessage message = new ResNotifyBecomeFriendMessage();
		message.targetId = this.id;
		message.name = this.name;
		message.icon = this.icon;
		MessageUtil.tell_player_message(friend, message);
	}
	public void RemoveFriendCache(long friendId,ERelationShip ship)
	{
		if (ship == ERelationShip.Friend)
		{			
			if (this.friendList.containsKey(friendId))
			{
				this.friendList.remove(friendId);
				//发送给客户端好友改变了
				ResFrientListRemoveMessage message = new ResFrientListRemoveMessage();
				message.friendId = friendId;
				MessageUtil.tell_player_message(this, message);
				//更改数据库
				PlayerManager.getInstance().friendDao.deleteFriend(this.id, friendId);
			}
		}
		else
		{
			
		}
	}
	public void NotifyAddFriend(long friendId,ERelationShip ship)
	{
		Friend friend = this.friendList.get(friendId); 
		ResAddFriendResultMessage message = new ResAddFriendResultMessage();
		FriendData data = new FriendData();
		data.icon = friend.getIcon();
		data.name = friend.getName();
		data.playerId = friendId;
		data.relationShip = ship.value;
		data.status = PlayerManager.getInstance().GetPlayerStatus(friendId);
		message.data = data;
		MessageUtil.tell_player_message(this, message);
	}
	public long getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(long dailyTime) {
		this.dailyTime = dailyTime;
	}
	public int getDailyCount() {
		return dailyCount;
	}
	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getGmlevel() {
		return gmlevel;
	}
	public void setGmlevel(int gmlevel) {
		this.gmlevel = gmlevel;
	}
}
