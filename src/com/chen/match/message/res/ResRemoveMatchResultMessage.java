package com.chen.match.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResRemoveMatchResultMessage extends Message
{
	public int errorCode;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1011;
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
		this.errorCode = readInt(buffer);
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) {
		// TODO Auto-generated method stub
		writeInt(buffer, errorCode);
		return true;
	}
	
}
