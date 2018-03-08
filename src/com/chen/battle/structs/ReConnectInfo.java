package com.chen.battle.structs;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;

public class ReConnectInfo extends Bean
{
	public long playerId;
	public String nickName;
	public int heroId;
	public int campId;
	@Override
	public void read(MessageUnpacker messageUnpacker)
	{

	}

	@Override
	public void write(MessageBufferPacker messagePack)
	{
		writeLong(messagePack, playerId);
		writeInt(messagePack, heroId);
		writeString(messagePack, nickName);
		writeInt(messagePack, campId);
	}

}
