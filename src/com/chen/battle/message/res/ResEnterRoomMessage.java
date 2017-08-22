package com.chen.battle.message.res;

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
	public List<RoomMemberData> m_oData = new ArrayList<>();
	public int m_nTimeLimit;
	public int m_nMapId;
	public byte m_btGameType;
	public List<Integer> canUseHeroList = new ArrayList<>();
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
	public boolean read(IoBuffer buf) {
		
		return true;
	}

	@Override
	public boolean write(IoBuffer buf) {
		writeLong(buf, this.battleId);
		writeInt(buf, this.m_oData.size());
		for (int i=0; i<this.m_oData.size(); i++)
		{
			writeBean(buf, this.m_oData.get(i));
		}
		writeInt(buf, this.m_nTimeLimit);
		writeInt(buf, this.m_nMapId);
		writeByte(buf, this.m_btGameType);
		writeIntList(buf, canUseHeroList);
		return true;
	}

}
