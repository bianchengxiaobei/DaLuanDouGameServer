package com.chen.match.message.res;

import java.nio.ByteBuffer;

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
	public void read(ByteBuffer buffer) 
	{
		super.read(buffer);
		this.teamId = readInt(this.messageUnpacker);
		this.mapId = readInt(this.messageUnpacker);
		this.matchType = readByte(this.messageUnpacker);		
	}

	@Override
	public void write(IoBuffer buffer)
	{
		writeInt(this.messagePack, teamId);
		writeInt(this.messagePack, mapId);
		writeByte(this.messagePack, matchType);
		super.write(buffer);
	}

}
