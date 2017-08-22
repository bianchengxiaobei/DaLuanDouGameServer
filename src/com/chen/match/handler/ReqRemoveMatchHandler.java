package com.chen.match.handler;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.match.manager.MatchManager;
import com.chen.match.message.req.ReqRemoveMatchMessage;
import com.chen.match.message.res.ResRemoveMatchResultMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.utils.MessageUtil;

public class ReqRemoveMatchHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqRemoveMatchHandler.class);
	@Override
	public void action() 
	{
		try {
			ReqRemoveMatchMessage msg = (ReqRemoveMatchMessage)this.getMessage();
			Player player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
			if (player == null)
			{
				log.error("玩家"+msg.getSession()+"没有注册该角色");
			}
			boolean isOK = MatchManager.getInstance().removeMatchTeam(player.getMatchPlayer());
			if (isOK)
			{
				ResRemoveMatchResultMessage message = new ResRemoveMatchResultMessage();
				message.errorCode = 0;
				MessageUtil.tell_player_message(player, message);	
			}else
			{
				ResRemoveMatchResultMessage message = new ResRemoveMatchResultMessage();
				message.errorCode = 1;
				MessageUtil.tell_player_message(player, message);	
			}
		} catch (Exception e) {
			log.error(e,e);
		}		
	}
}
