package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

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
	public void read(ByteBuffer buffer)
	{

	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeLong(this.messagePack, playerId);
		writeLong(this.messagePack, targetId);
		writeInt(this.messagePack, PosX);
		writeInt(this.messagePack, PosZ);
		writeInt(this.messagePack, dirAngle);
		writeInt(this.messagePack, effectId);
		writeInt(this.messagePack, emitId);
		super.write(buffer);
	}
	
}
