package com.chen.battle.skill.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResLastingSkillStateMessage extends Message
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
		return 1035;
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
	public boolean read(IoBuffer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeLong(buffer, playerId);
		writeLong(buffer, targetId);
		writeInt(buffer, PosX);
		writeInt(buffer, PosZ);
		writeInt(buffer, dirAngle);
		writeInt(buffer, skillId);
		return true;
	}

}
