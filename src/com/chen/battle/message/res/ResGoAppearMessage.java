package com.chen.battle.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResGoAppearMessage extends Message
{
	public long playerId;
	public int dirX;
	public int dirY;
	public int posX;
	public int posY;
	public int hp;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1023;
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) {
		writeLong(buffer, playerId);
		writeInt(buffer, posX);
		writeInt(buffer, posY);
		writeInt(buffer, dirX);
		writeInt(buffer, dirY);
		writeInt(buffer, hp);
		return true;
	}

}
