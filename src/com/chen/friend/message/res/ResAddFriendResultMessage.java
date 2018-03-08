package com.chen.friend.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.login.bean.FriendData;
import com.chen.message.Message;

public class ResAddFriendResultMessage extends Message
{
	public FriendData data;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1069;
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
		data.write(messagePack);
		super.write(buffer);
	}	
}
