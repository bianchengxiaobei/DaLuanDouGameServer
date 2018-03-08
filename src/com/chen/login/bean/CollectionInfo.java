package com.chen.login.bean;

import org.msgpack.core.MessageBufferPacker;

import com.chen.collection.structs.CollectionItem;
import com.chen.message.Bean;

public class CollectionInfo extends Bean
{
	public CollectionItem[] collectionItems;
	
	@Override
	public void write(MessageBufferPacker bufferPacker)
	{
		this.writeArray(bufferPacker, collectionItems);
	}
}
