package com.chen.match.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.command.Handler;
import com.chen.match.message.req.ReqStartMatchMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqStartMatchHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqStartMatchHandler.class);
	@Override
	public void action() 
	{
		try {
			ReqStartMatchMessage msg = (ReqStartMatchMessage)this.getMessage();
			Player player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
			if (player == null)
			{
				log.error("玩家"+msg.getSession()+"没有注册该角色");
			}
			BattleManager.getInstance().askStartMatch(player);
		} catch (Exception e) {
			log.error(e,e);
		}		
	}

}
