package com.chen.login.bean;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;
/**
 * 角色基础信息实体类
 * @author chen
 *
 */
public class RoleBasicInfo extends Bean
{
	public long m_dwId;
	public String m_strAccount;
	public String m_strName;
	public int m_strIcon;
	public int m_dwLevel;
	public int m_dwExp;
	public int m_dwNeedExp;
	public int m_dwMoney;
	public int m_dwTicket;
	public int m_dwRank;
	public long m_dwOnlineTime;
	public long m_dwLoginTime;
	@Override
	public void write(MessageBufferPacker messagePack)  
	{
		writeLong(messagePack, m_dwId);
		writeString(messagePack, this.m_strAccount);
		writeString(messagePack, m_strName);
		writeInt(messagePack, this.m_strIcon);
		writeInt(messagePack, this.m_dwLevel);
		writeInt(messagePack, m_dwExp);
		writeInt(messagePack, m_dwNeedExp);
		writeInt(messagePack, this.m_dwMoney);
		writeInt(messagePack, this.m_dwTicket);
		writeInt(messagePack, m_dwRank);
		writeLong(messagePack, m_dwOnlineTime);
		writeLong(messagePack, m_dwLoginTime);
	}
	@Override
	public void read(MessageUnpacker messageUnpacker) 
	{
		this.m_dwId = readLong(messageUnpacker);
		this.m_strAccount = readString(messageUnpacker);
		this.m_strName = readString(messageUnpacker);
		this.m_strIcon = readInt(messageUnpacker);
		this.m_dwLevel = readInt(messageUnpacker);
		this.m_dwExp = readInt(messageUnpacker);
		this.m_dwMoney = readInt(messageUnpacker);
		this.m_dwTicket = readInt(messageUnpacker);
		this.m_dwOnlineTime = readLong(messageUnpacker);
		this.m_dwLoginTime = readLong(messageUnpacker);		
	}
}
