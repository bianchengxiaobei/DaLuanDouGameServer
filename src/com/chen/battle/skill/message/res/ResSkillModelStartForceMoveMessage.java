package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

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
	public void read(ByteBuffer buffer) 
	{
		
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeLong(this.messagePack, playerId);
		writeInt(this.messagePack, skillId);
		writeInt(this.messagePack, posX);
		writeInt(this.messagePack, posY);
		writeInt(this.messagePack, posZ);
		writeInt(this.messagePack, dirAngle);
		writeInt(this.messagePack, speed);
		super.write(buffer);
	}

}
