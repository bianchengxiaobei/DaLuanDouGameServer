package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResBattleTipMessage extends Message
{
	public int tipCode;
	public long battleAllTime;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1022;
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
		writeInt(this.messagePack, tipCode);
		writeLong(this.messagePack, battleAllTime);
		super.write(buffer);
	}

}
