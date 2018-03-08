package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSkillModelStopForceMoveMessage extends Message
{
	public long playerId;
	public int posX;
	public int posY;
	public int PosZ;
	public int effectId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1029;
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
		writeInt(this.messagePack, posX);
		writeInt(this.messagePack, posY);
		writeInt(this.messagePack, PosZ);
		writeInt(this.messagePack, effectId);
		super.write(buffer);
	}
	
}
