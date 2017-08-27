package com.chen.messagepool;

import java.util.HashMap;

import com.chen.battle.handler.ReqEnterSceneHandler;
import com.chen.battle.handler.ReqSelectHeroHandler;
import com.chen.battle.message.req.ReqEnterSceneMessage;
import com.chen.battle.message.req.ReqSelectHeroMessage;
import com.chen.battle.skill.handler.ReqAskUseSkillHandler;
import com.chen.battle.skill.message.req.ReqAskUseSkillMessage;
import com.chen.command.Handler;
import com.chen.login.handler.ReqCreateCharacterToGameServerHandler;
import com.chen.login.handler.ReqLoginCharacterToGameServerHandler;
import com.chen.login.message.req.ReqCreateCharacterToGameServerMessage;
import com.chen.login.message.req.ReqLoginCharacterToGameServerMessage;
import com.chen.match.handler.ReqAutoMatchHandler;
import com.chen.match.handler.ReqRemoveMatchHandler;
import com.chen.match.handler.ReqStartMatchHandler;
import com.chen.match.message.req.ReqAutoMatchMessage;
import com.chen.match.message.req.ReqRemoveMatchMessage;
import com.chen.match.message.req.ReqStartMatchMessage;
import com.chen.message.Message;
import com.chen.move.handler.ReqAskMoveHandler;
import com.chen.move.handler.ReqAskStopMoveHandler;
import com.chen.move.message.req.ReqAskMoveMessage;
import com.chen.move.message.req.ReqAskStopMoveMessage;
import com.chen.server.handler.ResRegisterGateHandler;
import com.chen.server.message.res.ResRegisterGateMessage;

public class MessagePool 
{
	HashMap<Integer, Class<?>> messages = new HashMap<Integer, Class<?>>();
	HashMap<Integer, Class<?>> handlers = new HashMap<Integer, Class<?>>();
	public MessagePool()
	{
		register(10001, ReqLoginCharacterToGameServerMessage.class, ReqLoginCharacterToGameServerHandler.class);
		register(10004, ResRegisterGateMessage.class, ResRegisterGateHandler.class);
		register(10007, ReqCreateCharacterToGameServerMessage.class, ReqCreateCharacterToGameServerHandler.class);
	
		register(1007, ReqAutoMatchMessage.class, ReqAutoMatchHandler.class);
		register(1008, ReqStartMatchMessage.class, ReqStartMatchHandler.class);
		register(1009, ReqRemoveMatchMessage.class, ReqRemoveMatchHandler.class);
		register(1015, ReqSelectHeroMessage.class, ReqSelectHeroHandler.class);
		register(1019, ReqEnterSceneMessage.class, ReqEnterSceneHandler.class);
		register(1021, ReqAskMoveMessage.class , ReqAskMoveHandler.class);
		register(1026, ReqAskStopMoveMessage.class , ReqAskStopMoveHandler.class);
		register(1030, ReqAskUseSkillMessage.class, ReqAskUseSkillHandler.class);
	}
	private void register(int id,Class<?> messageClass,Class<?> handlerClass)
	{
		messages.put(id, messageClass);
		if (handlerClass != null)
		{
			handlers.put(id, handlerClass);
		}
	}
	public Message getMessage(int id) throws InstantiationException, IllegalAccessException
	{
		if (messages.containsKey(id))
		{
			return (Message)messages.get(id).newInstance();
		}else{
			return null;
		}
	}
	public Handler getHandler(int id) throws InstantiationException, IllegalAccessException
	{
		if (handlers.containsKey(id))
		{
			return (Handler)handlers.get(id).newInstance();
		}else{
			return null;
		}
	}
}
