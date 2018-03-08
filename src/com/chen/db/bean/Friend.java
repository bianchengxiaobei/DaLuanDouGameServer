package com.chen.db.bean;

public class Friend
{
	private long roleId;
	private long friendId;
	private byte relationship;
	private byte icon;
	private String name;
	public byte status;
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	public byte getRelationship() {
		return relationship;
	}
	public void setRelationship(byte relationship) {
		this.relationship = relationship;
	}
	public byte getIcon() {
		return icon;
	}
	public void setIcon(byte icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
