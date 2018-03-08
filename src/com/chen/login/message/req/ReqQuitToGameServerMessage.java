package com.chen.login.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqQuitToGameServerMessage extends Message
{
	public int isForce;

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 10035;
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
		super.read(buffer);
		this.isForce = readInt(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer buffer) {
		
	}
	
}
