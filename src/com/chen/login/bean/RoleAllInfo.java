package com.chen.login.bean;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import com.chen.message.Bean;
/**
 * 角色所有信息实体类
 * @author chen
 *
 */
public class RoleAllInfo extends Bean
{
	public RoleBasicInfo m_oBasicInfo;
	public HeroInfo m_oHeroInfo;
	public FriendListInfo m_oFriendInfo;
	public DailySignInfo m_oDailySignInfo;
	public CollectionInfo m_oCollectionInfo;
	public EmailInfo m_oEmailInfo;
	public RoleAllInfo()
	{
		this.m_oBasicInfo = new RoleBasicInfo();
	}
	@Override
	public void write(MessageBufferPacker messagePack) 
	{
		this.m_oBasicInfo.write(messagePack);;
		this.m_oHeroInfo.write(messagePack);;
		m_oFriendInfo.write(messagePack);
		this.m_oDailySignInfo.write(messagePack);
		this.m_oCollectionInfo.write(messagePack);
		this.m_oEmailInfo.write(messagePack);
	}
	@Override
	public void read(MessageUnpacker messageUnpacker) 
	{
		
	}	
}
