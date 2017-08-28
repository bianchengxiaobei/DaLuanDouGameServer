package com.chen.battle.skill.config;

import com.chen.battle.skill.structs.ESkillAOEType;
import com.chen.battle.skill.structs.ESkillRangeShapeType;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;

public class SSkillModelRangleConfig extends SkillEffectBaseConfig
{
	public ESkillAOEType eSkillAOEType;
	public int rangeIntervalTime;
	public int rangleTimes;
	public ESkillRangeShapeType eSkillRangeShapeType;
	public int rangeParam1;
	public int rangeParam2;
	public int maxEffectObj;
	public int lifeTime;
	public int releaseDist;
}
