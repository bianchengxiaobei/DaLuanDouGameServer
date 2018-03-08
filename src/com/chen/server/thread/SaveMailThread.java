package com.chen.server.thread;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.db.bean.Mail;
import com.chen.db.dao.MailDao;
import com.chen.mail.structs.EMailDealState;

public class SaveMailThread extends Thread
{
	private Logger logger = LogManager.getLogger(SaveMailThread.class);
	private String threadName;
	private MailDao dao = new MailDao();
	private LinkedBlockingQueue<MailInfo> mail_queue = new LinkedBlockingQueue<>();
	private static final int MaxSize = 10000;
	public boolean bStop;//是否停止了
	public boolean bInsertDB;
	public SaveMailThread(String threadName)
	{
		super(threadName);
		this.threadName = threadName;
	}
	@Override
	public void run()
	{
		bStop = false;
		while (!bStop || mail_queue.size() > 0)
		{
			MailInfo mailInfo = mail_queue.poll();
			if (mailInfo == null)
			{
				try {
					synchronized (this)
					{
						wait();
					}
				} catch (Exception e) {
					logger.error("保存Mail线程:"+threadName+"等待出现异常:"+e.getMessage());
				}
			}
			else
			{
				try 
				{
					if (mail_queue.size() > MaxSize)
					{
						bInsertDB = false;
					}
					if (bInsertDB)
					{
						if (mailInfo.deal == EMailDealState.Mail_Update)
						{
							if (this.dao.Update(mailInfo.mail) == 0)
							{
								logger.error("邮件更新数据出错:"+mailInfo.mail.getMailId());
							}
						}
						else if(mailInfo.deal == EMailDealState.Mail_Insert)
						{
							if (this.dao.insert(mailInfo.mail) == 0)
							{
								logger.error("邮件插入数据出错:"+mailInfo.mail.getMailId());
							}
						}
						else if(mailInfo.deal == EMailDealState.Mail_Delete)
						{
							this.dao.delete(mailInfo.mail.getMailId());
						}
						else if(mailInfo.deal == EMailDealState.Mail_DeleteAllById)
						{
							this.dao.deleteByRoleId(mailInfo.mail.getReceiverId());
						}
					}
					else
					{
						logger.error("不能处理邮件");
					}
				}
				catch (Exception e) 
				{
					logger.error(e);
					this.mail_queue.add(mailInfo);
				}				
			}
		}
	}
	public void stop(boolean flag)
	{
		bStop = true;
		try
		{
			synchronized (this)
			{
				notify();
			}
		} 
		catch (Exception e)
		{
			logger.error("邮件线程："+threadName+"Notify异常："+e.getMessage());
		}
	}
	private class MailInfo
	{
		public Mail mail;
		public EMailDealState deal;
		public MailInfo(Mail mail,EMailDealState s)
		{
			this.mail = mail;
			this.deal = s;
		}
	}
	/**
	 * 添加需要处理的邮件
	 * @param mail
	 * @param state
	 */
	public void AddDealMail(Mail mail,EMailDealState state)
	{
		try 
		{
			this.mail_queue.add(new MailInfo(mail,state));
			synchronized (this)
			{
				notify();
			}
		} 
		catch (Exception e)
		{
			logger.error(e);
		}
	}
}
