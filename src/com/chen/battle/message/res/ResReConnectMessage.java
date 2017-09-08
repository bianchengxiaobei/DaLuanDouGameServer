package com.chen.battle.message.res;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.battle.structs.ReConnectInfo;
import com.chen.message.Message;

public class ResReConnectMessage extends Message
{
	public int battleState;
	public long battleId;
	public int mapId;
	public long playerId;
	public List<ReConnectInfo> ReConnectInfo = new ArrayList<>();
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
	public boolean read(IoBuffer buffer)
	{
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer)
	{
		writeInt(buffer, battleState);
		writeLong(buffer, battleId);
		writeInt(buffer, mapId);
		writeLong(buffer, playerId);
		writeInt(buffer, this.ReConnectInfo.size());
		for (int i=0;i<this.ReConnectInfo.size();i++)
		{
			writeBean(buffer, this.ReConnectInfo.get(i));
		}
		return true;
	}

}
