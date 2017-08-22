package com.chen.battle.message.res;

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
	public boolean read(IoBuffer buffer) {
		this.m_playerId = readLong(buffer);
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) {
		writeLong(buffer, m_playerId);
		return true;
	}
}
