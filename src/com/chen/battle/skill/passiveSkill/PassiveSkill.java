package com.chen.battle.skill.passiveSkill;

import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.SSGameUnit;

public class PassiveSkill 
{
	public PassiveEffect[] keepedEffects;
	public SSGameUnit theOwner;
	public PassiveSkillConfig config;
	public int id;
	public long lastEffectTime;
	public int triggerTimes;
	public boolean bIfExpired = false;
	public BattleContext battle;
	public PassiveSkill(PassiveSkillConfig config)
	{
		this.config = config;
		this.keepedEffects = new PassiveEffect[6];
	}
	public void Begin()
	{
		if (theOwner != null)
			theOwner.battle.effectManager.AddEffectsFromConfig(config.skillStartModelList, theOwner, theOwner, theOwner.GetCurPos(), theOwner.GetCurDir(), null, battle.battleHeartBeatTime, null);
	}
	public void OnHeartBeat(long now,long tick)
	{
		
	}
	public void Trigger(SSGameUnit target,int pa1,int pa2,int pa3)
	{
		if (theOwner.IsDead() && config.bIfClearWhenDead)
		{
			return ;
		}
		//如果有冷却时间的额话
		if (this.config.Cooldown > 0)
		{
			long now = battle.battleHeartBeatTime;
			if (now < this.lastEffectTime + config.Cooldown)
			{
				return;
			}
		}
		if (this.config.Cooldown > 0)
		{
			this.lastEffectTime = battle.battleHeartBeatTime;
		}
		SSGameUnit[] targetArray = new SSGameUnit[2];
		int targetSize = 0;
		switch (config.passiveSkillTargetType)
		{
		case EPassiveSkillTargetType_Self:
			targetArray[0] = theOwner;
			targetSize = 1;
			break;
		case EPassiveSkillTargetType_Trigger:
			targetArray[0] = target;
			targetSize = 1;
			break;
		case EPassiveSkillTargetType_Both:
			targetArray[0] = theOwner;
			targetArray[1] = target;
			targetSize = 2;
			break;
		}
		//如果触发客户端的被动技能显示
		for (int i=0;i<targetSize;i++)
		{
			theOwner.battle.effectManager.AddEffectsFromConfig(config.skillIntervalModelList, theOwner, targetArray[i], theOwner.GetCurPos(), theOwner.GetCurDir(), null, battle.battleHeartBeatTime, null);
		}
		if (this.config.passiveEffectArray != null)
		{
			int size = this.config.passiveEffectArray.length;
			if (size > 0)
			{
				for (int i=0;i<size;i++)
				{
					boolean isOldEffect = true;
					PassiveEffect effect = this.GetKeepedEffect(this.config.passiveEffectArray[i]);
					if (effect == null)
					{
						effect = CreatePassiveEffect(config.passiveEffectArray[i], theOwner);
						isOldEffect = false;
					}
					if (effect == null)
					{
						continue;
					}
					effect.Trigger(target,EPassiveSkillTriggerType.EPassiveSkillTriggerType_None,pa1,pa2,pa3);
					if (isOldEffect == false)
					{
						if (effect.bIfKeeped && effect.IfLongTimeEffect())
						{
							this.AddKeepedEffect(effect);
						}
						else
						{
							effect = null;
						}
					}
				}
			}
		}
		
	}
	public void DisTrigger()
	{
		if (this.keepedEffects != null)
		{
			int size = this.keepedEffects.length;
			if (size > 0)
			{
				for (int i=0; i<size; i++)
				{
					if (this.keepedEffects[i] != null)
					{
						this.keepedEffects[i].DisTrigger();
					}
				}
			}
		}
	}
	public PassiveEffect GetKeepedEffect(int passEffectId)
	{
		if (this.keepedEffects != null)
		{
			int size = this.keepedEffects.length;
			if (size > 0)
			{
				for (int i=0; i<size; i++)
				{
					if (this.keepedEffects[i] != null && this.keepedEffects[i].id == passEffectId)
					{
						return this.keepedEffects[i];
					}
				}
			}
			return null;
		}
		return null;
	}
	public void AddKeepedEffect(PassiveEffect effect)
	{
		for (int i=0;i<6;i++)
		{
			if (this.keepedEffects[i] == null)
			{
				this.keepedEffects[id] = effect;
				break;
			}
		}
	}
	public PassiveEffect CreatePassiveEffect(int effectTypeId,SSGameUnit theOwner)
	{
		EPassiveLongTimeSkillType type = EPassiveLongTimeSkillType.values()[effectTypeId];
		PassiveEffect effect = null;
		switch (type)
		{
		case BallPassive:
			PassiveEffect_Ball effect_Ball = new PassiveEffect_Ball();
			effect = effect_Ball;
			break;
		}
		if (effect != null)
		{
			effect.type = type;
			effect.id = theOwner.battle.GenerateEffectId();
			effect.theOwner = theOwner;
		}
		return effect;
	}
}
