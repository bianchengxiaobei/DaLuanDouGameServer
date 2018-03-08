package com.chen.db.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.DBServer;
import com.chen.db.bean.Collection;
import com.chen.db.bean.Friend;
import com.chen.db.bean.Role;

public class CollectionDao
{
	private Logger logger = LogManager.getLogger(CollectionDao.class);
	private SqlSessionFactory sqlMapper = DBServer.getInstance().getSqlMapper();
	public int insert(Collection collection)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			long start = System.currentTimeMillis();
			int rows = session.insert("game_collection.insert",collection);
			session.commit();
			long end = System.currentTimeMillis();
			logger.debug("插入Collection数据消耗的时间："+(end - start));
			return rows;
		}catch (Exception e) {
			logger.error(e,e);
			return -1;
		}finally{
			session.close();
		}	
	}
	public List<Collection> selectById(long roleId)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			long start = System.currentTimeMillis();
			List<Collection> list = session.selectList("game_collection.selectByRoleId",roleId);
			long end = System.currentTimeMillis();
			logger.debug("选择Collection根据id消耗的时间："+(end - start));
			return list;
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		}finally
		{
			session.close();
		}
	}
	public int update(Collection collection)
	{
		SqlSession session = sqlMapper.openSession();
		try 
		{
			long start = System.currentTimeMillis();
			int rows = session.update("game_collection.update", collection);
			session.commit();
			long end = System.currentTimeMillis();
			logger.debug("Update Collection耗费："+(end-start));
			return rows;
		} catch (Exception e) {
			logger.error(e,e);
			return -1;
		}finally
		{
			session.close();
		}
	}
}
