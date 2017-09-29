package com.chen.server.message.req;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskPingMessage extends Message
{
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1045;
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
	public boolean read(IoBuffer arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
