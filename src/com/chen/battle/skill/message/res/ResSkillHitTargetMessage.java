package com.chen.battle.skill.message.res;

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
	public boolean read(IoBuffer buffer)
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer)
	{
		writeLong(buffer, theOwner);
		writeLong(buffer, hitTarget);
		writeInt(buffer, effectId);
		return true;
	}
	
}
