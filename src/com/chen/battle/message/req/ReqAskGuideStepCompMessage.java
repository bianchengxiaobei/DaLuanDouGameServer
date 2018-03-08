package com.chen.battle.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskGuideStepCompMessage extends Message
{
	public int taskId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1053;
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
		this.taskId = readInt(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer arg0) 
	{
		
	}

}
