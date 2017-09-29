package com.chen.server.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResAskPingMessage extends Message
{
	//public long time;
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
	public boolean read(IoBuffer buffer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) {
		//writeLong(buffer, time);
		return true;
	}
	
}
