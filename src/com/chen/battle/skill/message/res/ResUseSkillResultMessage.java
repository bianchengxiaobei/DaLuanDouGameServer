package com.chen.battle.skill.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResUseSkillResultMessage extends Message
{
	public int skillId;
	public int errorCode;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1074;
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
	public void write(IoBuffer buffer)
	{
		writeInt(messagePack, skillId);
		writeInt(messagePack, errorCode);
		super.write(buffer);
	}
}
