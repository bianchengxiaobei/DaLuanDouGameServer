package com.chen.friend.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAddFriendByIdMessage extends Message
{
	public long friendId;
	public byte relationShip;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1061;
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
		this.friendId = readLong(this.messageUnpacker);
		this.relationShip = readByte(this.messageUnpacker);	
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		
	}
	
}
