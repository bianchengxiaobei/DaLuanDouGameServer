package com.chen.battle.skill.structs;

public enum ESkillModelTargetType
{
	eSMTC_None(0),
	eSMTC_Self(1),//自己
	eSMTC_Enemy(2),//敌人
	eSMTC_AllObject(3);//所有单位
	public int value;
	private ESkillModelTargetType(int value) 
	{
		this.value = value;
	}
}
