package com.chen.db.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.DBServer;
import com.chen.db.bean.Friend;

public class FriendDao 
{
	private Logger logger = LogManager.getLogger(FriendDao.class);
	private SqlSessionFactory sqlMapper = DBServer.getInstance().getSqlMapper();
	public int insert(Friend friend)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			long start = System.currentTimeMillis();
			int rows = session.insert("game_friend.insert",friend);
			session.commit();
			long end = System.currentTimeMillis();
			logger.debug("插入朋友数据消耗的时间："+(end - start));
			return rows;
		}catch (Exception e) {
			logger.error(e,e);
			return -1;
		}finally{
			session.close();
		}	
	}
	public List<Friend> selectById(long roleId)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			long start = System.currentTimeMillis();
			List<Friend> list = session.selectList("game_friend.select",roleId);
			long end = System.currentTimeMillis();
			logger.debug("选择朋友根据id消耗的时间："+(end - start));
			return list;
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		}finally
		{
			session.close();
		}
	}
	public int deleteFriend(long roleId,long friendId)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			HashMap<String, Long> map = new HashMap<>();
			map.put("roleId", roleId);
			map.put("friendId", friendId);
			long start = System.currentTimeMillis();
			int row = session.delete("game_friend.delete",map);
			long end = System.currentTimeMillis();
			logger.debug("删除朋友根据id消耗的时间："+(end - start));
			return row;
		} catch (Exception e) {
			logger.error(e,e);
			return 0;
		}finally
		{
			session.close();
		}
	}
}
