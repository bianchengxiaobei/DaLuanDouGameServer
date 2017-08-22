package com.chen.match.message.req;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqStartMatchMessage extends Message
{
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1008;
	}

	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return "Local";
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
		return true;
	}

}
