package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResReleasingSkillStateMessage extends Message
{
	public long playerId;
	public long targetId;
	public int PosX;
	public int PosZ;
	public int dirAngle;
	public int skillId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1037;
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
	public void read(ByteBuffer arg0) {
		
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeLong(this.messagePack, playerId);
		writeLong(this.messagePack, targetId);
		writeInt(this.messagePack, PosX);
		writeInt(this.messagePack, PosZ);
		writeInt(this.messagePack, dirAngle);
		writeInt(this.messagePack, skillId);
		super.write(buffer);
	}

}
