package com.chen.login.message.req;

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
	public boolean read(IoBuffer buffer) {
		// TODO Auto-generated method stub
		this.isForce = readInt(buffer);
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) {
		// TODO Auto-generated method stub
		writeInt(buffer, isForce);
		return true;
	}
	
}
