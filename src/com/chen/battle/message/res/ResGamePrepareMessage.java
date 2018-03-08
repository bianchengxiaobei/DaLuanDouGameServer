package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

/**
 * 服务器向客户端发送进入准备状态1014
 * @author chen
 *
 */
public class ResGamePrepareMessage extends Message
{
	private int timeLimit;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1017;
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
	public void read(ByteBuffer buf) {
	
	}

	@Override
	public void write(IoBuffer buf) {
		writeInt(this.messagePack, this.timeLimit);
		super.write(buf);
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

}
