package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResHeroRebornMessage extends Message
{
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1043;
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
	public void write(IoBuffer arg0) {
		super.write(arg0);
	}
	
}
