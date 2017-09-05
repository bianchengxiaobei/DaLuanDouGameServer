package com.chen.login.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResRemoveCharacterToGateMessage extends Message
{
	public long playerId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 10036;
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer)
	{
		writeLong(buffer, playerId);
		return true;
	}

}
