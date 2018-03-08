package com.chen.move.message.req;

import java.nio.ByteBuffer;

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
	public void read(ByteBuffer buffer)
	{
		super.read(buffer);
		this.x = readInt(this.messageUnpacker);
		this.z = readInt(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		
	}

}
