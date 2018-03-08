package com.chen.battle.impl;

import java.util.Map;

import com.chen.battle.structs.EBattleType;
import com.chen.db.bean.Role;
import com.chen.match.structs.EBattleModeType;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class GuideBattle extends Battle
{

	public GuideBattle(EBattleModeType match_type, EBattleType type, long battleId, int mapId,
			Map<Integer, Player> userList) 
	{
		super(match_type, type, battleId, mapId, userList);	
	}
	/**
	 * 向导结算
	 */
	@Override
	public void CaculateResult(int win,Map<Long, Integer> result)
	{
		for (Player player : this.userMap.values())
		{
			if (player != null)
			{
				Role role = PlayerManager.getInstance().dao.selectById(player.getId());
				if (role != null)
				{
					role.setExp(550);
					//检测是否升级，通知客户端
					role.setFinishedGuideStep("1001");
					player.setFinishedGuideStep("1001");
					player.PostGuideStep();
				}
				PlayerManager.getInstance().dao.update(role);
			}
		}
		result = null;
	}
	@Override
	public void ReCreateMatch()
	{
		
	}
}
