package com.chen.battle.skill.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSkillFlyEmitMessage extends Message
{
	public long playerId;
	public long targetId;
	public int PosX;
	public int PosZ;
	public int dirAngle;
	public int effectId;
	public int emitId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1032;
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
		writeLong(buffer, targetId);
		writeInt(buffer, PosX);
		writeInt(buffer, PosZ);
		writeInt(buffer, dirAngle);
		writeInt(buffer, effectId);
		writeInt(buffer, emitId);
		return true;
	}
	
}
