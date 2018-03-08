package com.chen.match.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqRemoveMatchMessage extends Message
{
	public byte stopFlag;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1009;
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
	public void read(ByteBuffer buf) 
	{
		super.read(buf);
		this.stopFlag = readByte(this.messageUnpacker);		
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		
	}
	
}
