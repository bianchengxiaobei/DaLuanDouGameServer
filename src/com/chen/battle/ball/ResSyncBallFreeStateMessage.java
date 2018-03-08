package com.chen.battle.ball;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResSyncBallFreeStateMessage extends Message
{
	public int PosX;
	public int PosZ;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1047;
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
		writeInt(this.messagePack, PosX);
		writeInt(this.messagePack, PosZ);
		super.write(buffer);
	}
	
}
