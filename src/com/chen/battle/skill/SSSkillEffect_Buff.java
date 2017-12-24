package com.chen.battle.skill;

import com.chen.battle.skill.config.SkillModelBuffConfig;
import com.chen.battle.skill.structs.EBuffReplaceType;
import com.chen.battle.skill.structs.ESkillEffectType;
import com.chen.battle.skill.structs.ESkillModelTargetType;
import com.chen.parameter.structs.EParameterCate;

public class SSSkillEffect_Buff extends SSSkillEffect
{
	public SkillModelBuffConfig m_BuffConfig;
	private long beginDotTime;//buff开始时间
	private boolean bIfBuffAdded;//buff是否生效
	private int passitiveSkillId;
	private int repeatTimes;//堆叠层数
	
	public SSSkillEffect_Buff()
	{
		this.skillEffectType = ESkillEffectType.Buffer;
	}
	@Override
	public boolean Begin() 
	{
		this.m_BuffConfig = (SkillModelBuffConfig)this.config;
		if (this.m_BuffConfig == null)
		{
			return false;
		}
		if (this.m_BuffConfig.eTargetType == ESkillModelTargetType.eSMTC_Self)
		{
			this.target = theOwner;
		}
		if (target == null || target.IsDead())
		{
			return false;
		}
		if (target.CanAddBuff() == false)
		{
			return false;
		}
		if (CheckHitCampType(target) == false)
		{
			return false;
		}
		SSSkillEffect_Buff sameBuff = null;
		int sameBuffNum = 0;
		SSSkillEffect_Buff conflictBuff = null;
		SSSkillEffect_Buff replaceBuff = null;
		long curTime = System.currentTimeMillis();
		for (Integer id = target.bufferArray.Begin();id != target.bufferArray.End();id = target.bufferArray.Next())
		{
			SSSkillEffect_Buff buffEffect = (SSSkillEffect_Buff)target.battle.effectManager.GetEffect(id);
			if (buffEffect != null)
			{
				SkillModelBuffConfig tempConfig = (SkillModelBuffConfig)buffEffect.config;
				if (tempConfig == null)
				{
					target.bufferArray.RemoveElement(id);
					continue;
				}
				//相同的buff
				if (tempConfig.skillModelId == this.m_BuffConfig.skillModelId)
				{
					sameBuffNum++;
					sameBuff = buffEffect;
				}
				if (m_BuffConfig.rejectId != 0 && tempConfig.rejectId == m_BuffConfig.rejectId)
				{
					conflictBuff = buffEffect;
				}
				if (m_BuffConfig.replaceId != 0 && tempConfig.replaceId == m_BuffConfig.replaceId)
				{
					replaceBuff = buffEffect;
				}
			}
			else
			{
				logger.error("找不到buffEffect："+id);
			}
		}
		if (conflictBuff != null)
		{
			return false;
		}
		if (replaceBuff != null)
		{
			replaceBuff.isExpired = true;
		}
		//如果有相同的buff
		else if(sameBuff != null)
		{
			//如果不可堆叠，则返回
			if (m_BuffConfig.eBuffReplaceType == EBuffReplaceType.Forbit)
			{
				return false;
			}
			else if(m_BuffConfig.eBuffReplaceType == EBuffReplaceType.Reset)
			{
				//如果堆叠次数未满，则给原有buff添加效果
				if (sameBuff.repeatTimes < m_BuffConfig.replaceTimes)
				{
					sameBuff.AddRepeatEffect();
				}
				sameBuff.ResetTime();
				return false;
			}
			else if (m_BuffConfig.eBuffReplaceType == EBuffReplaceType.SingleCaculate)
			{
				if (m_BuffConfig.replaceTimes > 0 && sameBuffNum >= m_BuffConfig.replaceTimes)
				{
					return false;
				}
			}
		}
		beginDotTime = curTime - m_BuffConfig.effectInterval;
		beginTime = curTime;
		
		if (bIfBuffAdded == false)
		{
			this.AddBuffEffect(false);
			target.AddBuffEffect(id);
		}
		target.battle.effectManager.AddEffectsFromConfig(m_BuffConfig.skillStartModelList, theOwner,
				target, target.GetCurPos(), target.GetCurDir(), skill, curTime, null);
		return true;
	}

	@Override
	public boolean Update(long now, long tick) 
	{
		if (isExpired)
		{
			return false;
		}
		if (beginTime + m_BuffConfig.effectLastTick <= now)
		{
			return false;
		}
		//如果非英雄的NPC死亡了，他施放的buff要移除
		if (theOwner.IsDead() && theOwner.IfHero() == false)
		{
			return false;
		}
		//目标死亡，且设置为死亡后清除buff(大部分buff都是)，则清除
		if (target.IsDead() && m_BuffConfig.bIfClearWhenDead == true)
		{
			return false;
		}
		//每到了时间点，就调用一次dot效果
		if (m_BuffConfig.effectInterval > 0 && beginDotTime + m_BuffConfig.effectInterval <= now)
		{
			beginDotTime += m_BuffConfig.effectInterval;
			battle.effectManager.AddEffectsFromConfig(m_BuffConfig.skillIntervalModelList, theOwner, 
					target, target.GetCurPos(), target.GetCurDir(), skill, now, null);
		}
		return true;
	}

	@Override
	public void End() 
	{
		
		
	}
	
	public void AddRepeatEffect()
	{
		this.AddBuffEffect(true);
	}
	public void AddBuffEffect(boolean bRepeat)
	{
		bIfBuffAdded = true;
		if (m_BuffConfig.buffEffectInfo.eParamType != EParameterCate.None)
		{
			if (m_BuffConfig.buffEffectInfo.eParamType == EParameterCate.PassitiveSkill)
			{
				
			}
			else
			{
				SSSkillEffect_Caculate.CaculateSkillEffectOnce(theOwner, target, m_BuffConfig.buffEffectInfo, 0, false, true);
				this.repeatTimes++;
			}
		}
	}
	public void ResetTime()
	{
		this.beginTime = System.currentTimeMillis();
	}
	public void Clear()
	{
		this.End();
	}
}
