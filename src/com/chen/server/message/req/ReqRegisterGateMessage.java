package com.chen.server.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;
/**
 * 游戏服务器注册网关服务器请求消息10003
 * @author Administrator
 *
 */
public class ReqRegisterGateMessage extends Message
{
	// 服务器编号
	private int serverId;

	// 服务器名字
	private String serverName;

	/**
	 * 写入字节缓存
	 */
	@Override
	public void write(IoBuffer buf) {
		// 服务器编号
		writeInt(this.messagePack, this.serverId);
		// 服务器名字
		writeString(this.messagePack, this.serverName);
		super.write(buf);
	}

	/**
	 * 读取字节缓存
	 */
	@Override
	public void read(ByteBuffer buf) 
	{
		super.read(buf);
		// 服务器编号
		this.serverId = readInt(this.messageUnpacker);
		// 服务器名字
		this.serverName = readString(this.messageUnpacker);
	}

	/**
	 * get 服务器编号
	 * 
	 * @return
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * set 服务器编号
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * get 服务器名字
	 * 
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * set 服务器名字
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public int getId() {
		return 10003;
	}

	@Override
	public String getQueue() {
		return null;
	}

	@Override
	public String getServer() {
		return null;
	}
}
