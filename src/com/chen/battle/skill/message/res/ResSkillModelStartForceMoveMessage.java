package com.chen.battle.skill.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSkillModelStartForceMoveMessage extends Message
{
	public long playerId;
	public int speed;
	public int dirAngle;
	public int posX;
	public int posY;
	public int posZ;
	public int skillId;
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1028;
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
		writeInt(buffer, posX);
		writeInt(buffer, posY);
		writeInt(buffer, posZ);
		writeInt(buffer, dirAngle);
		writeInt(buffer, speed);
		return true;
	}

}
