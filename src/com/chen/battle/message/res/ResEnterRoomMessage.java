package com.chen.battle.message.res;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.battle.structs.RoomMemberData;
import com.chen.message.Message;
/**
 * 服务器发送给客户端进入匹配选人的房间1009
 * @author Administrator
 *
 */
public class ResEnterRoomMessage extends Message
{
	public long battleId;
	public RoomMemberData[] m_oData;
	public int m_nTimeLimit;
	public int m_nMapId;
	public byte m_btGameType;
//	public int[] canUseHeroList;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1014;
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
		writeLong(this.messagePack, this.battleId);
		writeArray(this.messagePack, this.m_oData);
		writeInt(this.messagePack, this.m_nTimeLimit);
		writeInt(this.messagePack, this.m_nMapId);
		writeByte(this.messagePack, this.m_btGameType);
//		writeIntList(this.messagePack, buf, canUseHeroList);
		super.write(buf);
	}

}
