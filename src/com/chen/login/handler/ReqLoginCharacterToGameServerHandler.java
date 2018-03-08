package com.chen.login.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.EBattleState;
import com.chen.battle.structs.EBattleType;
import com.chen.command.Handler;
import com.chen.gm.manager.GMCommandManager;
import com.chen.login.bean.RoleAllInfo;
import com.chen.login.message.req.ReqLoginCharacterToGameServerMessage;
import com.chen.login.message.res.ResLoginSuccessToGateMessage;
import com.chen.mail.manager.MailServerManager;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.server.impl.GameServer;
import com.chen.utils.MessageUtil;

public class ReqLoginCharacterToGameServerHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqLoginCharacterToGameServerHandler.class);

	@Override
	public void action()
	{
		ReqLoginCharacterToGameServerMessage msg = (ReqLoginCharacterToGameServerMessage)getMessage();
		try {
			Player player = null;
			int serverId = GameServer.getInstance().getServer_id();
			player = PlayerManager.getInstance().getPlayer(msg.getPlayerId());
			if (player == null)
			{
				player = PlayerManager.getInstance().loadPlayer(msg.getPlayerId());
				if (player == null)
				{
					//选择角色失败
					log.error("载入角色失败");
					return;
				}
			}
			player.InitHero();
			player.InitFriend();
			player.collectionManager.Init();
			player.setGateId(msg.getGateId());
			if (msg.getLoginIp() == null || msg.getLoginIp().length() == 0)
			{
				log.error("错误IP");
			}
			player.setLoginIp(msg.getLoginIp());
			player.setLoginType(msg.getLoginType());
			player.setUserName(msg.getUserName());
			player.setServerName(GameServer.getInstance().getServer_name());
			player.setWebName(GameServer.getInstance().getServer_web());
			//加载邮件系统
			MailServerManager.getInstance().LoginLoadMail(player);
			PlayerManager.getInstance().registerPlayer(player);
			//加载GM等级
			GMCommandManager.getInstance().LoadGMLevel(player);
			boolean isInBattle = player.getBattleInfo().getBattleState().value < EBattleState.eBattleState_Async.value ? false : true;
			//通知网关服务器玩家角色登录成功
			ResLoginSuccessToGateMessage gate_msg = new ResLoginSuccessToGateMessage();
			gate_msg.setServerId(serverId);
			gate_msg.setCreateServerId(player.getCreateServerId());
			gate_msg.setUserId(msg.getUserId());
			gate_msg.setPlayerId(player.getId());
			MessageUtil.send_to_gate(msg.getGateId(), player.getId(), gate_msg);		
			if (!isInBattle)
			{
				if (player.getBattleInfo().getBattleTyoe() == EBattleType.eBattleType_Free)
				{
								
				}
			}
		} catch (Exception e) {
			log.error("服务器加载角色失败");
			log.error(e);
			//发送选择角色失败消息给网关		
		}		
	}
}
