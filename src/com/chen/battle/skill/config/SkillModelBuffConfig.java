package com.chen.battle.skill.config;

import com.chen.battle.skill.structs.BuffEffectInfo;
import com.chen.battle.skill.structs.EBuffReplaceType;
import com.chen.battle.skill.structs.ESkillBuffType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;
import com.chen.battle.skill.structs.SkillEffectBaseConfig;

public class SkillModelBuffConfig extends SkillEffectBaseConfig
{
	public ESkillBuffType eBuffType;
	public EBuffReplaceType eBuffReplaceType;//堆叠类型
	public int replaceTimes;//堆叠数量
	public int rejectId;//互斥id
	public int replaceId;//替换Id
	public boolean bIfClearWhenDead;//死亡是否清理
	public int effectLastTick;//持续时间
	public int effectInterval;//间隔时间
	public BuffEffectInfo buffEffectInfo;
	public NextSkillEffectConfig skillStartModelList[] = new NextSkillEffectConfig[3];//开始技能配置
	public NextSkillEffectConfig skillIntervalModelList[] = new NextSkillEffectConfig[3];//持续技能配置
	public NextSkillEffectConfig skillEndModelList[] = new NextSkillEffectConfig[3];//结束技能配置
}

