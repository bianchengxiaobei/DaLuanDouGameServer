package com.chen.friend.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResNotifyBecomeFriendMessage extends Message
{
	public String name;
	public int icon;
	public long targetId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1063;
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
		writeLong(this.messagePack, targetId);
		writeInt(messagePack, icon);
		writeString(this.messagePack, name);
		super.write(buffer);
	}

}
