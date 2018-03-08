package com.chen.login.bean;

import org.msgpack.core.MessageBufferPacker;

import com.chen.message.Bean;

public class DailySignInfo extends Bean
{
	public int dailyCount;
	public long leftTime;
	@Override
	public void write(MessageBufferPacker messageBufferPacker)
	{
		writeInt(messageBufferPacker, dailyCount);
		writeLong(messageBufferPacker, leftTime);
	}
}
