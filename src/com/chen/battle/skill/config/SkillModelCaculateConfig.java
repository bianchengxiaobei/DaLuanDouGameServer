package com.chen.battle.skill.config;

import com.chen.battle.skill.structs.BuffEffectInfo;
import com.chen.battle.skill.structs.EEffectCaculateType;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;
import com.chen.parameter.structs.EParameterCate;

public class SkillModelCaculateConfig extends SkillEffectBaseConfig
{
//	public EParameterCate eEffectCate;
//	public int EffectBaseValue;
//	public int EffectRate;
//	public EEffectCaculateType[] eEffectAddCacuType = new EEffectCaculateType[16];
//	public int[] eEffectAddCacuValue = new int[16];
//	public EEffectCaculateType[] eEffectMultCacuType = new EEffectCaculateType[16];
//	public int[] eEffectMultCacuValue = new int[16];
	public SkillModelCaculateConfig()
	{
		buffEffectInfo = new BuffEffectInfo();
	}
	public BuffEffectInfo buffEffectInfo;
}
