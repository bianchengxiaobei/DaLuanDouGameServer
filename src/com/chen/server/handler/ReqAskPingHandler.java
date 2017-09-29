package com.chen.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.server.message.req.ReqAskPingMessage;
import com.chen.server.message.res.ResAskPingMessage;
import com.chen.utils.MessageUtil;

public class ReqAskPingHandler extends Handler
{
	Logger log = LogManager.getLogger(ReqAskPingHandler.class);

	@Override
	public void action() {
		try {
			ReqAskPingMessage msg = (ReqAskPingMessage) this
					.getMessage();
			Player player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
			if (player == null)
			{
				log.error("玩家没有注册角色");
				return;
			}
			ResAskPingMessage message = new ResAskPingMessage();
			MessageUtil.tell_player_message(player, message);
		} catch (ClassCastException e) {
			log.error(e);
		}
	}
}
