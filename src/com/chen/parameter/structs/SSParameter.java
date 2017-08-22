package com.chen.parameter.structs;

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
	
}
