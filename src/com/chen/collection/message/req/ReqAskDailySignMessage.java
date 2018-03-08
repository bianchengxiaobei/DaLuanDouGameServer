package com.chen.collection.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskDailySignMessage extends Message
{
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1064;
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
				
	}
	@Override
	public void write(IoBuffer buffer)
	{
		
	}
}
