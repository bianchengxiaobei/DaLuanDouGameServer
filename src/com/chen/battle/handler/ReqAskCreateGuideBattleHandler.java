package com.chen.battle.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.impl.GuideBattle;
import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.req.ReqAskCreateGudieBattleMessage;
import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBattleType;
import com.chen.battle.structs.GuideBattleContext;
import com.chen.command.Handler;
import com.chen.match.structs.EBattleModeType;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskCreateGuideBattleHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqAskCreateGuideBattleHandler.class);
	@Override
	public void action() 
	{
		try
		{
			ReqAskCreateGudieBattleMessage message = (ReqAskCreateGudieBattleMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player == null)
				{
					log.error("没有Player");
					return;
				}
				player.getBattleInfo().changeTypeWithState(EBattleType.eBattleType_Guide1, EBattleState.eBattleState_Wait);
				long battleId = BattleManager.getInstance().generateBattleId();
//				GuideBattleContext guideBattleContext = new GuideBattleContext(EBattleModeType.Game_Mode_Guide, battleId, 0);
//				BattleManager.getInstance().allBattleMap.put(battleId, guideBattleContext);
				Map<Integer, Player> userList = new HashMap<>();
				userList.put(1, player);
				GuideBattle battle = new GuideBattle(EBattleModeType.Game_Mode_Guide, EBattleType.eBattleType_Guide1, battleId, 0, userList);
				BattleManager.getInstance().allBattleMap.put(battleId, battle);
				battle.start();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}	
	}

}
