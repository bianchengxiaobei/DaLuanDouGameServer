package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResBattleFinishMessage extends Message
{
	public int winCampId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1054;
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
		writeInt(this.messagePack, this.winCampId);
		super.write(buffer);
	}

}
