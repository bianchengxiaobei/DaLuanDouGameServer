package com.chen.login.message.res;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.login.bean.RoleAllInfo;
import com.chen.message.Message;
/**
 * 服务器发送给客户端进入大厅消息1005
 * @author chen
 *
 */
public class ResEnterLobbyMessage extends Message
{
	public RoleAllInfo roleAllInfo = new RoleAllInfo();
	public boolean isInBattle;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1005;
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
	public void write(IoBuffer buf) {
		this.roleAllInfo.write(messagePack);
		writeBoolean(this.messagePack, isInBattle);
		super.write(buf);
	}

	@Override
	public void read(ByteBuffer buf) 
	{
		
	}

}
