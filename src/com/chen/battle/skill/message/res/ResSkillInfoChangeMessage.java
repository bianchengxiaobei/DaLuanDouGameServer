package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSkillInfoChangeMessage extends Message
{
	public int skillId;
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
	public void read(ByteBuffer buffer) 
	{
		
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeInt(this.messagePack, skillId);
		writeInt(this.messagePack, coolTime);
		super.write(buffer);
	}
	
}
