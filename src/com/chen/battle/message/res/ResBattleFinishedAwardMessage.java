package com.chen.battle.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResBattleFinishedAwardMessage extends Message
{
	public int getGold;
	public int getExp;
	public int curLevel;
	public int curExp;
	public int curNeedExp;
    public int needExp;
    public int rank;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1070;
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
	public void write(IoBuffer buffer)
	{
		writeInt(messagePack, getGold);
		writeInt(messagePack, getExp);
		writeInt(messagePack, curLevel);
		writeInt(messagePack, curExp);
		writeInt(messagePack, curNeedExp);
		writeInt(messagePack, needExp);
		writeInt(messagePack, rank);
		super.write(buffer);
	}
}
