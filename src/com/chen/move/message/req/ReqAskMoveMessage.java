package com.chen.move.message.req;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskMoveMessage extends Message
{
	public int x;
	public int z;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1021;
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
		this.x = readInt(buffer);
		this.z = readInt(buffer);
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		
		return true;
	}

}
