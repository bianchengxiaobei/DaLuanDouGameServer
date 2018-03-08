package com.chen.login.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.EBattleState;
import com.chen.command.Handler;
import com.chen.login.manager.LoginManager;
import com.chen.login.message.res.ResEnterLobbyMessage;
import com.chen.login.message.res.ResRegisterSuccessIfInBattleMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.utils.MessageUtil;

public class ResRegisterSuccessIfInBattleHandler extends Handler
{
	private Logger log = LogManager.getLogger(ResRegisterSuccessIfInBattleHandler.class);
	@Override
	public void action() 
	{
		try 
		{
			ResRegisterSuccessIfInBattleMessage message = (ResRegisterSuccessIfInBattleMessage)this.getMessage();
			Player player = PlayerManager.getInstance().getPlayer(message.playerId);
			if (player == null)
			{
				log.error("Player == null");
			}
			ResEnterLobbyMessage message2 = new ResEnterLobbyMessage();
			message2.roleAllInfo = LoginManager.getInstance().getRoleAllInfo(player);
			boolean isInBattle = player.getBattleInfo().getBattleState().value < EBattleState.eBattleState_Async.value ? false : true;
			message2.isInBattle = isInBattle;
			MessageUtil.tell_player_message(player, message2);
			player.PostGuideStep();
			//玩家已经进入战斗
			BattleContext battleContext = BattleManager.getInstance().getBattleContext(player);
			if (battleContext != null)
			{
				battleContext.OnEnterBattleState(player);
			}
		} catch (Exception e) 
		{
			log.error(e);
		}

	}
}
