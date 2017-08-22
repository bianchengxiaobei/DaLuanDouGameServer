package com.chen.player.structs;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.structs.PlayerBattleInfo;
import com.chen.db.bean.Hero;
import com.chen.db.dao.HeroDao;
import com.chen.match.structs.MatchPlayer;


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
	private long exp;
	//是否是重新连接
	private boolean isReconnect;
	
	private transient int state;
	//-------------------------------战斗---------------------
	//玩家匹配信息
	private MatchPlayer matchPlayer;	
	//玩家战斗信息
	private PlayerBattleInfo battleInfo;
	
	private List<Hero> heroList;
	
	public Player()
	{
		matchPlayer = new MatchPlayer(this);	
		battleInfo = new PlayerBattleInfo();
		setHeroList(new ArrayList<>());
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
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
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
	public void setState(int state) {
		this.state = state;
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
	
	public void initHero()
	{
		if (this.id != 0)
			this.setHeroList(new HeroDao().selectById(this.id));
	}
	public List<Hero> getHeroList() {
		return heroList;
	}
	public void setHeroList(List<Hero> heroList) {
		this.heroList = heroList;
	}
}
