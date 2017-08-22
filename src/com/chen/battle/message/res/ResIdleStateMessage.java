package com.chen.battle.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResIdleStateMessage extends Message
{
	public long playerId;
	public float posX;
	public float posY;	
	public float posZ;
	public float dirX;
	public float dirY;
	public float dirZ;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1024;
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
	public boolean read(IoBuffer buffer) 
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeLong(buffer, playerId);
		writeFloat(buffer, posX);
		writeFloat(buffer, posY);
		writeFloat(buffer, posZ);
		writeFloat(buffer, dirX);
		writeFloat(buffer, dirY);
		writeFloat(buffer, dirZ);
		return true;
	}
	
}
