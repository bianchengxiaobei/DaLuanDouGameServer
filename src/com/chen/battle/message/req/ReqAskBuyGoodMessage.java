package com.chen.battle.message.req;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ReqAskBuyGoodMessage extends Message
{
	public int goodId;
	public byte buyType;
	public byte num;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1056;
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
		super.read(buffer);
		this.goodId = readInt(this.messageUnpacker);
		this.buyType = readByte(this.messageUnpacker);
		this.num = readByte(this.messageUnpacker);
	}

	@Override
	public void write(IoBuffer buffer) 
	{
		
	}

}
