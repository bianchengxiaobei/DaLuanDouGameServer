package com.chen.battle.structs;

import java.util.HashSet;
import java.util.Set;

import com.chen.player.structs.Player;

public class SSPlayer 
{
	public long battleId;
	public Player player;
	public SSHero sHero;
	public boolean bIfConnect = false;
	public Set<Integer> canUserHeroList = new HashSet<>();
	
	public SSPlayer(Player player)
	{
		this.player = player;
	}
	public void addCanUseHero(Integer heroId)
	{
		this.canUserHeroList.add(heroId);
	}
	
}
