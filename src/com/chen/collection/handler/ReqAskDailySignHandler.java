package com.chen.collection.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.collection.message.req.ReqAskDailySignMessage;
import com.chen.command.Handler;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskDailySignHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqAskDailySignHandler.class);
	@Override
	public void action()
	{
		try 
		{
			ReqAskDailySignMessage message = (ReqAskDailySignMessage)this.getMessage();
			if (message != null)
			{
				PlayerManager.getInstance().AskDailySign(message.getRoleId().get(0));
			}
		} 
		catch (Exception e) 
		{
			log.error(e);
		}
	}

}
