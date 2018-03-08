package com.chen.gm.message;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.chen.message.Message;

public class GMCommandMessage extends Message
{
	public Map<Integer, String> commands = new HashMap<>();
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 19999;
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
		this.commands.clear();
		super.read(buffer);
		int size = readInt(messageUnpacker);
		for (int i=0; i<size; i++)
		{
			int id = readInt(messageUnpacker);
			String content = readString(messageUnpacker);
			this.commands.put(id, content);
		}
		readEnd();
	}
}
