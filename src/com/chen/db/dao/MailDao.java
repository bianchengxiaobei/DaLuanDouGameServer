package com.chen.db.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.DBServer;
import com.chen.db.bean.Mail;

public class MailDao
{
	private Logger logger = LogManager.getLogger(MailDao.class);
	private SqlSessionFactory sqlMapper = DBServer.getInstance().getSqlMapper();
	public List<Mail> SelectByRoleId(long roleId)
	{
		SqlSession session = sqlMapper.openSession();
		try 
		{
			List<Mail> list = session.selectList("game_mail.selectByRoleId",roleId);
			return list;
		} 
		catch (Exception e) 
		{
			logger.error(e);
			return null;
		}
		finally 
		{
			session.close();
		}
	}
	public List<Mail> SelectByMailId(long mailId)
	{
		SqlSession session = sqlMapper.openSession();
		try 
		{
			List<Mail> list = session.selectList("game_mail.selectById",mailId);
			return list;
		} 
		catch (Exception e) 
		{
			logger.error(e);
			return null;
		}
		finally 
		{
			session.close();
		}
	}
	public int Update(Mail mail)
	{
		SqlSession session = sqlMapper.openSession();
		try 
		{
			int row = session.update("game_mail.update",mail);
			session.commit();
			return row;
		} 
		catch (Exception e) 
		{
			logger.error(e);
		}
		finally 
		{
			session.close();
		}
		return 0;
	}
	public int insert(Mail mail)
	{
		SqlSession session = sqlMapper.openSession();
		try
		{
			int rows = session.insert("game_mail.insert",mail);
			session.commit();
			return rows;
		}
		catch (Exception e) 
		{
			logger.error(e);
		}
		finally 
		{
			session.close();
		}
		return 0;
	}
	public int delete(long mailId)
	{
		SqlSession session = sqlMapper.openSession();
		try 
		{
			int rows = session.delete("game_mail.delete",mailId);
			session.commit();
			return rows;
		} 
		catch (Exception e) 
		{
			logger.error(e);
		}
		finally 
		{
			session.close();
		}
		return 0;
	}
	public int deleteByRoleId(long roleId)
	{

		SqlSession session = sqlMapper.openSession();
		try 
		{
			int rows = session.delete("game_mail.deleteByRoleId",roleId);
			session.commit();
			return rows;
		} 
		catch (Exception e) 
		{
			logger.error(e);
		}
		finally 
		{
			session.close();
		}
		return 0;
	}
}
