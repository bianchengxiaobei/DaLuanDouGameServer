package com.chen.match.message.req;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;
/**
 * 客户端向游戏服务器发送请求匹配的消息1006
 * @author chen
 *
 */
public class ReqAutoMatchMessage extends Message
{
	public int m_nMapId;
	public byte m_btGameMode;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1007;
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
	public boolean read(IoBuffer buf) {
		this.m_nMapId = readInt(buf);
		this.m_btGameMode = readByte(buf);
		return true;
	}

	@Override
	public boolean write(IoBuffer buf) {
		writeInt(buf, m_nMapId);
		writeByte(buf, m_btGameMode);
		return true;
	}
	
}
