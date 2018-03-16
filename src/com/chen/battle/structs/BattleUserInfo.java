package com.chen.battle.structs;

public class BattleUserInfo 
{
	public boolean bIsHeroChoosed;
	public boolean bIsLoadedComplete;
	public int selectedHeroId = -1;
	public long AIId;
	//public boolean bReconnect;
	public boolean bIfAI;//是否是机器人
	public SSPlayer sPlayer;
	public SSHero sHero;
	public long offlineTime;//如果掉线多少秒替换成机器人
	public EGameObjectCamp camp;//阵营
}
