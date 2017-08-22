package com.chen.battle.skill.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.SSSkillEffect;
import com.chen.battle.skill.loader.SkillEffectConfigXmlLoader;
import com.chen.battle.skill.structs.ESkillEffectType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;
import com.chen.battle.skill.structs.SSSkillEffect_Move;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;
import com.chen.battle.skill.structs.SkillModelMoveConfig;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSGameUnit;

public class SSEffectManager
{
	public Map<Integer, SSSkillEffect> waittingEffectMap = new HashMap<Integer, SSSkillEffect>();
	public Map<Integer, SSSkillEffect> updatingEffectMap = new HashMap<Integer, SSSkillEffect>();
	public Map<Integer, ESkillEffectType> skillTypeConfig = new HashMap<>();
	public Map<Integer, SkillModelMoveConfig> skillModelMoveConfig = new HashMap<>();
	public static final String skillTypeConfigPath = "server-config/skillTypeConfig.xml";
	public BattleContext battle;
	
	public SSSkillEffect GetEffect(int id)
	{
		if (id == 0)
		{
			return null;
		}
		if (this.waittingEffectMap.containsKey(id))
		{
			return this.waittingEffectMap.get(id);
		}
		if (this.updatingEffectMap.containsKey(id))
		{
			return this.updatingEffectMap.get(id);
		}
		return null;
	}
	public void RemoveEffect(int effectId)
	{
		//移除效果时，要注意需要将其强制停止
		SSSkillEffect effect = this.GetEffect(effectId);
		if (effect != null)
		{
			effect.ForceStop();
		}
		DestoryAFreeEffect(effect);
		if (this.waittingEffectMap.containsKey(effectId))
		{
			this.waittingEffectMap.remove(effectId);
		}
		if (this.updatingEffectMap.containsKey(effectId))
		{
			this.updatingEffectMap.remove(effectId);
		}
	}
	/**
	 * 添加特效到管理器中
	 * @param effectCfg
	 * @param theOwner
	 * @param target
	 * @param pos
	 * @param dir
	 * @param skill
	 * @param beginTime
	 * @param startSkillGo
	 */
	public void AddEffectsFromConfig(NextSkillEffectConfig[] effectCfg,SSGameUnit theOwner,SSGameUnit target,CVector3D pos,CVector3D dir,
			SSSkill skill,long beginTime,SSGameUnit startSkillGo)
	{
		if (effectCfg == null || theOwner == null)
		{
			return ;
		}
		SSSkillEffect beDependedEffect = null;
		int dependEffectIdList[] = new int[16];
		int dependNum = 0;
		for (int i=0;i<16;i++)
		{
			NextSkillEffectConfig config = effectCfg[i];
			if (config.skillEffectId == 0)
			{
				continue;
			}
			SSSkillEffect effect = this.CreateSkillEffect(config.skillEffectId);
			if (effect == null)
			{
				continue;
			}
			effect.battle = this.battle;
			effect.skill = skill;
			effect.theOwner = theOwner;
			effect.target = target;
			effect.startSkillGO = startSkillGo;
			effect.dir = dir;
			effect.pos = pos;
			effect.effectDelayTime = config.delay;
			effect.beginTime = beginTime;
			effect.IsForceStop = false;
			effect.id = battle.GenerateEffectId();
			Arrays.fill(effect.dependeEffect, null);
			//将依赖的效果ID加入到该效果的列表中。只存储ID
			if (config.dependedArr[0] != 0)				
			{
				dependEffectIdList = Arrays.copyOf(config.dependedArr, 16);
				beDependedEffect = effect;
				dependNum = 0;
			}
			if (beDependedEffect != null)
			{
				for (int j=0;j<16;j++)
				{
					if (dependEffectIdList[j] == config.skillEffectId)
					{
						beDependedEffect.dependeEffect[dependNum++] = effect;
					}
				}
			}
			this.AddEffect(effect);
		}
	}
	public void AddEffect(SSSkillEffect effect)
	{
		if (effect == null)
		{
			return;
		}
		if (effect.IsForceStop ==  true)
		{
			return;
		}
		if (effect.effectDelayTime > 0)
		{
			this.waittingEffectMap.put(effect.id, effect);
		}
		else
		{
			if (effect.Begin() == true)
			{
				effect.AddSelfToUsingList();
				this.updatingEffectMap.put(effect.id, effect);
			}
			else
			{
				effect.CheckCooldown();
				effect.End();
				DestoryAFreeEffect(effect);
			}
		}
		effect.CheckCooldown();
	}
	public SSSkillEffect CreateSkillEffect(int effectId)
	{
		SkillEffectBaseConfig config = null;
		SSSkillEffect effect = null;
		if (skillTypeConfig.isEmpty())
		{
			skillTypeConfig = new SkillEffectConfigXmlLoader().load(skillTypeConfigPath);
		}
		ESkillEffectType effectType = ESkillEffectType.None;
		if (skillTypeConfig.containsKey(effectId))
		{
			effectType = skillTypeConfig.get(effectId);
		}
		switch (effectType) {
		case Move:
			config = this.skillModelMoveConfig.get(effectId);
			if (config == null)
			{
				break;
			}
			effect = new SSSkillEffect_Move();
			break;

		default:
			return null;
		}
		if (effect == null)
		{
			return null;
		}
		effect.skillEffectType = effectType;
		effect.config = config;
		return effect;
	}
	public void DestoryAFreeEffect(SSSkillEffect effect)
	{
		if (effect != null)
		{
			effect = null;
		}
	}
	public void OnHeartBeat(long now,long tick)
	{
		if (this.waittingEffectMap.isEmpty() == false)
		{
			for (SSSkillEffect wEffect : this.waittingEffectMap.values())
			{
				if (wEffect == null)
				{
					continue;
				}
				int id = wEffect.id;
				//如果是无效的
				if (wEffect.IsInvalid())
				{
					wEffect.Clear();
					DestoryAFreeEffect(wEffect);
					this.waittingEffectMap.remove(id);
					continue;
				}
				if (wEffect.IsNeedWait(now))
				{
					continue;
				}
				if (wEffect.Begin() == true)
				{
					wEffect.AddSelfToUsingList();
					updatingEffectMap.put(wEffect.id, wEffect);
				}
				else
				{
					this.DestoryAFreeEffect(wEffect);
				}
				this.waittingEffectMap.remove(id);
			}
		}
		if (this.updatingEffectMap.isEmpty() == false)
		{
			for (SSSkillEffect uEffect : this.updatingEffectMap.values())
			{
				if (uEffect == null)
				{
					continue;
				}
				int id = uEffect.id;
				if (uEffect.IsInvalid())
				{
					uEffect.Clear();
					DestoryAFreeEffect(uEffect);
					this.updatingEffectMap.remove(id);
					continue;
				}
				if (uEffect.Update(now, tick) == false)
				{
					uEffect.StopDependedEffect();
					uEffect.End();
					uEffect.Clear();
					DestoryAFreeEffect(uEffect);
					this.updatingEffectMap.remove(id);
					continue;
				}
			}
		}
		
	}
}
