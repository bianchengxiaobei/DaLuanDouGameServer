package com.chen.battle.message.res;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.battle.structs.ReConnectInfo;
import com.chen.message.Message;

public class ResReConnectMessage extends Message
{
	public int battleState;
	public long battleId;
	public byte gameType;
	public int mapId;
	public long playerId;
	public long battleTime;
	public int timeLimit;
	public ReConnectInfo[] ReConnectInfo;
	@Override
	public int getId() 
	{
		// TODO Auto-generated method stub
		return 1038;
	}

	@Override
	public String getQueue()
	{
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
		writeInt(this.messagePack, battleState);
		writeLong(this.messagePack, battleId);
		writeByte(messagePack, gameType);
		writeInt(this.messagePack, mapId);
		writeLong(this.messagePack, playerId);
		writeLong(this.messagePack, battleTime);
		writeInt(messagePack, timeLimit);
		writeArray(this.messagePack,this.ReConnectInfo);
		super.write(buffer);
	}

}
