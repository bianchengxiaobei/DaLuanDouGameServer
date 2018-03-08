package com.chen.battle.skill.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResBuffEffectMessage extends Message
{
	public long playerId;
    public boolean buffStateEnd;
    public int time;
    public int effectId;
    public int projectId;
	@Override
	public int getId()
	{
		// TODO Auto-generated method stub
		return 1062;
	}

	@Override
	public String getQueue()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServer()
	{
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
		writeLong(this.messagePack, playerId);
		writeInt(this.messagePack, time);
		writeInt(this.messagePack, effectId);
		writeInt(this.messagePack, projectId);
		writeBoolean(this.messagePack, buffStateEnd);
		super.write(buffer);
	}
}
