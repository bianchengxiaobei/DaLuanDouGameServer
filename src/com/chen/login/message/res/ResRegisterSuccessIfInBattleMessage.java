package com.chen.login.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResRegisterSuccessIfInBattleMessage extends Message
{
	public long playerId;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 10050;
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
		this.playerId = readLong(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		writeLong(this.messagePack, playerId);
		super.write(buffer);
	}

}
