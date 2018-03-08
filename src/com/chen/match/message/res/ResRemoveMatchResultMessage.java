package com.chen.match.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResRemoveMatchResultMessage extends Message
{
	public int errorCode;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1010;
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
		this.errorCode = readInt(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer buffer)
	{		
		writeInt(this.messagePack, errorCode);
		super.write(buffer);
	}
	
}
