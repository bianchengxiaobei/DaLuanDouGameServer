package com.chen.friend.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskDelFriendMessage extends Message
{
	public long friendId;
    public byte relationShip;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1067;
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
		this.friendId = readLong(messageUnpacker);
		this.relationShip = readByte(messageUnpacker);
		readEnd();
	}
	@Override
	public void write(IoBuffer buffer)
	{
		
	}
}
