package com.chen.battle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.req.ReqAskGuideStepCompMessage;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.EGuideStepType;
import com.chen.battle.structs.GuideBattleContext;
import com.chen.battle.structs.SSHero;
import com.chen.battle.structs.SSPlayer;
import com.chen.command.Handler;
import com.chen.match.structs.EBattleModeType;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskGuideStepCompHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqAskGuideStepCompHandler.class);
	@Override
	public void action() 
	{
		try
		{
			ReqAskGuideStepCompMessage message = (ReqAskGuideStepCompMessage)this.getMessage();	
			Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
			if (player == null)
			{
				log.error("玩家没有注册角色");
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
				log.error("模式不对："+battle.battleType.toString());
				return;
			}
			SSHero player2 = battle.getUserBattleInfo(player).sPlayer.sHero;
			int index = message.taskId - 7001;
			((GuideBattleContext)battle).StartGameMode(player2, EGuideStepType.values()[index]);
		} 
		catch (Exception e) 
		{
			log.error(e);
		}
			
	}
}
