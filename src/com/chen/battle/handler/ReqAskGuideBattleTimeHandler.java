package com.chen.battle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.impl.GuideBattle;
import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.req.ReqAskGuideBattleTimeMessage;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.EBattleTipType;
import com.chen.command.Handler;
import com.chen.match.structs.EBattleModeType;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskGuideBattleTimeHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqAskGuideBattleTimeHandler.class);
	@Override
	public void action() 
	{
		try 
		{
			ReqAskGuideBattleTimeMessage message = (ReqAskGuideBattleTimeMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player == null)
				{
					log.error("没有Player");
					return;
				}				
				BattleContext battle = BattleManager.getInstance().getBattleContext(player);
				if (battle == null)
				{
					log.error("该角色还没有战斗对决");
					return;
				}
				if (battle.battleType != EBattleModeType.Game_Mode_Guide)
				{
					log.error("不是向导战斗");
					return;
				}
				battle.ChangeBattleTime(5*1000);
				battle.BoradTipsByType(EBattleTipType.eTips_CountDown);
			}
		} 
		catch (Exception e)
		{
			log.error(e);
		}		
	}
}
