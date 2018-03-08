package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResChangeHpMessage extends Message
{
	public long playerId;
	public int value;
	public int reason;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1040;
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
	public void read(ByteBuffer arg0)
	{
	
	}

	@Override
	public void write(IoBuffer buffer)
	{
		writeLong(this.messagePack, playerId);
		writeInt(this.messagePack, value);
		writeInt(this.messagePack, reason);
		super.write(buffer);
	}
	
}
