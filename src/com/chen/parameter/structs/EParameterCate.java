package com.chen.parameter.structs;

public enum EParameterCate
{
	None(0),
	PhyHurt(1),
	MagicHurt(2),
	CurHp(3),
	PhyAttack(4),
	MagicAttack(5),
	PhyDefense(6),
	MagicDefense(7),
	MoveSpeed(8),
	AttackSpeed(9),
	MaxHp(10),
	Dizziness(11),//昏迷
	Silence(12),//沉默
	AttackDist(13),//射程
	TrueHurt(14),
	CooldownReduce(15),//冷却缩减
	MaxMp(16),
	HpRecover(17),
	MpRecover(18),
	ReliveTime(19),
	CriPersent(20),
	CriHarm(21),
	PhyPass(22),
	PhyPassPercent(23),
	MagicPass(24),
	MagicPassPercent(25),
	PhyDmgReduce(26),//(相当于物理护盾)
	MagicDmgReduce(27),
	DmgReducePercent(28),
	Invisible(29),
	PassitiveSkill(30),
	End(31);
	public int value;
	EParameterCate(int value)
	{
		this.value = value;
	}
}
