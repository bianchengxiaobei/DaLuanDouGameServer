package com.chen.battle.skill.passiveSkill;

import com.chen.battle.skill.structs.NextSkillEffectConfig;

public class PassiveSkillConfig 
{
	public int passiveSkillId;
	public boolean bIfClearWhenDead;//死亡是否清理
	public int Cooldown;//0代表无冷却
	public EPassiveSkillTargetType passiveSkillTargetType;//作用的对象
	public int[] passiveSkillTriggerType;
	public NextSkillEffectConfig skillStartModelList[] = new NextSkillEffectConfig[3];//开始技能配置
	public NextSkillEffectConfig skillIntervalModelList[] = new NextSkillEffectConfig[3];//持续技能配置
	public NextSkillEffectConfig skillEndModelList[] = new NextSkillEffectConfig[3];//结束技能配置
	public int[] passiveEffectArray;
}
