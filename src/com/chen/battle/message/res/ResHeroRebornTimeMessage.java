package com.chen.battle.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResHeroRebornTimeMessage extends Message
{
	public int rebornTime;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1044;
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
	public boolean read(IoBuffer arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer buffer) {
		// TODO Auto-generated method stub
		writeInt(buffer, rebornTime);
		return true;
	}
	
	
}
