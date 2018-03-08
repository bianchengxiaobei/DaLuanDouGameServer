package com.chen.battle.skill.passiveSkill;

public enum EPassiveSkillTriggerType 
{
	EPassiveSkillTriggerType_None(0),
	EPassiveSkillTriggerType_HeartBeat(1),
	EPassiveSkillTriggerType_Attack(2),
	EPassiveSkillTriggerType_Attacked(3),
	EPassiveSkillTriggerType_Hurted(4),
	EPassiveSkillTriggerType_Die(5),
	EPassiveSkillTriggerType_SkillAttack(6),
	EPassiveSkillTriggerType_UseSkill(7),
	EPassiveSkillTriggerType_TargetDie(8),
	EPassiveSkillTriggerType_NormalAttackCaculate_Before(9),
	EPassiveSkillTriggerType_NormalAttackCaculate_After(10),
	EPassiveSkillTriggerType_NormalAttackHurt(11),
	EPassiveSkillTriggerType_SkillHurt(12),
	EPassiveSkillTriggerType_Move(13),
	EPassiveSkillTriggerType_Ball(14),
	EPassiveSkillTriggerType_Max(15);
	public int value;
	private EPassiveSkillTriggerType(int value)
	{
		this.value = value;
	}
}
