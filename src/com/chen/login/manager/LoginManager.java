package com.chen.login.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.chen.login.bean.RoleAllInfo;
import com.chen.login.message.req.ReqCreateCharacterToGameServerMessage;
import com.chen.login.message.res.ResEnterLobbyMessage;
import com.chen.login.message.res.ResLoginSuccessToGateMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.server.impl.GameServer;
import com.chen.utils.MessageUtil;

public class LoginManager 
{
	private Logger log = LogManager.getLogger(LoginManager.class);
	private static LoginManager instance;
	private static Object obj = new Object();
	private LoginManager()
	{
		
	}
	public static LoginManager getInstance()
	{
		synchronized (obj) 
		{
			if (null == instance)
			{
				instance = new LoginManager();
			}
		}
		return instance;
	}
	/**
	 * 创建角色
	 * @param msg
	 */
	public void createCharacter(ReqCreateCharacterToGameServerMessage msg)
	{
		Player player = createCharacter(msg.getSession(),msg.getCreateServer(),msg.getName(),
				msg.getUserId(),msg.getUserName(),msg.getSex(),
				msg.getIcon(),msg.getLoginType(),msg.getOptIp(),msg.getGateId());
		if (player == null)
		{
			return;
		}
		player.InitHero();
		player.InitFriend();
		player.collectionManager.Init();
		int serverId = GameServer.getInstance().getServer_id();
		//通知网关服务器玩家登录成功
		ResLoginSuccessToGateMessage gate_msg = new ResLoginSuccessToGateMessage();
		gate_msg.setServerId(serverId);
		gate_msg.setCreateServerId(player.getCreateServerId());
		gate_msg.setUserId(msg.getUserId());
		gate_msg.setPlayerId(player.getId());
		MessageUtil.send_to_gate(msg.getGateId(),player.getId(), gate_msg);
	}	
	/**
	 * 创建角色
	 * @param session
	 * @param createServer
	 * @param name
	 * @param userId
	 * @param userName
	 * @param icon
	 * @param loginType
	 * @param optIp
	 * @param gateId
	 * @return
	 */
	private Player createCharacter(IoSession session,int createServer, 
			String name,String userId,String userName,byte sex,int icon,int loginType,String optIp,int gateId)
	{
		Player player = PlayerManager.getInstance().createPlayer(name, icon,sex);
		player.setUserId(userId);
		player.setUserName(userName);
		player.setServerName(GameServer.getInstance().getServer_name());
		player.setSex(sex);
		player.setWebName(GameServer.getInstance().getServer_web());
		player.setCreateServerId(createServer);
		player.setLoginType(loginType);
		player.setLoginIp(optIp);
		try {
			//保存玩家
			PlayerManager.getInstance().insertPlayer(player);
		} catch (Exception e) {
			log.error(e,e);
			//通知网关，数据库处理出错
			return null;
		}
		//注册玩家
		PlayerManager.getInstance().registerPlayer(player);
		player.setGateId(gateId);
		return player;
	}
	public RoleAllInfo getRoleAllInfo(Player player)
	{
		RoleAllInfo allinfo = new RoleAllInfo();
		allinfo.m_oBasicInfo = PlayerManager.getInstance().getPlayerBasicInfo(player);
		allinfo.m_oHeroInfo = PlayerManager.getInstance().GetPlayerHeroInfo(player);
		allinfo.m_oFriendInfo = PlayerManager.getInstance().GetPlayerFriendInfo(player);
		allinfo.m_oDailySignInfo = PlayerManager.getInstance().GetPlayerSignInfo(player);
		allinfo.m_oCollectionInfo = PlayerManager.getInstance().GetPlayerCollectionInfo(player);
		allinfo.m_oEmailInfo = PlayerManager.getInstance().GetPlayerEmailInfo(player);
		return allinfo;
	}
}
