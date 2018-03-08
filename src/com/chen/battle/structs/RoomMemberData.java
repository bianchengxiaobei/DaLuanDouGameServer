package com.chen.battle.structs;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;

public class RoomMemberData extends Bean
{
	public long playerId;
	public int level;
	public String name;
	public int icon;
	public boolean isReconnecting;
	public int camp;
	@Override
	public void read(MessageUnpacker messageUnpacker)
	{
		
	}

	@Override
	public void write(MessageBufferPacker messagePack)
	{
		writeLong(messagePack, this.playerId);
		writeInt(messagePack, this.level);
		writeString(messagePack, this.name);
		writeInt(messagePack, this.icon);
		writeBoolean(messagePack, this.isReconnecting);
		writeInt(messagePack, camp);
	}
}
