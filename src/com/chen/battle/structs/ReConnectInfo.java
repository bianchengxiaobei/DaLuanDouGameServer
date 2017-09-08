package com.chen.battle.structs;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Bean;

public class ReConnectInfo extends Bean
{
	public long playerId;
	public String nickName;
	public int heroId;
	@Override
	public boolean read(IoBuffer arg0) 
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeLong(buffer, playerId);
		writeInt(buffer, heroId);
		writeString(buffer, nickName);
		return true;
	}

}
