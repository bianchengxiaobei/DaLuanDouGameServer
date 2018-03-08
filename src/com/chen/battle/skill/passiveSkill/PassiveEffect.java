package com.chen.battle.skill.passiveSkill;

import com.chen.battle.structs.SSGameUnit;

public class PassiveEffect 
{
	public int id;
	public SSGameUnit theOwner;
	public boolean bIfKeeped;
	public EPassiveLongTimeSkillType type;
	public void OnHeartBeat(long now,long tick)
	{
		
	}
	
	public void Trigger(SSGameUnit target,EPassiveSkillTriggerType type,int pa1,int pa2,int pa3)
	{
		
	}
	public void DisTrigger()
	{
		
	}
	public boolean IfLongTimeEffect()
	{
		return false;
	}
}
