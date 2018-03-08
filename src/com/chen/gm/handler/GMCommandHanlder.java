package com.chen.gm.handler;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.gm.message.GMCommandMessage;
import com.chen.gm.structs.EGMCommandId;
import com.chen.mail.manager.MailServerManager;
import com.chen.player.structs.Player;

public class GMCommandHanlder extends Handler
{
	private Logger log = LogManager.getLogger(GMCommandHanlder.class);
	@Override
	public void action() 
	{
		try 
		{
			GMCommandMessage message = (GMCommandMessage)this.getMessage();
			Player player = (Player)this.getParameter();
			if (player == null)
			{
				log.error("不存在Player");
				return;
			}
			if (message.commands.size() == 0)
			{
				log.error("指令为空");
				return;
			}
			int gmLevel = player.getGmlevel();
			if (gmLevel <= 0)
			{
				return;
			}
			for (Entry<Integer, String> entry : message.commands.entrySet())
			{
				EGMCommandId type = EGMCommandId.values()[entry.getKey()];
				switch (type) 
				{
				case SendMail:
					String[] split = entry.getValue().split(",");
					long receiverId = Long.parseLong(split[0]);
					String title = split[1];
					String content = split[2];
					String gift = split[3];
					MailServerManager.getInstance().SendSystemMail(receiverId, title, content, gift);
					break;
				}
			}
		}
		catch (Exception e) 
		{
			log.error(e);
		}
		
	}

}
