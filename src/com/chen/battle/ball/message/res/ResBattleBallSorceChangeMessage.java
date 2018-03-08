package com.chen.battle.ball.message.res;

import java.nio.ByteBuffer;

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
	public void read(ByteBuffer buffer)
	{
		
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeInt(this.messagePack, teamId);
		writeInt(this.messagePack, time);
		super.write(buffer);
	}
	
}
