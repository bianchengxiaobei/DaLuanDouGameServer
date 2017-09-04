package com.chen.battle.skill.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.crypto.Data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.SSSkillEffect;
import com.chen.battle.skill.SSSkillEffect_Emit;
import com.chen.battle.skill.SSSkillEffect_Move;
import com.chen.battle.skill.SSSkillEffect_Range;
import com.chen.battle.skill.config.SkillModelMoveConfig;
import com.chen.battle.skill.loader.SkillEffectTypeConfigXmlLoader;
import com.chen.battle.skill.structs.ESkillEffectType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSGameUnit;
import com.chen.data.manager.DataManager;

public class SSEffectManager
{
	public Logger logger = LogManager.getLogger(SSEffectManager.class);
	public Map<Integer, SSSkillEffect> waittingEffectMap = new HashMap<Integer, SSSkillEffect>();
	//public Map<Integer, SSSkillEffect> updatingEffectMap = new HashMap<Integer, SSSkillEffect>();
	public ConcurrentHashMap<Integer, SSSkillEffect> updatingEffectMap = new ConcurrentHashMap<Integer, SSSkillEffect>();
	

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
		SSSkillEffect effect = null;
		if (this.waittingEffectMap.containsKey(effectId))
		{
			effect = this.waittingEffectMap.remove(effectId);
			if (effect != null)
			{
				effect.ForceStop();
			}
			DestoryAFreeEffect(effect);
			return;
		}	
		if (this.updatingEffectMap.containsKey(effectId))
		{
			effect = this.updatingEffectMap.remove(effectId);
			if (effect != null)
			{
				effect.ForceStop();
			}
			DestoryAFreeEffect(effect);
			return;
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
		if (theOwner == null)
		{
			return ;
		}
		SSSkillEffect beDependedEffect = null;
		int dependEffectIdList[] = new int[16];
		int dependNum = 0;
		for (int i=0;i<16;i++)
		{
			NextSkillEffectConfig config = effectCfg[i];
			if (config == null)
			{
				continue;
			}
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
				return;
			}
		}
		effect.CheckCooldown();
	}
	public SSSkillEffect CreateSkillEffect(int effectId)
	{
		SkillEffectBaseConfig config = null;
		SSSkillEffect effect = null;
		ESkillEffectType effectType = DataManager.getInstance().skillTypeConfigLoader.skillEffectTypeMap.get(effectId);		
		switch (effectType) {
		case Move:
			config = DataManager.getInstance().skillModelMoveConfigLoader.skillModelMoveConfig.get(effectId);
			if (config == null)
			{
				break;
			}
			effect = new SSSkillEffect_Move();
			break;
		case Range:
			config = DataManager.getInstance().skillModelRangeConfigLoader.skillModelRangeConfig.get(effectId);
			if (config == null)
			{
				break;
			}
			effect = new SSSkillEffect_Range();
			break;
		case Emit:
			config = DataManager.getInstance().skillModelFlyConfigLoader.skillModelFlyConfig.get(effectId);
			if (config == null)
			{
				logger.error("FlyConfig == null");
				break;
			}
			effect = new SSSkillEffect_Emit();
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
			Iterator<Map.Entry<Integer, SSSkillEffect>> iterator = this.updatingEffectMap.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry<Integer, SSSkillEffect> entry = iterator.next();
				Integer id = entry.getKey();
				SSSkillEffect uEffect = entry.getValue();
				if (uEffect == null)
				{
					System.err.println("UEffect == null");
					iterator.remove();
					continue;
				}
				if (uEffect.IsInvalid())
				{
					uEffect.Clear();
					DestoryAFreeEffect(uEffect);
					iterator.remove();
					continue;
				}
				if (uEffect.Update(now, tick) == false)
				{
					uEffect.StopDependedEffect();
					uEffect.End();
					uEffect.Clear();
					DestoryAFreeEffect(uEffect);
					iterator.remove();
					continue;
				}
			}
//			for (SSSkillEffect uEffect : this.updatingEffectMap.values())
//			{
//				if (uEffect == null)
//				{
//					System.err.println("UEffect == null");
//					continue;
//				}
//				int id = uEffect.id;
//				if (uEffect.IsInvalid())
//				{
//					//uEffect.Clear();
//					//DestoryAFreeEffect(uEffect);
//					//this.updatingEffectMap.remove(id);
//					deleteEffectIds.add(id);
//					continue;
//				}
//				if (uEffect.Update(now, tick) == false)
//				{
//					uEffect.StopDependedEffect();
//					uEffect.End();
//					//uEffect.Clear();
//					//DestoryAFreeEffect(uEffect);
//					//this.updatingEffectMap.remove(id);
//					deleteEffectIds.add(id);
//					continue;
//				}
//			}
////			for (int id : deleteEffectIds)
////			{
////				if (updatingEffectMap.containsKey(id))
////				{
////					System.out.println("delete EffectId:"+id);
////					this.updatingEffectMap.remove(id);
////				}
////			}
		}
		
	}
}
