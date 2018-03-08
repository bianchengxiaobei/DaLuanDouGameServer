package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResRunningStateMessage extends Message
{
	public long playerId;
	public int posX;
	public int posZ;
	public int angle;
	public int moveSpeed;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1025;
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
	public void read(ByteBuffer buffer) {
		
	}

	@Override
	public void write(IoBuffer buffer)
	{
		writeLong(this.messagePack, playerId);
		writeInt(this.messagePack, posX);
		writeInt(this.messagePack, posZ);
		writeInt(this.messagePack, angle);
		writeInt(this.messagePack, moveSpeed);
		super.write(buffer);
	}

}
