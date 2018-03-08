package com.chen.login.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.login.message.req.ReqGuideModuleCompMessage;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class ReqGuideModuleCompHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqGuideModuleCompHandler.class);
	@Override
	public void action()
	{
		try
		{
			//更新缓存和数据库
			ReqGuideModuleCompMessage message = (ReqGuideModuleCompMessage)this.getMessage();
			if (message != null)
			{
				Player player = PlayerManager.getInstance().getPlayer(message.getRoleId().get(0));
				if (player != null)
				{
					String guideStep = player.getFinishedGuideStep();
					guideStep += String.format(",%d", message.moduleId);
					player.setFinishedGuideStep(guideStep);
					//数据库,我觉得退出服务器的时候统一更新数据库比较好
					if (message.moduleId == 1003)
					{
						guideStep += ",ok";
						player.setFinishedGuideStep(guideStep);
					}
				}
			}
		} 
		catch (Exception e) 
		{
			log.error(e);
		}		
	}

}
