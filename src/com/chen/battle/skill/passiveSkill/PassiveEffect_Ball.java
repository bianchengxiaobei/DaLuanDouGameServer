package com.chen.battle.skill.passiveSkill;

import com.chen.battle.structs.SSGameUnit;
import com.chen.parameter.structs.EParameterCate;

public class PassiveEffect_Ball extends PassiveEffect
{
	SSGameUnit theOwner;
	public PassiveEffect_Ball()
	{
		this.bIfKeeped = true;
	}
	@Override
	public void Trigger(SSGameUnit target,EPassiveSkillTriggerType type,int pa1,int pa2,int pa3)
	{
		if (theOwner == null)
		{
			theOwner = target;
		}
		target.ChangeFPData(EParameterCate.MoveSpeed, 0, -500, true);
	}
	@Override
	public void DisTrigger()
	{
		if (theOwner != null)
			theOwner.ChangeFPData(EParameterCate.MoveSpeed, 0, -500, false);
	}
	@Override
	public boolean IfLongTimeEffect()
	{
		return true;
	}
}
