package com.chen.db.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.DBServer;
import com.chen.db.bean.Hero;

public class HeroDao
{
	private Logger logger = LogManager.getLogger(HeroDao.class);
	private SqlSessionFactory sqlMapper = DBServer.getInstance().getSqlMapper();
	public int insert(Hero hero)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			int rows = session.insert("game_hero.insert",hero);
			session.commit();
			return rows;
		}finally{
			session.close();
		}	
	}
	public List<Hero> selectById(long roleId)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			long start = System.currentTimeMillis();
			List<Hero> list = session.selectList("game_hero.selectHerosById",roleId);
			long end = System.currentTimeMillis();
			logger.debug("选择英雄根据id消耗的时间："+(end - start));
			return list;
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		}finally
		{
			session.close();
		}
	}
}
