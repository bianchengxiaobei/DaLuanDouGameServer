package com.chen.move.struct;

public class ColSphere 
{
	public ColVector point;
	public float radius;
	public ColSphere(ColVector p,float r)
	{
		this.point = p;
		this.radius = r;
	}
	@Override
	public String toString()
	{
		return this.point.toString();
	}
}
