package com.chen.battle.skill.config;

import com.chen.battle.skill.structs.EEffectCaculateType;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;
import com.chen.parameter.structs.EParameterCate;

public class SkillModelCaculateConfig extends SkillEffectBaseConfig
{
	public EParameterCate eEffectCate;
	public int EffectBaseValue;
	public int EffectRate;
	public EEffectCaculateType[] eEffectAddCacuType = new EEffectCaculateType[16];
	public int[] eEffectAddCacuValue = new int[16];
	public EEffectCaculateType[] eEffectMultCacuType = new EEffectCaculateType[16];
	public int[] eEffectMultCacuValue = new int[16];
	public SkillModelCaculateConfig()
	{
		for (int i=0;i<16;i++)
		{
			this.eEffectAddCacuType[i] = EEffectCaculateType.None;
			this.eEffectMultCacuType[i] = EEffectCaculateType.None;
		}
	}
}
