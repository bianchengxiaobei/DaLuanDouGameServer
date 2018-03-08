package com.chen.match.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResMatchTeamPlayerInfoMessage extends Message
{
	public byte pos;
	public String nickName;
	public int icon;
	public boolean isInsert;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1012;
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
		writeByte(this.messagePack, pos);
		writeString(this.messagePack, nickName);
		writeInt(this.messagePack, icon);
		writeBoolean(this.messagePack, isInsert);
		super.write(buffer);
	}
}
