package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSkillHitTargetMessage extends Message
{
	public long theOwner;
	public long hitTarget;
	public int effectId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1041;
	}

	@Override
	public String getQueue()
	{
		
		return null;
	}

	@Override
	public String getServer()
	{
		
		return null;
	}

	@Override
	public void read(ByteBuffer buffer)
	{
	}

	@Override
	public void write(IoBuffer buffer)
	{
		writeLong(this.messagePack, theOwner);
		writeLong(this.messagePack, hitTarget);
		writeInt(this.messagePack, effectId);
		super.write(buffer);
	}
	
}
