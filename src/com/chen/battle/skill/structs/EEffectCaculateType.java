package com.chen.battle.skill.structs;

public enum EEffectCaculateType 
{
	None(0),
	
	SelfPhyAttack(1),
	SelfMagicAttack(2),
	SelfPhyDefence(3),
	SelfMagicDefence(4),
	SelfCurHp(5),
	SelfMaxHp(6),
	SelfLostHpPercent(7),
	SelfCupMp(8),
	SelfMaxMp(9),
	SelfLostMpPercent(10),
	SelfLevel(11),
	SelfMoveSpeed(12),
	
	TarPhyAttack(13),
	TarMagicAttack(14),
	TarPhyDefence(15),
	TarMagicDefence(16),
	TarCurHp(17),
	TarMaxHp(18),
	TarLostHpPercent(19),
	TarCupMp(20),
	TarMaxMp(21),
	TarLostMpPercent(22),
	TarLevel(23),
	TarMoveSpeed(24);
	
	public int value;
	private EEffectCaculateType(int value)
	{
		this.value = value;
	}
}
