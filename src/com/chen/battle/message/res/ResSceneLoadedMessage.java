package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSceneLoadedMessage extends Message
{
	public long m_playerId = 0L;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1020;
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
	public void read(ByteBuffer buffer) 
	{
		super.read(buffer);
		this.m_playerId = readLong(this.messageUnpacker);		
	}

	@Override
	public void write(IoBuffer buffer) {
		writeLong(this.messagePack, m_playerId);
		super.write(buffer);
	}
}
