package com.chen.login.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.login.message.req.ReqQuitToGameServerMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqQuitToGameServerHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqQuitToGameServerHandler.class);
	@Override
	public void action()
	{
		try 
		{
			ReqQuitToGameServerMessage message = (ReqQuitToGameServerMessage)getMessage();
			Player player = (Player)this.getParameter();
			if (player != null)
			{
				PlayerManager.getInstance().quitting(player, message.isForce);
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}		
	}

}
