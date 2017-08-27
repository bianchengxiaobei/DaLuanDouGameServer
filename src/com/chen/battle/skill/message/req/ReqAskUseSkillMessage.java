package com.chen.battle.skill.message.req;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskUseSkillMessage extends Message
{
	public int skillId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1030;
	}

	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return "Local";
	}

	@Override
	public String getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean read(IoBuffer arg0) {
		// TODO Auto-generated method stub
		this.skillId = readInt(arg0);
		return true;
	}

	@Override
	public boolean write(IoBuffer arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
