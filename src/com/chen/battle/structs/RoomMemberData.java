package com.chen.battle.structs;



import org.apache.mina.core.buffer.IoBuffer;
import com.chen.message.Bean;

public class RoomMemberData extends Bean
{
	public long playerId;
	public int level;
	public String name;
	public int icon;
	public byte isReconnecting;
	public int camp;
	//public List<Integer> canUseHeroList = new ArrayList<>();
	@Override
	public boolean read(IoBuffer buf) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean write(IoBuffer buf) 
	{
		writeLong(buf, this.playerId);
		writeInt(buf, this.level);
		writeString(buf,this.name);
		writeInt(buf, this.icon);
		writeByte(buf, this.isReconnecting);
		writeInt(buf, camp);
		//writeIntList(buf, canUseHeroList);
		return true;
	}
}
