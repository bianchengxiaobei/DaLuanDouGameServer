package com.chen.match.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResMatchTeamBaseInfoMessage extends Message
{
	public int teamId;
	public int mapId;
	public byte matchType;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1011;
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
		this.teamId = readInt(buffer);
		this.mapId = readInt(buffer);
		this.matchType = readByte(buffer);
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer)
	{
		writeInt(buffer, teamId);
		writeInt(buffer, mapId);
		writeByte(buffer, matchType);
		return true;
	}

}
