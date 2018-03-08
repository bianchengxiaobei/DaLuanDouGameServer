package com.chen.parameter.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.message.res.ResChangeHpMessage;
import com.chen.battle.structs.SSGameUnit;
import com.chen.parameter.structs.EParameterCate;
import com.chen.parameter.structs.EParameterType;
import com.chen.parameter.structs.FightProperty;
import com.chen.parameter.structs.SSParameter;
import com.chen.utils.MessageUtil;

public class SSParameterManager 
{
	public Logger logger = LogManager.getLogger(SSParameterManager.class);
	public SSParameter[] paras = new SSParameter[EParameterCate.End.value];
	public SSGameUnit theOwner;
	
	public SSParameterManager()
	{
		for (int i=0; i<EParameterCate.End.value; i++)
		{
			paras[i] = new SSParameter();
		}
		paras[EParameterCate.CurHp.value].SetMinBaseValue(0);
		
		paras[EParameterCate.MoveSpeed.value].SetIfOnlyOneMinusePercent(true);
		paras[EParameterCate.MoveSpeed.value].SetMinValue(1);
		paras[EParameterCate.MoveSpeed.value].SetMinBaseValue(1);
		paras[EParameterCate.MoveSpeed.value].SetMaxValue(1100);
		
		paras[EParameterCate.AttackSpeed.value].SetIfOnlyOneMinusePercent(true);
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
	public int GetValue(int type)
	{
		if (type < 0 || type >= EParameterCate.End.value)
		{
			return 0;
		}
		SSParameter parameter = paras[type];
		if (parameter != null)
		{
			return parameter.GetValue();
		}
		else
		{
			logger.error("找不带参数数据："+type);
			return 0;
		}
	}
	public void AddBaseGroup(FightProperty fp,boolean isBaseValue)
	{
		this.AddBaseValue(EParameterCate.PhyAttack.value, fp.phyAttack);;
		this.AddBaseValue(EParameterCate.MagicAttack.value, fp.magicAttack);
		this.AddBaseValue(EParameterCate.PhyDefense.value, fp.phyDefence);
		this.AddBaseValue(EParameterCate.MagicDefense.value, fp.magicDefence);
		this.AddBaseValue(EParameterCate.MoveSpeed.value, fp.moveSpeed);
		this.AddBaseValue(EParameterCate.MaxHp.value, fp.maxHp);
		this.AddBaseValue(EParameterCate.MaxMp.value,fp.maxMp);
		this.AddBaseValue(EParameterCate.HpRecover.value, fp.hpRecover);
		this.AddBaseValue(EParameterCate.MpRecover.value, fp.mpRecover);
		this.AddBaseValue(EParameterCate.ReliveTime.value, fp.reliveSecond);
		this.AddBaseValue(EParameterCate.CriPersent.value, fp.criPersend);
		this.AddBaseValue(EParameterCate.CriHarm.value, fp.criHarm);
		this.AddBaseValue(EParameterCate.AttackDist.value, fp.attackDist);
		this.AddBaseValue(EParameterCate.Dizziness.value, fp.dizzinessCounter);
		this.AddBaseValue(EParameterCate.Silence.value, fp.silenceCounter);
		this.AddBaseValue(EParameterCate.Invisible.value, fp.invisibleCounter);
		if (isBaseValue)
		{
			this.AddBaseValue(EParameterCate.AttackSpeed.value, fp.attackSpeed);;
		}
		else
		{
			this.AddPercentValue(EParameterCate.AttackSpeed.value, fp.attackSpeed);;
		}
	}
	public void AddBaseValue(int type,int value)
	{
		if (type < 0 || type >= EParameterCate.End.value)
		{
			return;
		}
		SSParameter parameter = paras[type];
		if (parameter != null)
		{
			parameter.AddBase(value);
		}
		else
		{
			logger.error("找不带参数数据："+type);
		}
		if (type == EParameterCate.Dizziness.value && value != 0)
		{
			this.OnDizzChange(true, value);
		}
	}
	public void RemoveBaseValue(int type,int value)
	{
		if (type < 0 || type >= EParameterCate.End.value)
		{
			return;
		}
		SSParameter parameter = paras[type];
		if (parameter != null)
		{
			parameter.RemoveBase(value);
		}
		else
		{
			logger.error("找不带参数数据："+type);
		}
		if (type == EParameterCate.Dizziness.value && value != 0)
		{
			this.OnDizzChange(false, value);
		}
	}
	public void AddPercentValue(int type,int value)
	{
		if (type < 0 || type >= EParameterCate.End.value)
		{
			return;
		}
		SSParameter parameter = paras[type];
		if (parameter != null)
		{
			parameter.AddPercent(value);
		}
		else
		{
			logger.error("找不带参数数据："+type);
		}
	}
	public void RemovePercentValue(int type,int value)
	{
		if (type < 0 || type >= EParameterCate.End.value)
		{
			return;
		}
		SSParameter parameter = paras[type];
		if (parameter != null)
		{
			parameter.RemovePercent(value);
		}
		else
		{
			logger.error("找不带参数数据："+type);
		}
	}
	
	public void ChangeHp(int value,int reason)
	{
		int noLimitValue = GetValue(EParameterCate.CurHp.value) + value;
		this.AddBaseValue(EParameterCate.CurHp.value, value);
		this.SyncHp(value>0?GetValue(EParameterCate.CurHp.value):noLimitValue, reason);
	}
	public void SyncHp(int value,int reason)
	{
		ResChangeHpMessage message = new ResChangeHpMessage();
		message.playerId = theOwner.id;
		message.reason = reason;
		message.value = value;
		MessageUtil.tell_battlePlayer_message(theOwner.battle, message);
	}
	/**
	 * 进入眩晕处理
	 * @param bAdd
	 * @param changeValue
	 */
	public void OnDizzChange(boolean bAdd,int changeValue)
	{
		int nowValue = paras[EParameterCate.Dizziness.value].GetValue();
		int oldValue = bAdd ? nowValue - changeValue : nowValue + changeValue;
		if (nowValue > 0 && oldValue <= 0)
		{
			theOwner.OnDizziness();
		}
	}
}
