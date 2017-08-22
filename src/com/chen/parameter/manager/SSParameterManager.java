package com.chen.parameter.manager;

import com.chen.battle.structs.SSGameUnit;
import com.chen.parameter.structs.EParameterCate;
import com.chen.parameter.structs.EParameterType;
import com.chen.parameter.structs.SSParameter;

public class SSParameterManager 
{
	public SSParameter[] paras = new SSParameter[EParameterCate.End.value];
	public SSGameUnit theOwner;
	
	public SSParameterManager()
	{
		for (int i=0; i<EParameterCate.End.value; i++)
		{
			paras[i] = new SSParameter();
		}
		paras[EParameterCate.CurHp.value].SetMinBaseValue(0);
		
		paras[EParameterCate.MoveSpeed.value].SetMinValue(1);
		paras[EParameterCate.MoveSpeed.value].SetMinBaseValue(1);
		paras[EParameterCate.MoveSpeed.value].SetMaxValue(1100);
		
		paras[EParameterCate.AttackSpeed.value].SetMinValue(1);
		paras[EParameterCate.AttackSpeed.value].SetMinBaseValue(1);
		paras[EParameterCate.AttackSpeed.value].SetMaxValue(5000);
		paras[EParameterCate.Dizziness.value].SetType(EParameterType.Status);
		paras[EParameterCate.Silence.value].SetType(EParameterType.Status);
		
		theOwner = null;
	}
	
	public void SetOwner(SSGameUnit theOwner)
	{
		this.theOwner = theOwner;
	}
}
