package com.chen.battle.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

/**
 * 服务器发送给客户端加载场景消息1015
 * @author chen
 *
 */
public class ResEnterSceneMessage extends Message
{
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1018;
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
	public void read(ByteBuffer arg0) {
		
	}

	@Override
	public void write(IoBuffer arg0)
	{
		super.write(arg0);
	}
	
}
