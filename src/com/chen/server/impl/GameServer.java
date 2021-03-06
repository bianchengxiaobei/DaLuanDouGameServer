package com.chen.server.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.chen.battle.manager.BattleManager;
import com.chen.battle.structs.BattleContext;
import com.chen.cache.executor.NonOrderedQueuePoolExecutor;
import com.chen.cache.executor.OrderedQueuePoolExecutor;
import com.chen.cache.impl.MemoryCache;
import com.chen.cache.structs.AbstractWork;
import com.chen.command.Handler;
import com.chen.data.manager.DataManager;
import com.chen.match.manager.MatchManager;
import com.chen.message.Message;
import com.chen.messagepool.MessagePool;
import com.chen.mina.IServer;
import com.chen.mina.impl.ClientServer;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;
import com.chen.server.config.GameConfig;
import com.chen.server.loader.GameConfigXmlLoader;
import com.chen.server.message.req.ReqRegisterGateMessage;
import com.chen.server.thread.SaveMailThread;
import com.chen.server.thread.SchedularThread;
import com.chen.server.thread.ServerThread;

public class GameServer extends ClientServer
{
	private static Logger log = LogManager.getLogger(GameServer.class);
	private static Object obj = new Object();
	private static GameServer server;
	private static MessagePool messagePool = new MessagePool();
	private static GameConfig gameConfig;
	private static final String defaultGameConfig = "server-config/game-config.xml";
	private static final String defaultClientServerConfig = "server-config/client-server-config.xml";
	private static final String defaultPublicServerConfig = "server-config/public-server-config.xml";
	private OrderedQueuePoolExecutor decodeExecutor = new OrderedQueuePoolExecutor("网关消息解析队列",100,10000);
	private NonOrderedQueuePoolExecutor commandExecutor = new NonOrderedQueuePoolExecutor(100);
	private NonOrderedQueuePoolExecutor worldCommandExecutor = new NonOrderedQueuePoolExecutor(100);
	
	public static ConcurrentHashMap<String, Integer> delay = new ConcurrentHashMap<>();
//	private ThreadGroup thread_group;
//	private ServerThread wServerThread;
	public SaveMailThread wMailThread;
//	private SchedularThread wSchedularThread;
	private boolean connectSuccess = true;
	public GameServer(String serverConfig)
	{
		super(serverConfig);
	}
	public GameServer()
	{
		this(defaultClientServerConfig);
	}
	public static GameServer getInstance()
	{
		synchronized (obj) 
		{
			if (server == null)
			{
				server = new GameServer();
				gameConfig = new GameConfigXmlLoader().load(defaultGameConfig);
			}
		}
		return server;
	}
	@Override
	protected void init() 
	{
		super.init();		
		DataManager.getInstance().Init();
		wMailThread = new SaveMailThread("Save-Mail-Thread");
	}
	@Override
	public void run() 
	{
		super.run();
		//内网定时发送
		new Timer("Inner-Send-Timer").schedule(new TimerTask() {	
			@Override
			public void run() 
			{
				List<IoSession> sessions = new ArrayList<IoSession>();
				synchronized (gateSessions) 
				{
					Iterator<List<IoSession>> iter = gateSessions.values().iterator();
					while (iter.hasNext())
					{
						List<IoSession> list = iter.next();
						sessions.addAll(list);
					}
				}
				synchronized (centerSessions)
				{
					sessions.addAll(centerSessions);
				}
				for (IoSession ioSession : sessions)
				{
					IoBuffer sendBuf = null;
					synchronized (ioSession) 
					{
						if (ioSession.containsAttribute("send_buf"))
						{
							sendBuf = (IoBuffer)ioSession.getAttribute("send_buf");
							ioSession.removeAttribute("send_buf");
						}
					}
					try {
						if (sendBuf != null && sendBuf.position() > 0)
						{
							sendBuf.flip();
							WriteFuture wf = ioSession.write(sendBuf);
							wf.await();
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		},1,1);
		new Timer("Match Update").schedule(new TimerTask()
		{		
			@Override
			public void run() 
			{
				try 
				{
					MatchManager.getInstance().update();	
				} 
				catch (Exception e)
				{
					log.error(e);
				}	
			}
		},1000,1000);
	}
	@Override
	public void doCommand(IoSession session, IoBuffer buf) 
	{
		try 
		{
			int id = buf.getInt();//消息id
			//System.out.println("游戏服务器收到消息id"+id);
			long sessionId = buf.getLong();//客户端通信id号
			if (sessionId > 0)
			{
				decodeExecutor.addTask(sessionId, new Work(id, session, buf));
			}
			else
			{
				System.err.println("SessIonId<=0:"+sessionId+"消息Id:"+id);
			}
		} 
		catch (Exception e) 
		{
			log.error(e,e);
		}		
	}
	private class Work extends AbstractWork
	{
		private int id;
		private IoSession iosession;
		private IoBuffer buf;
		public Work(int id,IoSession iosession,IoBuffer buf)
		{
			this.id = id;
			this.iosession = iosession;
			this.buf = buf;
		}
		@Override
		public void run() 
		{
			try 
			{
				//获取消息体
				Message msg = messagePool.getMessage(id);
				if (null == msg)
				{
					log.error("收到了不存在的消息："+id);
					return;
				}
				int roleNum = buf.getInt();//用户拥有的角色数量
				for (int i=0;i<roleNum;i++)
				{
					msg.getRoleId().add(buf.getLong());//取得用户所有角色的id，设置到消息中
				}
				msg.read(buf.buf());
				msg.setSession(iosession);
				Handler handler = messagePool.getHandler(id);
				if (handler == null)
				{
					log.error("收到消息没有Handler");
				}
				handler.setMessage(msg);
				handler.setCreateTime(System.currentTimeMillis());
				if ("Local".equalsIgnoreCase(msg.getQueue()))
				{
					commandExecutor.execute(handler);
				}else if ("Server".equalsIgnoreCase(msg.getQueue()))
				{
					Player player = null;
					if (msg.getRoleId().size()>0)
					{
						player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
						if (player == null)
						{
							//服务器之间消息直接执行
							worldCommandExecutor.execute(handler);
							return;
						}
						handler.setParameter(player);
						worldCommandExecutor.execute(handler);
					}
				}
				else
				{
					Player player = null;
					if (msg.getRoleId().size()>0)
					{
						player = PlayerManager.getInstance().getPlayer(msg.getRoleId().get(0));
						if (player == null)
						{
							worldCommandExecutor.execute(handler);
							return;
						}
						handler.setParameter(player);
						worldCommandExecutor.execute(handler);
					}
				}
			}			
			catch (Exception e) 
			{
				log.error(e,e);
			}
		}
		
	}
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionClosed(IoSession session)
	{
		log.error(session+"关闭");
		//发生错误关闭连接
		int id = (Integer)session.getAttribute("connect-server-id");
		if (id != 0)
		{
			removeSession(session, id, GATE_SERVER);
		}
	}
	@Override
	public void sessionCreate(IoSession arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionOpened(IoSession arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void connectComplete() {
		connectSuccess = true;	
	}
	@Override
	public void register(IoSession session, int type)
	{
		switch (type) {
		case IServer.GATE_SERVER:
			System.out.println("开始注册网关服务器");
			ReqRegisterGateMessage msg = new ReqRegisterGateMessage();
			msg.setServerId(this.getServer_id());
			msg.setServerName(this.getServer_name());
			session.write(msg);
			break;
		default:
			break;
		}
	}
	@Override
	protected void stop()
	{
		BattleContext[] battleServer = BattleManager.getInstance().mServers.values().toArray(new BattleContext[0]);
		for (int i=0;i<battleServer.length;i++)
		{
			battleServer[i].Stop();
		}
		wMailThread.stop(true);
		try{
			Thread.sleep(10000);
		}catch (Exception e) {
			log.error(e, e);
		}
		//保存玩家数据
		MemoryCache<Long, Player> players = PlayerManager.getInstance().getPlayers();
		//事件迭代器
		Player[] saveplayers = players.getCache().values().toArray(new Player[0]);
		
		log.error("保存玩家开始，保存数量：" + saveplayers);
		int count = 0;
		//派发事件
		for (Player player : saveplayers) {
			//Player player = saves.get(i);
			count++;
			try{
				PlayerManager.getInstance().quit(player);
				PlayerManager.getInstance().UpdatePlayer(player);
			}catch(Exception ex){
				log.error(ex, ex);
			}
			if(count % 100 == 0) log.error("已经保存数量：" + count);
		}
		log.error("游戏服务器" + server_id + "停止成功！");
	}
}
