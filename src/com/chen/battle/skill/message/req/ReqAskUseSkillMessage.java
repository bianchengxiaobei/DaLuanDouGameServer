package com.chen.battle.skill.message.req;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.apache.mina.core.buffer.IoBuffer;
import com.chen.message.Message;

public class ReqAskUseSkillMessage extends Message
{
	public int skillId;
	public int[] skillParams;
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
	public void read(ByteBuffer buffer)
	{
		super.read(buffer);
		this.skillId = readInt(this.messageUnpacker);
		this.skillParams = readIntList(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer arg0) {
	
	}

}
