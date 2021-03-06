package com.chen.battle.skill.handler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.skill.message.req.ReqAskUseSkillMessage;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSPlayer;
import com.chen.command.Handler;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqAskUseSkillHandler extends Handler
{
	
	private Logger log = LogManager.getLogger(ReqAskUseSkillHandler.class);
	@Override
	public void action() 
	{
		try 
		{
			ReqAskUseSkillMessage msg = (ReqAskUseSkillMessage)getMessage();
			Player player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
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
			SSPlayer player2 = battle.getUserBattleInfo(player).sPlayer;
			int size = msg.skillParams.length;
			if (size > 0)
			{
				int num = size / 3;
				for (int i=0;i<num;i++)
				{
					float x = msg.skillParams[0 + i * 3] * 0.001f;
					float y = msg.skillParams[1 + i * 3] * 0.001f;
					float z = msg.skillParams[2 + i * 3] * 0.001f;
					CVector3D param = new CVector3D(x, y, z);
					player2.sHero.curActionInfo.skillParams = param; 
				}
			}
			if (player2 != null)
			{
				BattleManager.getInstance().AskUseSkill(player2, msg.skillId);
			}else
			{
				log.error("不存在SSPlayer");
			}
			
		} catch (Exception e)
		{
			log.error("玩家释放技能失败");
		}		
	}

}
