package com.chen.friend.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.friend.message.req.ReqAskDelFriendMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskDelFriendHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqAddFriendByIdHandler.class);
	@Override
	public void action() 
	{
		try 
		{
			ReqAskDelFriendMessage message = (ReqAskDelFriendMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player != null)
				{
					player.DeleteFriend(message.friendId, message.relationShip);
				}
			}
		}
		catch (Exception e)
		{
			log.error(e,e);
		}
	}
}
