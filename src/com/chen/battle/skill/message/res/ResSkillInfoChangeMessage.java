package com.chen.battle.skill.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSkillInfoChangeMessage extends Message
{
	public long playerId;
	public int skillSlotIndex;
	public int skillId;
	public int time;
	public int coolTime;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1027;
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
	public boolean read(IoBuffer buffer) 
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeLong(buffer, playerId);
		writeInt(buffer, skillId);
		writeInt(buffer, skillSlotIndex);
		writeInt(buffer, time);
		writeInt(buffer,coolTime);
		return true;
	}
	
}
