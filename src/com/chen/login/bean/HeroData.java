package com.chen.login.bean;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;

public class HeroData extends Bean
{
	public int heroId;
	@Override
	public void read(MessageUnpacker messageUnpacker) 
	{
		
	}

	@Override
	public void write(MessageBufferPacker messagePack)
	{
		writeInt(messagePack, this.heroId);
	}
	
}
