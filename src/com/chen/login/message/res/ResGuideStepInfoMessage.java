package com.chen.login.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResGuideStepInfoMessage extends Message
{
	public boolean allComp;
	public int[] finishedList;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1051;
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
		writeBoolean(this.messagePack, allComp);
		if (finishedList == null)
		{
			finishedList = new int[0];
		}
		writeIntList(this.messagePack,buffer,finishedList);
		super.write(buffer);
	}

}
