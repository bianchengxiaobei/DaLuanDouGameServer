package com.chen.move.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskStopMoveMessage extends Message
{
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1026;
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
	public void read(ByteBuffer buffer) 
	{
		
	}

	@Override
	public void write(IoBuffer arg0)
	{
		
	}

}
