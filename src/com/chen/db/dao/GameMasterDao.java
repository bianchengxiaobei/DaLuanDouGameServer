package com.chen.db.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.DBServer;
import com.chen.db.bean.GameMaster;

public class GameMasterDao 
{
	private Logger log = LogManager.getLogger(RoleDao.class);
	private SqlSessionFactory sqlMapper = DBServer.getInstance().getSqlMapper();
	
	public GameMaster selectByUserId(long userId)
	{
		SqlSession session = sqlMapper.openSession();
		try {
			GameMaster gm = session.selectOne("game_master.selectByUserId",userId);
			return gm;
		} catch (Exception e) {
			log.error(e);
			return null;
		}finally{
			session.close();
		}
	}
}
