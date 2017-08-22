package com.chen.battle.message.req;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

/**
 * 客户端发送给服务器选定该神兽请求消息1011
 * @author chen
 *
 */
public class ReqSelectHeroMessage extends Message
{
	public int heroId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1015;
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
		this.heroId = readInt(buf);
		return true;
	}

	@Override
	public boolean write(IoBuffer arg0) {
		// TODO Auto-generated method stub
		return true;
	}	
}
