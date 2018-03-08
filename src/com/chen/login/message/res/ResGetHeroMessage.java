package com.chen.login.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResGetHeroMessage extends Message
{
	public int heroId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1058;
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
		
	}

	@Override
	public void write(IoBuffer buffer)
	{
		writeInt(this.messagePack, heroId);
		super.write(buffer);
	}

}
