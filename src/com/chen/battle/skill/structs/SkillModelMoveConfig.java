package com.chen.battle.skill.structs;

public class SkillModelMoveConfig extends SkillEffectBaseConfig
{
	public ESkillEffectMoveType eMoveType;//移动类型,传送、相对、绝对
	public ESkillEffectMovedTargetType eMovedTargetType;//移动者类型,自己和他人
	public ESkillEffectMoveToTargetType eMoveToTargetType;//移动目标类型
	public int angle;
	public int distance;
	public int speed;
}
