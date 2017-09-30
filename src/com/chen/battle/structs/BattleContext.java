package com.chen.battle.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.ball.Ball;
import com.chen.battle.hero.config.SHeroConfig;
import com.chen.battle.manager.BattleManager;
import com.chen.battle.message.res.ResBattleTipMessage;
import com.chen.battle.message.res.ResEnterSceneMessage;
import com.chen.battle.message.res.ResGamePrepareMessage;
import com.chen.battle.message.res.ResHeroRebornMessage;
import com.chen.battle.message.res.ResReConnectMessage;
import com.chen.battle.message.res.ResSceneLoadedMessage;
import com.chen.battle.message.res.ResSelectHeroMessage;
import com.chen.battle.skill.manager.SSEffectManager;
import com.chen.data.manager.DataManager;
import com.chen.message.Message;
import com.chen.move.manager.SSMoveManager;
import com.chen.move.struct.ColVector;
import com.chen.move.struct.EAskStopMoveType;
import com.chen.player.structs.Player;
import com.chen.server.BattleServer;
import com.chen.server.config.BattleConfig;
import com.chen.utils.MessageUtil;

public class BattleContext extends BattleServer
{
	private Logger log = LogManager.getLogger(BattleContext.class);
	private EBattleType battleType;
	private EBattleServerState battleState = EBattleServerState.eSSBS_SelectHero;
	private long battleId;
	public int mapId;
	private long battleStateTime;
	public long battleHeartBeatTime;
	private long lastCheckPlayTimeout;
	private long battleFinishProtectTime = 0;
	private BattleUserInfo[] m_battleUserInfo = new BattleUserInfo[maxMemberCount];
	
	public Map<Long, SSGameUnit> gameObjectMap = new HashMap<>();
	public Set<EBattleTipType> tipSet = new HashSet<>();
	public SSMoveManager moveManager;
	public SSEffectManager effectManager;
	public Ball ball;
	public AtomicInteger effectId = new AtomicInteger(0);
	public static final int maxMemberCount = 6; 
	public static final int timeLimit = 200000;
	public static final int prepareTimeLimit = 5000;
	public static final int loadTimeLimit = 100000;
	public EBattleType getBattleType() {
		return battleType;
	}
	public void setBattleType(EBattleType battleType) {
		this.battleType = battleType;
	}
	public long getBattleId() {
		return battleId;
	}
	public void setBattleId(long battleId) {
		this.battleId = battleId;
	}
	public EBattleServerState getBattleState() {
		return battleState;
	}
	public BattleUserInfo[] getM_battleUserInfo() {
		return m_battleUserInfo;
	}
	public void setM_battleUserInfo(BattleUserInfo[] m_battleUserInfo) {
		this.m_battleUserInfo = m_battleUserInfo;
	}
	public BattleContext(EBattleType type, long battleId,int mapId)
	{
		super("战斗-"+battleId);
		this.battleId = battleId;
		this.mapId = mapId;
		this.battleType = type;
		this.moveManager = new SSMoveManager();
		this.effectManager = new SSEffectManager();
		this.ball = new Ball(this);
		this.effectManager.battle = this;
	}
	
	@Override
	protected void init() 
	{
         System.out.println("BattleContent:Init");
	}
	@Override
	public void run()
	{
		while (this.battleState != EBattleServerState.eSSBS_Finished)
		{
			this.battleHeartBeatTime = System.currentTimeMillis();
			this.OnHeartBeat(BattleContext.this.battleHeartBeatTime, 50);
			if (this.battleState == EBattleServerState.eSSBS_Finished)
			{
				log.debug("战斗结束");
				BattleManager.getInstance().allBattleMap.remove(this.battleId);
				BattleManager.getInstance().mServers.remove(this.battleId, this);				
			}
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void OnHeartBeat(long now,long tickSpan)
	{
		boolean res = CheckPlayTimeout(now);
		if (res)
		{
			//战斗结束直接返回
			return;
		}
		this.checkSelectHeroTimeout();
		this.checkPrepareTimeout();
		this.checkLoadingTimeout();
		this.DoPlayHeartBeat(now,tickSpan);
	}
	public void EnterBattleState(Player player)
	{
		boolean isReconnect = player.isReconnect();
		if (isReconnect)
		{
			//通知重新连接信息
		}
		log.info("玩家"+player.getId()+"确认加入战斗房间，当前战斗状态:"+battleState.toString());
		//以后再扩展开选择符文等
	}
	/**
	 * 玩家确认选择该英雄
	 * @param player
	 * @param heroId
	 */
	public void AskSelectHero(Player player,int heroId)
	{
		BattleUserInfo info = getUserBattleInfo(player);
		info.selectedHeroId = heroId;
		info.bIsHeroChoosed = true;
		ResSelectHeroMessage msg = new ResSelectHeroMessage();
		msg.playerId = player.getId();
		msg.heroId = heroId;
		MessageUtil.tell_battlePlayer_message(this,msg);
	}
	/**
	 * 玩家发送加载完成消息
	 */
	public void EnsurePlayerLoaded(Player player)
	{
		BattleUserInfo data = this.getUserBattleInfo(player);
		data.bIsLoadedComplete = true;
		ResSceneLoadedMessage msg = new ResSceneLoadedMessage();
		msg.m_playerId = player.getId();
		//说明是重新连接进入的
		if (this.battleState == EBattleServerState.eSSBS_Playing)
		{
			//更新客户端战斗时间
			//显示其他玩家，更新自己的状态
			//data.sHero.SendAppearMessage();
			for (BattleUserInfo other : this.m_battleUserInfo)
			{
				if (other != null)
				{
					other.sHero.SendToOhterAppearMessage(player);
				}
			}
			SyncState(data.sHero);
		}
		MessageUtil.tell_battlePlayer_message(this, msg);
	}
	public void checkSelectHeroTimeout()
	{
		if (this.battleState != EBattleServerState.eSSBS_SelectHero)
		{
			return ;
		}
		boolean ifAllUserSelect = true;
		for (int i=0; i<maxMemberCount; i++)
		{
			if (this.m_battleUserInfo[i] != null)
			{
				if (this.m_battleUserInfo[i].bIsHeroChoosed == false)
				{
					ifAllUserSelect = false;
					break;
				}
			}
		}
		//等待时间结束
		if (ifAllUserSelect || (System.currentTimeMillis() - this.battleStateTime) >= timeLimit)
		{
			for (int i = 0; i < maxMemberCount; i++) {
				if (this.m_battleUserInfo[i] != null)
				{
					if (false == this.m_battleUserInfo[i].bIsHeroChoosed) {
						//如果还没有选择神兽，就随机选择一个
						if (this.m_battleUserInfo[i].selectedHeroId == -1)
						{
							this.m_battleUserInfo[i].selectedHeroId = randomPickHero(this.m_battleUserInfo[i].sPlayer.canUserHeroList);
						}
						this.m_battleUserInfo[i].bIsHeroChoosed = true;
						//然后将选择该神兽的消息广播给其他玩家
						ResSelectHeroMessage msg = new ResSelectHeroMessage();
						msg.heroId = this.m_battleUserInfo[i].selectedHeroId;
						msg.playerId = this.m_battleUserInfo[i].sPlayer.player.getId();
						MessageUtil.tell_battlePlayer_message(this, msg);
					}
				}
			}
			//选择神兽阶段结束，改变状态，进入准备状态
			setBattleState(EBattleServerState.eSSBS_Prepare,true);
		}
	}
	public void checkLoadingTimeout()
	{
		if (this.battleState != EBattleServerState.eSSBS_Loading)
		{
			return ;
		}
		boolean bIfAllPlayerConnect = true;
		//时间未到，则检查是否所有玩家已经连接
		if (System.currentTimeMillis() - this.battleStateTime < loadTimeLimit)
		{
			for (int i=0;i<this.m_battleUserInfo.length;i++)
			{
				if (this.m_battleUserInfo[i] != null)
				{
					if (this.m_battleUserInfo[i].bIsLoadedComplete == false)
					{
						bIfAllPlayerConnect = false;
						break;
					}
				}
			}
		}
		if (bIfAllPlayerConnect == false)
		{
			return;
		}
		//加载静态的配置文件
		this.LoadMapConfigNpc();
		//然后创建神兽
		for (int i=0;i<this.m_battleUserInfo.length;i++)
		{
			if (this.m_battleUserInfo[i] == null)
			{
				continue;
			}
			SSPlayer user = this.m_battleUserInfo[i].sPlayer;
			CVector3D bornPos = new CVector3D(0, 0, 0);//这里需要通过配置文件加载
			CVector3D dir = new CVector3D(0,0,0);
			SSHero hero = null;
			hero = AddHero(user.player.getId(), bornPos, dir, user, this.m_battleUserInfo[i].selectedHeroId,this.m_battleUserInfo[i].camp);
			this.m_battleUserInfo[i].sHero = hero;
			//通知玩家游戏开始战斗倒计时
			BoradTipsByType(EBattleTipType.eTips_ObjAppear, this.m_battleUserInfo[i].sPlayer.player);
		}
		
		this.PostStartGameMsg();
		this.setBattleState(EBattleServerState.eSSBS_Playing,false);
	}
	public boolean CheckPlayTimeout(long now)
	{
		if (this.lastCheckPlayTimeout == 0)
		{
			this.lastCheckPlayTimeout = now;
			return false;
		}
		//每隔5秒检测一次
		if (now - this.lastCheckPlayTimeout < 5000)
		{
			return false;
		}
		this.lastCheckPlayTimeout = now;
		boolean bAllUserOffline = true;
		for (int i=0;i<maxMemberCount;i++)
		{
			if (this.m_battleUserInfo[i] != null)
			{
				SSPlayer player = this.m_battleUserInfo[i].sPlayer;
				//如果有一个人连上去的话，就没有所有人断线
				if (player != null && player.bIfConnect == true)
				{
					bAllUserOffline = false;
					break;
				}
			}		
		}
		//如果玩家在线的话，战斗保护时间重置
		if (bAllUserOffline == false)
		{
			this.battleFinishProtectTime = 0;
		}
		if (bAllUserOffline && this.battleFinishProtectTime == 0)
		{
			this.battleFinishProtectTime = now + 5000;
		}
		if (bAllUserOffline && now > this.battleFinishProtectTime)
		{
			log.debug("所有玩家离线，战斗结束");
			Finish();
			return true;
		}
		return false;
	}
	public void checkPrepareTimeout()
	{
		if (this.battleState != EBattleServerState.eSSBS_Prepare)
		{
			return ;
		}
		if (System.currentTimeMillis() - this.battleStateTime > prepareTimeLimit)
		{
			this.setBattleState(EBattleServerState.eSSBS_Loading, true);
		}
	}
	public void DoPlayHeartBeat(long now,long tick)
	{
		if (this.battleState != EBattleServerState.eSSBS_Playing)
		{
			return;
		}
		this.moveManager.OnHeartBeat();
		for (SSGameUnit unit : gameObjectMap.values())
		{
			if (unit != null)
			{
				unit.OnHeartBeat(now, tick);
			}
		}		
		this.effectManager.OnHeartBeat(now, tick);
		this.ball.OnHeartBeat(now, tick);
	}
	public void BoradTipsByType(EBattleTipType type,Player player)
	{
		boolean flag = false;
		if (!this.tipSet.contains(type))
		{
			this.tipSet.add(type);
			flag = true;
		}
		if (flag)
		{
			ResBattleTipMessage message = new ResBattleTipMessage();
			switch (type) {
			case eTips_ObjAppear:
				message.tipCode = 100;
				break;
			case eTips_Gas:
				message.tipCode = 101;
				break;
			default:
				break;
			}
			MessageUtil.tell_player_message(player, message);
		}	
	}
	/**
	 * 改变游戏状态
	 * @param state
	 * @param isSendToClient
	 */
	public void setBattleState(EBattleServerState state,boolean isSendToClient)
	{
		this.battleState = state;
		this.battleStateTime = System.currentTimeMillis();
		if (isSendToClient)
		{
			switch (state) {
			case eSSBS_Prepare:
				//通知客户端开始进入准备状态
				ResGamePrepareMessage pre_msg = new ResGamePrepareMessage();
				pre_msg.setTimeLimit(prepareTimeLimit);
				MessageUtil.tell_battlePlayer_message(this, pre_msg);
				break;
			case eSSBS_Loading:
				//通知客户端开始加载场景
				ResEnterSceneMessage scene_msg = new ResEnterSceneMessage();
				MessageUtil.tell_battlePlayer_message(this, scene_msg);
				break;			
			default:
				break;
			}
		}
	}
	/**
	 * 请求开始移动
	 * @param player
	 * @param _dir
	 * @return
	 */
	public boolean AskMoveDir(SSGameUnit player,CVector3D _dir)
	{
		ColVector dir = new ColVector(_dir.x, _dir.y,_dir.z);
		return moveManager.AskStartMoveDir(player, dir);
	}
	/**
	 * 重置坐标
	 * @param player
	 * @param pos
	 * @param dir
	 * @param bIfImpact
	 * @return
	 */
	public boolean ResetPos(SSGameUnit player,CVector3D pos,boolean bIfImpact)
	{
		ColVector cPos = new ColVector(pos.x,pos.y,pos.z);		
		boolean r = this.moveManager.ResetPos(player, cPos, bIfImpact);
		if (r)
		{
			this.SyncState(player);
		}
		return r;
	}
	/**
	 * 请求停止移动
	 * @param player
	 * @return
	 */
	public boolean AskStopMoveDir(SSGameUnit player)
	{
		return moveManager.AskStopMoveObject(player, EAskStopMoveType.Dir);
	}
	public boolean AskStopMoveObjectAll(SSGameUnit player)
	{
		return moveManager.AskStopMoveObject(player, EAskStopMoveType.All);
	}
	public boolean AskStopMoveTarget(SSGameUnit player)
	{
		return moveManager.AskStopMoveObject(player, EAskStopMoveType.Target);
	}
	/**
	 * 请求开始强制移动
	 * @param player
	 * @param dir
	 * @param speed
	 * @param bIfImpact
	 * @return
	 */
	public boolean AskStartMoveForced(SSGameUnit player, CVector3D dir, float speed, boolean bIfImpact)
	{
		return moveManager.AskMoveForced(player, new ColVector(dir.x, dir.y, dir.z), speed, bIfImpact);
	}
	public boolean AskStopMoveObjectForceMove(SSGameUnit player)
	{
		return moveManager.AskStopMoveObject(player, EAskStopMoveType.ForceMove);
	}
	
	/**
	 * 加载地图配置
	 */
	public void LoadMapConfigNpc()
	{
//		map = new SSMap();
//		map.Init(0, "server-config/map-config.xml");
	}	
	/**
	 * 取得场景中独立无二的特效Id
	 * @return
	 */
	public int GenerateEffectId()
	{
		return effectId.incrementAndGet();
	}
	public SSHero AddHero(Long playerId,CVector3D pos,CVector3D dir,SSPlayer user,int heroId,EGameObjectCamp _camp)
	{
		//取得英雄配置表加载基础数据
		SHeroConfig heroConfig = DataManager.getInstance().heroConfigXMLLoader.heroConfigMap.get(heroId);
		if (heroConfig == null)
		{
			log.error("找不到英雄："+heroId);
			return null;
		}
		
		SSHero hero = new SSHero(playerId,this,_camp);
		hero.LoadHeroConfig(heroConfig);
		//hero.LOadHeroConfig
		user.sHero = hero;
		hero.player = user;
		hero.BeginActionIdle(false);
		hero.bornPos = pos;
		hero.ResetAI();
		this.EnterBattle(hero, pos, dir);
		//加载被动技能
		return hero;
	}
	public void EnterBattle(SSGameUnit go,CVector3D pos, CVector3D dir)
	{
		SSGameUnit unit = gameObjectMap.get(go.id);
		if (unit != null)
		{
			log.debug("游戏场景中已经存在该物体");
			return;
		}
		go.curActionInfo.pos = pos;
		//go.expire = false;
		gameObjectMap.put(go.id, go);
		go.curActionInfo.dir = dir;
		go.enterBattleTime = System.currentTimeMillis();
		if (go.IfCanImpact())
		{
			this.AddMoveObject(go);
		}
	}
	/**
	 * 战斗结束
	 */
	public void Finish()
	{
		if (this.battleState == EBattleServerState.eSSBS_Finished)
		{
			return;
		}
		for (int i=0; i<this.m_battleUserInfo.length; i++)
		{
			if (this.m_battleUserInfo[i] == null)
			{
				continue;
			}
			this.m_battleUserInfo[i].sPlayer.player.getBattleInfo().reset();
		}
		//通知客户端战斗结束
		setBattleState(EBattleServerState.eSSBS_Finished,true);
		//通知客户端那方赢了
		
	}
	public void AddMoveObject(SSMoveObject obj)
	{
		this.moveManager.AddMoveObject(obj);
	}
	public void SyncState(SSGameUnit obj)
	{
		if (obj == null)
		{
			log.error("不存在英雄");
			return;
		}
		Message message = obj.ConstructObjStateMessage();
		MessageUtil.tell_battlePlayer_message(this, message);
	}
	public void AskRebornGameHero(SSHero obj)
	{
		Player player =  obj.player.player;
		if (player != null)
		{
			ResHeroRebornMessage message = new ResHeroRebornMessage();
			MessageUtil.tell_player_message(player, message);
		}
		obj.BeginActionIdle(false);
		//重新设置到出生点
		ResetPos(obj, obj.bornPos, false);
		obj.FullHp();
		SyncState(obj);
	}
	/**
	 * 取得随机神兽
	 * @param pickHeroList
	 * @param camType
	 * @return
	 */
	private int randomPickHero(Set<Integer> pickHeroList)
	{
		List<Integer> canChooseList = new ArrayList<Integer>();
		if (pickHeroList == null || pickHeroList.size() == 0)
		{
			System.out.println("没有英雄可以选择");
		}
		for (int heroId : pickHeroList) 
		{
			canChooseList.add(heroId);
		}
		return canChooseList.get((int) (Math.random()*canChooseList.size()));		
	}
	/**
	 * 根据玩家取得玩家数据
	 * @param player
	 * @return
	 */
	public BattleUserInfo getUserBattleInfo(Player player)
	{
		if (player == null)
		{
			return null;
		}
		for (int i=0; i<this.m_battleUserInfo.length; i++)
		{
			if (this.m_battleUserInfo[i] == null)
			{
				continue;
			}
			if (this.m_battleUserInfo[i].sPlayer.player.getId() == player.getId())
			{
				return this.m_battleUserInfo[i];
			}
		}
		return null;
	}
	public SSGameUnit GetGameObjectById(long id)
	{
		if (id <= 0)
		{
			return null;
		}
		if (this.gameObjectMap.containsKey(id))
		{
			return this.gameObjectMap.get(id);
		}
		return null;
	}
	/**
	 * 找到周围物体存入set集合中
	 * @param startPos
	 * @param radius
	 * @param objs
	 */
	public void FindAroundGo(CVector3D startPos,float radius,Set<SSGameUnit> objs)
	{
		float radiusSqrl = radius * radius;
		for (SSGameUnit obj : this.gameObjectMap.values())
		{
			if(obj != null)
			{
				CVector3D pos = obj.GetCurPos();
				if (Math.abs(pos.x - startPos.x) > radius || Math.abs(pos.z - startPos.z) > radius)
				{
					continue;
				}
				if ((pos.x - startPos.x)*(pos.x-startPos.x) + (pos.z - startPos.z)*(pos.z - startPos.z) < radiusSqrl)
				{
					objs.add(obj);
				}
			}
		}
	}
	/**
	 * 找到周围第一个物体
	 * @param startPos
	 * @param radius
	 * @param objs
	 */
	public SSGameUnit FindAroundGo(CVector3D startPos,float radius)
	{
		float radiusSqrl = radius * radius;
		for (SSGameUnit obj : this.gameObjectMap.values())
		{
			if(obj != null && obj.IsDead() == false)
			{
				CVector3D pos = obj.GetCurPos();
				if (Math.abs(pos.x - startPos.x) > radius || Math.abs(pos.z - startPos.z) > radius)
				{
					continue;
				}
				if ((pos.x - startPos.x)*(pos.x-startPos.x) + (pos.z - startPos.z)*(pos.z - startPos.z) < radiusSqrl)
				{
					return obj;
				}
			}
		}
		return null;
	}
	/**
	 * 是否能被攻击
	 * @param beHitter
	 * @param attacker
	 * @return
	 */
	public boolean CheckObjCanBeHit(SSGameUnit beHitter,SSGameUnit attacker)
	{
		return true;
	}
	/**
	 * 玩家离线
	 * @param player
	 */
	public void OnUserOffline(Player player)
	{
		BattleUserInfo info = this.getUserBattleInfo(player);
		if (info != null)
		{
			SSHero hero = info.sHero;
			if (hero != null)
			{
				//移除消息通信
				info.sPlayer.bIfConnect = false;
				info.bIsLoadedComplete = false;
				info.bReconnect = true;
				if (battleState == EBattleServerState.eSSBS_Playing)
				{
					info.offlineTime = System.currentTimeMillis();
				}
			}
		}
	}
	/**
	 * 玩家重新连接战斗
	 * @param player
	 */
	public void OnEnterBattleState(Player player)
	{
		BattleUserInfo info = this.getUserBattleInfo(player);
		if (info != null)
		{
			info.sPlayer.bIfConnect = true;
			//需要重新连接
			if (info.bReconnect == true)
			{
				//发送给客户端重新连接的消息
				ResReConnectMessage message = new ResReConnectMessage();
				message.battleState = this.battleState.getValue();
				message.battleId = this.battleId;
				message.mapId = this.mapId;
				message.playerId = player.getId();
				for (int i=0;i<this.m_battleUserInfo.length;i++)
				{
					if (this.m_battleUserInfo[i] == null)
					{
						continue;
					}
					ReConnectInfo info2 = new ReConnectInfo();
					info2.playerId = this.m_battleUserInfo[i].sPlayer.player.getId();
					info2.heroId = this.m_battleUserInfo[i].selectedHeroId;
					info2.nickName = this.m_battleUserInfo[i].sPlayer.player.getName();
					message.ReConnectInfo.add(info2);
				}
				MessageUtil.tell_player_message(player, message);
			}
			switch (battleState) {
			case eSSBS_SelectHero:
				//选定的英雄重新发送
				for (int i=0;i<this.m_battleUserInfo.length;i++)
				{
					if (this.m_battleUserInfo[i] == null || this.m_battleUserInfo[i].selectedHeroId == 0)
					{
						continue;
					}
					if (this.m_battleUserInfo[i].bIsHeroChoosed)
					{
						//然后将选择该神兽的消息广播给其他玩家
						ResSelectHeroMessage msg = new ResSelectHeroMessage();
						msg.heroId = this.m_battleUserInfo[i].selectedHeroId;
						msg.playerId = this.m_battleUserInfo[i].sPlayer.player.getId();
						MessageUtil.tell_player_message(player, msg);
					}
				}
				break;
			case eSSBS_Prepare:				
				break;
			case eSSBS_Loading:
				break;
			case eSSBS_Playing:
				SSHero hero = info.sHero;
				info.offlineTime = 0;
				if (hero != null)
				{
					hero.ResetAI();
				}
				break;
			default:
				break;
			}
		}
	}
	private void PostStartGameMsg()
	{
		for (int i=0; i<this.m_battleUserInfo.length; i++)
		{
			if (this.m_battleUserInfo[i] == null)
			{
				continue;
			}	
			if (this.m_battleUserInfo[i].sPlayer != null)
			{
				this.m_battleUserInfo[i].sHero.SendAppearMessage();
			}
			//发送每个玩家的英雄信息
			//然后通知客户端加载模型
		}
		ball.Start();
	}
	
}
