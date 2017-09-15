package com.chen.parameter.structs;

import java.util.Vector;

public class SSParameter 
{
	private EParameterType type;
	private int value;
	private int baseValue;
	private int minValue;
	private int maxValue;
	private int minBaseValue;
	private int maxBaseValue;
	private boolean hasMinValue;
	private boolean hasMaxValue;
	private boolean hasMinBaseValue;
	private boolean hasMaxBaseValue;	
	private boolean bIfChanged;//是否改变过
	private boolean bIfCountdown;//是否按照倒数计算
	private	boolean	bIfOnlyOneMinusePercent;//是否减少的比例只计算最高值
	private int percent;//千分比改变量
	private int percentAdd;//千分比增加量
	private Vector<Integer> percentSubVec = new Vector<>();//千分比减少列表
	public boolean maxValueCallBack = false;
	public boolean valueCallBack = false;
	
	public void SetType(EParameterType type)
	{
		this.type = type;
	}
	public void SetMinValue(int value)
	{
		this.hasMinValue = true;
		this.minValue = value;
		if (this.value < this.minValue)
		{
			this.value = this.minValue;
		}
	}
	public void SetMaxValue(int value)
	{
		this.hasMaxValue = true;
		this.maxValue = value;
		if (this.value > this.maxValue)
		{
			this.value = this.maxValue;
		}
	}
	public void SetMinBaseValue(int value)
	{
		this.hasMinBaseValue = true;
		this.minBaseValue = value;
		if (this.baseValue < minBaseValue)
		{
			this.baseValue = minBaseValue;
		}
	}
	public void SetMaxBaseValue(int value)
	{
		this.hasMaxBaseValue = true;
		this.maxBaseValue = value;
		if (this.baseValue > maxBaseValue)
		{
			this.baseValue = maxBaseValue;
		}
	}
	public int GetValue()
	{
		return this.value;
	}
	public void AddBase(int value)
	{
		this.ChangeBaseValue(value);
		this.ReCount();
	}
	public void RemoveBase(int value)
	{
		this.ChangeBaseValue(-value);
		this.ReCount();
	}
	public void AddPercent(int value)
	{
		if (this.bIfOnlyOneMinusePercent == true)
		{
			if (value > 0)
			{
				this.percentAdd += value;
			}
			else
			{
				this.percentSubVec.add(value);
			}
			this.RecountPersent();
		}
		else
		{
			this.percent += value;
		}
		this.ReCount();
	}
	public void RemovePercent(int value)
	{
		if (this.bIfOnlyOneMinusePercent == true)
		{
			if (value > 0)
			{
				this.percentAdd -= value;
			}
			else
			{
				if (this.percentSubVec.contains(value))
				{
					this.percentSubVec.remove((Integer)value);
				}
			}
			this.RecountPersent();
		}
		else
		{
			this.percent -= value;
		}
		this.ReCount();
	}
	
	
	
	public void OnMaxValueChanged(int oldValue,int newValue)
	{
		int targetNewBaseValue = oldValue == 0 ? 0 : this.baseValue * newValue / oldValue;
		this.SetMaxBaseValue(newValue);
		if (oldValue != 0)
		{
			AddBase(targetNewBaseValue - baseValue); 
		}
	}
	public void OnValueChanged(int oldValue,int newValue)
	{
		
	}
	private void ChangeBaseValue(int value)
	{
		this.baseValue += value;
		if (hasMinBaseValue && minBaseValue > this.baseValue)
		{
			this.baseValue = minBaseValue;
		}
		if (hasMaxBaseValue && maxBaseValue < this.baseValue)
		{
			this.baseValue = maxBaseValue;
		}
	}
	private void ReCount()
	{
		int oldValue = this.value;
		if (type == EParameterType.Normal && this.percent != 0)
		{
			if (this.bIfCountdown)
			{
				this.value = (int)(this.baseValue / (1 + (float)(percent / 1000)));
			}
			else
			{
				this.value = (int)(this.baseValue * (1 + (float)(percent / 1000)));
			}
		}
		else
		{
			this.value = baseValue; 
		}
		if (hasMinValue && this.value < this.minValue)
		{
			this.value = minValue;
		}
		if (hasMaxValue && this.value > this.maxValue)
		{
			this.value = maxValue;
		}
		this.bIfChanged = true;
		if  (maxValueCallBack && oldValue != this.value)
		{
			this.OnMaxValueChanged(oldValue, this.value);
		}
		if (valueCallBack && oldValue != value && oldValue != 0)
		{
			this.OnValueChanged(oldValue, this.value);
		}
	}
	private void RecountPersent()
	{
		//取最小值
		int minSub = 0;
		for (int sub : this.percentSubVec)
		{
			if (sub < minSub)
			{
				minSub = sub;
			}
		}
		this.percent = percentAdd + minSub;
	}
}
