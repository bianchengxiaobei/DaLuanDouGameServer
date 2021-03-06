package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResDeadStateMessage extends Message
{
	public long playerId;
	public long killerId;
	public int posX;
	public int posZ;
	public int angle;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1042;
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
	public void read(ByteBuffer arg0)
	{
		
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeLong(this.messagePack, playerId);
		writeLong(this.messagePack, killerId);
		writeInt(this.messagePack, posX);
		writeInt(this.messagePack, posZ);
		writeInt(this.messagePack, angle);
		super.write(buffer);
	}

}
