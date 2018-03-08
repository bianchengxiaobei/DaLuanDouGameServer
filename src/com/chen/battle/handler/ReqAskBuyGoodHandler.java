package com.chen.battle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.message.req.ReqAskBuyGoodMessage;
import com.chen.command.Handler;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskBuyGoodHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqAskBuyGoodHandler.class);
	@Override
	public void action()
	{
		try
		{
			ReqAskBuyGoodMessage message = (ReqAskBuyGoodMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player != null)
				{
					log.debug(message.goodId);
					player.AskBuyGood(message.goodId, message.buyType, message.num);
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

}
