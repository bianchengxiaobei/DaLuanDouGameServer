package com.chen.mail.manager;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.config.Config;
import com.chen.db.bean.Mail;
import com.chen.db.dao.MailDao;
import com.chen.mail.message.ResNotifyGetNewMailMessage;
import com.chen.mail.structs.EMailDealState;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.server.impl.GameServer;
import com.chen.server.thread.SaveMailThread;
import com.chen.utils.MessageUtil;

public class MailServerManager
{
	public Logger logger = LogManager.getLogger(MailServerManager.class);
	public static MailServerManager m_oInstance;
	
	public static Object lock = new Object();
	public MailDao dao = new MailDao();
	public static MailServerManager getInstance()
	{
		synchronized (lock)
		{
			if(m_oInstance == null)
			{
				m_oInstance = new MailServerManager();
			}
		}
		return m_oInstance;
	}
	/**
	 * 玩家登录时加载邮件
	 * @param player
	 */
	public void LoginLoadMail(Player player)
	{
		if (player == null)
		{
			logger.error("Player == null");
			return;
		}
		List<Mail> mails = dao.SelectByRoleId(player.getId());
		if (mails != null)
		{
			for(Mail mail : mails)
			{
				if (mail != null)
				{
					if(mail.getEndTime() < System.currentTimeMillis())
					{
						//删除
						GameServer.getInstance().wMailThread.AddDealMail(mail, EMailDealState.Mail_Delete);
					}
					else
					{
						player.mailList.put(mail.getMailId(), mail);
					}
				}
			}
		}
	}
	public boolean DeleteMail(Mail mail)
	{
		if (mail != null)
		{
			GameServer.getInstance().wMailThread.AddDealMail(mail, EMailDealState.Mail_Delete);
			return true;
		}
		return false;
	}
	public boolean DeleteMailByRoleId(Mail mail)
	{
		if (mail != null)
		{
			GameServer.getInstance().wMailThread.AddDealMail(mail, EMailDealState.Mail_DeleteAllById);
			return true;
		}
		return false;
	}
	public boolean SendSystemMail(long receiverId,String title,String content,String gift)
	{
		Mail mail = new Mail();
		mail.setSaveType(EMailDealState.Mail_Insert.value);
		mail.setSenderId(0);
		mail.setReceiverId(receiverId);
		mail.setTitle(title);
		mail.setContent(content);
		mail.setGift(gift);
		mail.setSendTime(System.currentTimeMillis());
		mail.setMailId(Config.getId());
		Player player = PlayerManager.getInstance().getPlayer(receiverId);
		if (player != null)
		{
			if (this.SaveMailToCacheMap(player, mail))
			{
				//通知客户端收到新邮件
				this.SendClientNewMail(player, mail);
				return true;
			}
		}
		else
		{
			if(this.SaveMailToDatabase(mail))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 保存邮件数据到数据库和缓存
	 * @param player
	 * @param mail
	 * @return
	 */
	public boolean SaveMailToCacheMap(Player player,Mail mail)
	{
		if(this.SaveMailToDatabase(mail))
		{
			if (player.mailList.containsKey(mail.getMailId()))
			{
				player.mailList.remove(mail.getMailId());
				player.mailList.put(mail.getMailId(), mail);
			}
			else
			{
				player.mailList.put(mail.getMailId(), mail);
			}
			return true;
		}
		return false;
	}
	public boolean SaveMailToDatabase(Mail mail)
	{
		if (mail != null)
		{
			GameServer.getInstance().wMailThread.AddDealMail(mail, EMailDealState.values()[mail.getSaveType()]);
			return true;
		}
		return false;
	}
	private void SendClientNewMail(Player player,Mail mail)
	{
		if (!player.mailList.isEmpty() && mail != null)
		{
			ResNotifyGetNewMailMessage message = new ResNotifyGetNewMailMessage();
			message.mail = mail;
			MessageUtil.tell_player_message(player, message);
		}
	}
}
