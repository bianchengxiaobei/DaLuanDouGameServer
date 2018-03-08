package com.chen.login.bean;

import java.util.HashMap;
import java.util.Map;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;

public class HeroInfo extends Bean
{
	public Map<Integer, HeroData> heros = new HashMap<>();
	@Override
	public void read(MessageUnpacker messageUnpacker) 
	{
		
	}

	@Override
	public void write(MessageBufferPacker messagePack)
	{
		writeMap(messagePack,heros);
	}

}
