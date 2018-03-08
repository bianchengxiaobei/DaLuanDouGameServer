package com.chen.battle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.req.ReqAskLockTargetMessage;
import com.chen.battle.structs.BattleContext;
import com.chen.command.Handler;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskLockTargetHanlder extends Handler
{
	private Logger log = LogManager.getLogger(ReqAskLockTargetHanlder.class);
	@Override
	public void action()
	{
		try 
		{
			ReqAskLockTargetMessage message = (ReqAskLockTargetMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player != null)
				{
					BattleContext battleContext = BattleManager.getInstance().getBattleContext(player);
					if(battleContext != null)
					{
						battleContext.AskLockTarget(battleContext.getUserBattleInfo(player).sHero, message.targetId);
					}
				}
			}
		} 
		catch (Exception e) 
		{
			this.log.error(e,e);
		}		
	}

}
