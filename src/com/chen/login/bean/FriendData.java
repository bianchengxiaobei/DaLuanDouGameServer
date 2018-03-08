package com.chen.login.bean;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;

public class FriendData extends Bean
{
	public long playerId;
	public byte relationShip;
	public byte icon;
	public String name;
	public byte status;
	@Override
	public void read(MessageUnpacker messageUnpacker)
	{
		
	}

	@Override
	public void write(MessageBufferPacker messagePack)
	{
		writeLong(messagePack, playerId);
		writeByte(messagePack, relationShip);
		writeByte(messagePack, icon);
		writeString(messagePack, name);
		writeByte(messagePack, status);

	}

}
