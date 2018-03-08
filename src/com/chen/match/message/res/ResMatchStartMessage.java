package com.chen.match.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

/**
 * 游戏服务器向客户端发送匹配开始消息1008
 * @author chen
 *
 */
public class ResMatchStartMessage extends Message
{
	public boolean m_reason;
	public int m_waitTime;

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1013;
	}

	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void read(ByteBuffer buf) 
	{
		
	}

	@Override
	public void write(IoBuffer buf) 
	{
		writeBoolean(this.messagePack, m_reason);
		writeInt(this.messagePack, m_waitTime);
		super.write(buf);
	}
	
}
