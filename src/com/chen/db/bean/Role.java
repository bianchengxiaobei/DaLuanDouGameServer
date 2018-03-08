package com.chen.db.bean;



public class Role 
{
	private Long roleId;
	private String userId;
	private Integer createServer;
	private String name;
	private Integer sex;
	private Integer level;
	private int rank;
	private int exp;
	private Integer icon;
	private int money;
	private int ticket;
	private String data;
	private String finishedGuideStep;
	private String loginip;
	private Integer addblackcount;
	private Long onlinetime; 
	private long dailyTime;
	private int dailyCount;
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
	public Role() {
		
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getCreateServer() {
		return createServer;
	}
	public void setCreateServer(Integer createServer) {
		this.createServer = createServer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public Integer getIcon() {
		return icon;
	}
	public void setIcon(Integer icon) {
		this.icon = icon;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getFinishedGuideStep() {
		return finishedGuideStep;
	}
	public void setFinishedGuideStep(String finishedGuideStep) {
		this.finishedGuideStep = finishedGuideStep;
	}
	public String getLoginip() {
		return loginip;
	}
	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}
	public Integer getAddblackcount() {
		return addblackcount;
	}
	public void setAddblackcount(Integer addblackcount) {
		this.addblackcount = addblackcount;
	}
	public Long getOnlinetime() {
		return onlinetime;
	}
	public void setOnlinetime(Long onlinetime) {
		this.onlinetime = onlinetime;
	}
	public int getDailyCount() {
		return dailyCount;
	}
	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}
	public long getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(long dailyTime) {
		this.dailyTime = dailyTime;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}
