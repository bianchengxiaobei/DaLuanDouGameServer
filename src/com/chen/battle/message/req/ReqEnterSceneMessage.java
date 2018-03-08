package com.chen.battle.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqEnterSceneMessage extends Message
{

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1019;
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
	}

	@Override
	public void write(IoBuffer buffer)
	{
		
	}

}
