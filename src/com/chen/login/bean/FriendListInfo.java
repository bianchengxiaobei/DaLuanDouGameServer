package com.chen.login.bean;

import java.util.HashMap;
import java.util.Map;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;

public class FriendListInfo extends Bean
{
	public Map<Long, FriendData> dicFriendList = new HashMap<>();
	@Override
	public void read(MessageUnpacker messageUnpacker)
	{
		
	}

	@Override
	public void write(MessageBufferPacker messagePack)
	{
		writeMap(messagePack, dicFriendList);
	}
	
}
