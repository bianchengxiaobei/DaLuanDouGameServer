package com.chen.battle.skill;

import com.chen.battle.skill.config.SkillModelCaculateConfig;
import com.chen.battle.skill.message.res.ResSkillHitTargetMessage;
import com.chen.battle.skill.structs.EEffectCaculateType;
import com.chen.battle.skill.structs.ESkillEffectType;
import com.chen.battle.structs.SSGameUnit;
import com.chen.parameter.structs.EParameterCate;
import com.chen.utils.MessageUtil;

public class SSSkillEffect_Caculate extends SSSkillEffect
{
	public SSSkillEffect_Caculate() 
	{
		this.skillEffectType = ESkillEffectType.Caculate;
	}
	@Override
	public boolean Begin() 
	{
		if (theOwner == null && this.CheckBattleInvaid() == false || target == null)
		{
			return false;
		}
		if (this.CheckHitCampType(target) == false)
		{
			return false;
		}
		SkillModelCaculateConfig caculateConfig = (SkillModelCaculateConfig)this.config;
		boolean bIfNormalAttack = false;
		
		boolean ret = this.CaculateSkillEffectOnce(theOwner, target, caculateConfig, skill == null ? 0 : skill.skillConfig.skillId, bIfNormalAttack, true);
		
		this.NotifySkillModelHitObj();
		return false;
	}

	@Override
	public boolean Update(long now, long tick) 
	{	
		return false;
	}

	@Override
	public void End()
	{
			
	}
	private boolean CaculateSkillEffectOnce(SSGameUnit theOwner,SSGameUnit target,SkillModelCaculateConfig config,int skillId,boolean bIfNormalAttack,boolean IfAdd)
	{
		if (target == null)
		{
			return false;
		}
		if (config == null)
		{
			logger.error("CacuConfig == null");
			return false;
		}
		if (!this.battle.CheckObjCanBeHit(target, theOwner) && config.eEffectCate.value <= EParameterCate.CurHp.value)
		{
			return false;
		}
		float value = config.EffectBaseValue;
		for (int i=0; i<16; i++)
		{
			if (config.eEffectAddCacuType[i] == EEffectCaculateType.None)
			{
				continue;
			}
			switch (config.eEffectAddCacuType[i]) 
			{
			case SelfPhyAttack:
				value += (float)theOwner.GetFPData(EParameterCate.PhyAttack) * config.eEffectAddCacuValue[i] * 0.001f;
				break;
			case SelfPhyDefence:
				value += (float)theOwner.GetFPData(EParameterCate.PhyDefense) * config.eEffectAddCacuValue[i] * 0.001f;
			default:
				break;
			}
		}
		boolean bIsCrit = false;
		switch (config.eEffectCate) 
		{
		case PhyHurt:
		case MagicHurt:
		case TrueHurt:
			int hurtValue = target.ApplyHurt(theOwner, (int)value, config.eEffectCate, bIfNormalAttack, bIsCrit);
			if (hurtValue == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		if (target == null)
		{
			return false;
		}
		target.ChangeFPData(config.eEffectCate, config.EffectBaseValue, config.EffectRate, IfAdd);
		return true;
	}
	private void NotifySkillModelHitObj()
	{
		if (theOwner == null || this.CheckBattleInvaid() == false)
		{
			return ;
		}
		ResSkillHitTargetMessage message = new ResSkillHitTargetMessage();
		message.theOwner = theOwner.id;
		message.hitTarget = target.id;
		message.effectId = this.config.skillModelId;
		MessageUtil.tell_battlePlayer_message(battle, message);
	}
}
