package com.chen.battle.message.res;

import java.nio.ByteBuffer;

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
	public void read(ByteBuffer buf) {

	}

	@Override
	public void write(IoBuffer buf) {
		writeLong(this.messagePack, playerId);
		writeInt(this.messagePack, heroId);
		super.write(buf);
	}	
}
