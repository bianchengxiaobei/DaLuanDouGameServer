package com.chen.battle.skill;

import com.chen.battle.skill.config.SkillModelCaculateConfig;
import com.chen.battle.skill.message.res.ResSkillHitTargetMessage;
import com.chen.battle.skill.structs.BuffEffectInfo;
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
		
		boolean ret = this.CaculateSkillEffectOnce(theOwner, target, caculateConfig.buffEffectInfo, skill == null ? 0 : skill.skillConfig.skillId, bIfNormalAttack, true);
		
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
	public static boolean CaculateSkillEffectOnce(SSGameUnit theOwner,SSGameUnit target,BuffEffectInfo skillInfo,int skillId,boolean bIfNormalAttack,boolean IfAdd)
	{
		if (target == null)
		{
			return false;
		}
		if (skillInfo == null)
		{
			logger.error("SkillInfo == null");
			return false;
		}
		if (!theOwner.battle.CheckObjCanBeHit(target, theOwner) && skillInfo.eParamType.value <= EParameterCate.CurHp.value)
		{
			return false;
		}
		float value = skillInfo.effectBaseValue;
		if (skillInfo.eEffectAddCaculType != null) 
		{
			for (int i = 0; i < skillInfo.eEffectAddCaculType.length; i++)
			{
				if (skillInfo.eEffectAddCaculType[i] == EEffectCaculateType.None) 
				{
					continue;
				}
				switch (skillInfo.eEffectAddCaculType[i]) 
				{
				case SelfPhyAttack:
					value += (float) theOwner.GetFPData(EParameterCate.PhyAttack) * skillInfo.effectAddCaculValue[i]
							* 0.01f;
					break;
				case SelfPhyDefence:
					value += (float) theOwner.GetFPData(EParameterCate.PhyDefense) * skillInfo.effectAddCaculValue[i]
							* 0.01f;
				default:
					break;
				}
			}
		}
		float multAdd = 0;
		if (skillInfo.eEffectMultCaculType != null)
		{
			for (int i = 0; i < skillInfo.eEffectMultCaculType.length; i++)
			{
				if (skillInfo.eEffectMultCaculType[i] == EEffectCaculateType.None) {
					continue;
				}
				switch (skillInfo.eEffectMultCaculType[i]) {
				case SelfPhyAttack:
					multAdd += (float) theOwner.GetFPData(EParameterCate.PhyAttack) * skillInfo.effectMultCaculValue[i]
							* 0.01f;
					break;
				case SelfPhyDefence:
					multAdd += (float) theOwner.GetFPData(EParameterCate.PhyDefense) * skillInfo.effectMultCaculValue[i]
							* 0.01f;
					break;
				default:
					break;
				}
			}
		}
		value = value * (1 + multAdd);
		boolean bIsCrit = false;
		switch (skillInfo.eParamType) 
		{
		case PhyHurt:
		case MagicHurt:
		case TrueHurt:
			int hurtValue = target.ApplyHurt(theOwner, (int)value, skillInfo.eParamType, bIfNormalAttack, bIsCrit);
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
		target.ChangeFPData(skillInfo.eParamType, skillInfo.effectBaseValue, skillInfo.effectRate, IfAdd);
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
