package com.chen.collection.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResDailySignResultMessage extends Message
{
	public int errorCode;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1065;
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
		this.writeInt(messagePack, errorCode);
		super.write(buffer);
	}
	
}
