package com.chen.login.message.res;

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
	public int server;
	public String userId;
	public int isInBattle;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 10005;
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
	public boolean write(IoBuffer buf) {
		writeBean(buf, this.roleAllInfo);
		writeInt(buf, server);
		writeString(buf, userId);
		writeInt(buf, isInBattle);
		return true;
	}

	@Override
	public boolean read(IoBuffer buf) {
		this.roleAllInfo = (RoleAllInfo)readBean(buf,RoleAllInfo.class);
		this.server = readInt(buf);
		this.userId = readString(buf);
		this.isInBattle = readInt(buf);
		return true;
	}

}
