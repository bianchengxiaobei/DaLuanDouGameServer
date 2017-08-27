package com.chen.battle.skill.message.res;

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
	public boolean read(IoBuffer buffer) 
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer)
	{
		writeLong(buffer, playerId);
		writeInt(buffer, posX);
		writeInt(buffer, posY);
		writeInt(buffer, PosZ);
		writeInt(buffer, effectId);
		return true;
	}
	
}
