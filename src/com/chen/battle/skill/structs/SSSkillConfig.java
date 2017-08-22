package com.chen.battle.skill.structs;

public class SSSkillConfig
{
	public int skillId;
	public ESkillType eSkillType;
	public boolean bIsNormalAttack;
	public ESkillReleaseWay eReleaseWay;//释放朝向
	public int prepareTime;
	public int cooldownTime;
	public int releaseTime;
	public int lastTime;
	public float releaseDis;//释放距离
	public ESkillTriggerWay eTriggerWay;//技能触发方式
	public boolean bImpact;//是否是瞬发技能
	public ESkillTargetType eSkillTargetType;//技能释放所选择目标类型
	public NextSkillEffectConfig[] skillModelList = new NextSkillEffectConfig[16];
	
}
