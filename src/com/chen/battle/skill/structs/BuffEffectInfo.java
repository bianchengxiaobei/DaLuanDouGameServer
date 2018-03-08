package com.chen.battle.skill.structs;

import com.chen.parameter.structs.EParameterCate;

public class BuffEffectInfo
{
	public EParameterCate eParamType;
	public int effectBaseValue;//基础值，用于伤害和属性改变
	public int effectRate;//千分比该变量，只用于属性改变
	public EEffectCaculateType eEffectAddCaculType[];
	public int[] effectAddCaculValue;
	public EEffectCaculateType eEffectMultCaculType[];
	public int[] effectMultCaculValue;
	
	public BuffEffectInfo()
	{
//		for (int i=0;i<4;i++)
//		{
//			this.eEffectAddCaculType[i] = EEffectCaculateType.None;
//			this.eEffectMultCaculType[i] = EEffectCaculateType.None;
//		}		
	}
}
