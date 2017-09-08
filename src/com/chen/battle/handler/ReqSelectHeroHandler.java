package com.chen.battle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.req.ReqSelectHeroMessage;
import com.chen.battle.structs.BattleContext;
import com.chen.command.Handler;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqSelectHeroHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqSelectHeroHandler.class);
	@Override
	public void action() 
	{
		try {
			ReqSelectHeroMessage msg = (ReqSelectHeroMessage) this.getMessage();
			Player player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
			if (player == null)
			{
				this.log.error("玩家还没有注册角色");
			}
			BattleContext battle = BattleManager.getInstance().getBattleContext(player);
			if (battle == null)
			{
				this.log.error("战场不存在");
			}
			battle.AskSelectHero(player, msg.heroId);
		} catch (Exception e) {
			this.log.error(e,e);
			e.printStackTrace();
		}		
	}

}
