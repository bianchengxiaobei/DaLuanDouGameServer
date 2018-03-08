package com.chen.login.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;
/**
 * 网关向游戏服务器发送创建角色请求
 * @author chen
 *
 */
public class ReqCreateCharacterToGameServerMessage extends Message
{
	private int gateId;
	private int createServer;
	private String userId;
	private String userName;
	private String name;
	private byte sex;//0-男，1-女
	private int icon;
	private byte isAdult;//0未成年，1成年
	private String optIp;
	private int loginType;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 10007;
	}
	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return "Local";
	}
	@Override
	public String getServer() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void read(ByteBuffer buf)
	{
		super.read(buf);
		this.gateId = readInt(this.messageUnpacker);
		this.createServer = readInt(this.messageUnpacker);
		this.userId = readString(this.messageUnpacker);
		this.userName = readString(this.messageUnpacker);
		this.name = readString(this.messageUnpacker);
		this.sex = readByte(this.messageUnpacker);
		this.icon = readInt(this.messageUnpacker);
		this.isAdult = readByte(this.messageUnpacker);
		this.optIp = readString(this.messageUnpacker);
		this.loginType = readInt(this.messageUnpacker);
		this.readEnd();
	}
	@Override
	public void write(IoBuffer buf) 
	{
		
	}
	public int getGateId() {
		return gateId;
	}
	public void setGateId(int gateId) {
		this.gateId = gateId;
	}
	public int getCreateServer() {
		return createServer;
	}
	public void setCreateServer(int createServer) {
		this.createServer = createServer;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public byte getIsAdult() {
		return isAdult;
	}
	public void setIsAdult(byte isAdult) {
		this.isAdult = isAdult;
	}
	public String getOptIp() {
		return optIp;
	}
	public void setOptIp(String optIp) {
		this.optIp = optIp;
	}
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	public byte getSex() {
		return sex;
	}
	public void setSex(byte sex) {
		this.sex = sex;
	}
}
