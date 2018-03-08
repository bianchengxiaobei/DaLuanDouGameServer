package com.chen.friend.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.friend.message.req.ReqRepalyBeFriendMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqRepalyBeFriendHandler extends Handler
{

	private Logger log = LogManager.getLogger(ReqRepalyBeFriendHandler.class);
	@Override
	public void action() 
	{
		try 
		{
			ReqRepalyBeFriendMessage message = (ReqRepalyBeFriendMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player != null)
				{
					player.RepalyBecomeFriend(message.friendId, message.bAccept);;
				}
			}
		}
		catch (Exception e)
		{
			log.error(e,e);
		}
	}

}
