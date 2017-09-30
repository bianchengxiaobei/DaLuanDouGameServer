package com.chen.battle.ball;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSyncBallOnDropingMessage extends Message
{
	public int PosX;
	public int PosZ;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1049;
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeInt(buffer, PosX);
		writeInt(buffer, PosZ);
		return true;
	}

}
