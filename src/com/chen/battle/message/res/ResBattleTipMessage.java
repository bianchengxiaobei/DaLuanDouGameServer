package com.chen.battle.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResBattleTipMessage extends Message
{
	public int tipCode;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1022;
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
		writeInt(buffer, tipCode);
		return true;
	}

}
