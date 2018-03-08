package com.chen.server.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResAskPingMessage extends Message
{
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1046;
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
		super.write(buffer);
	}
	
}
