package com.chen.login.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Bean;
/**
 * 角色所有信息实体类
 * @author chen
 *
 */
public class RoleAllInfo extends Bean
{
	public RoleBasicInfo m_oBasicInfo;
	
	public RoleAllInfo()
	{
		this.m_oBasicInfo = new RoleBasicInfo();
	}
	@Override
	public boolean write(IoBuffer buf) {
		writeBean(buf, this.m_oBasicInfo);
		return true;
	}
	@Override
	public boolean read(IoBuffer buf) {
		this.m_oBasicInfo = (RoleBasicInfo)readBean(buf, RoleBasicInfo.class);
		return true;
	}	
}
