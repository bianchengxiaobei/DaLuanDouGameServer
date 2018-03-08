package com.chen.db.bean;

public class GameMaster 
{
	private String username;
	private long userId;
	private int gmLevel;
	private int idDeleted;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getGmLevel() {
		return gmLevel;
	}
	public void setGmLevel(int gmLevel) {
		this.gmLevel = gmLevel;
	}
	public int getIdDeleted() {
		return idDeleted;
	}
	public void setIdDeleted(int idDeleted) {
		this.idDeleted = idDeleted;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
