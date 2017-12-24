package com.chen.battle.ball.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResBattleBallSorceChangeMessage extends Message
{
	public int teamId;
	public int time;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1050;
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
	public boolean read(IoBuffer buffer) {
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeInt(buffer, teamId);
		writeInt(buffer, time);
		return true;
	}
	
}
