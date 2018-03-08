package com.chen.battle.hero.config;

import com.chen.battle.structs.CVector3D;
import com.chen.parameter.structs.FightProperty;

public class SHeroConfig 
{
	public float colliderRadius;
	public CVector3D emitPos = new CVector3D();
	public int[] skillList = new int[7];
	public FightProperty baseFp = new FightProperty();
	public int costGold;
	public int costDimaond;
}
