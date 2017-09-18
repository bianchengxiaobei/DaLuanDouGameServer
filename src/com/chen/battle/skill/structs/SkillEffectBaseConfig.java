package com.chen.battle.skill.structs;

public class SkillEffectBaseConfig
{
	public int skillModelId;
	public ESkillEffectType eSkillEffectType;
	public ESkillModelTargetType eTargetType;
	public boolean bIsCooldown;
	public boolean bIsCanMove;
	public boolean bIsCanBreak;//是否能被打断
	public int releaseTimeDelay;
	public NextSkillEffectConfig skillModelList[] = new NextSkillEffectConfig[16];//下一步技能配置
	public SkillEffectBaseConfig()
	{
		bIsCanMove = true;
		bIsCanBreak = true;
	}

}
