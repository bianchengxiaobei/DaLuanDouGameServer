package com.chen.collection.structs;

import org.msgpack.core.MessageBufferPacker;

import com.chen.message.Bean;

public class CollectionItem extends Bean
{
	public int typeId;
	public int num;
	public int level;
	//public boolean canLocked;//是否能解锁
	public CollectionItem(int id,int num,int level)
	{
		this.typeId = id;
		this.num = num;
		this.level = level;
		//this.canLocked = false;
	}
	@Override
	public void write(MessageBufferPacker buffer)
	{
		this.writeInt(buffer, typeId);
		this.writeInt(buffer, num);
		this.writeInt(buffer, level);
	}
}
