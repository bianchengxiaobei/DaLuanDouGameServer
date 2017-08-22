package com.chen.match.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResMatchTeamPlayerInfoMessage extends Message
{
	public byte pos;
	public String nickName;
	public int icon;
	public byte isInsert;
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
	public boolean read(IoBuffer buffer)
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) 
	{
		writeByte(buffer, pos);
		writeString(buffer, nickName);
		writeInt(buffer, icon);
		writeByte(buffer, isInsert);
		return true;
	}

}
