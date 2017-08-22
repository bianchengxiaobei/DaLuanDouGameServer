package com.chen.battle.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

/**
 * 服务器发送给客户端选定神兽的结果1013
 * @author chen
 *
 */
public class ResSelectHeroMessage extends Message
{
	public long playerId;
	public int heroId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1016;
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
	public boolean read(IoBuffer buf) {
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buf) {
		writeLong(buf, playerId);
		writeInt(buf, heroId);
		return true;
	}	
}
