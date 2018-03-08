package com.chen.battle.skill.config;

import com.chen.battle.skill.structs.BuffEffectInfo;
import com.chen.battle.skill.structs.EEffectCaculateType;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;
import com.chen.parameter.structs.EParameterCate;

public class SkillModelCaculateConfig extends SkillEffectBaseConfig
{
	public SkillModelCaculateConfig()
	{
		buffEffectInfo = new BuffEffectInfo();
	}
	public BuffEffectInfo buffEffectInfo;
}
