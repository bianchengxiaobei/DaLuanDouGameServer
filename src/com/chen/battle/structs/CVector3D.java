package com.chen.battle.structs;

public class CVector3D
{
	public float x;
	public float y;
	public float z;
	public CVector3D(float x,float y,float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void zero()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	public float Length()
	{
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	public float SqrtLength()
	{
		return x*x+y*y+z*z;
	}
	public CVector3D Normalize()
	{
		CVector3D temp = new CVector3D(0,0,0);
		float length = this.Length();
		if (length == 0.0f)
		{
			return temp;
		}
		temp.x = this.x / length;
		temp.y = this.y / length;
		temp.z = this.z / length;
		return temp;
	}
	public void normalized()
	{
		float length = this.Length();
		if (length == 0)
		{
			return;
		}
		this.x = this.x / length;
		this.y = this.y / length;
		this.z = this.z / length;
	}
	public boolean CanWatch(float dist,CVector3D targetPos)
	{
		return this.GetWatchDistSqr(targetPos) <= dist * dist;
	}
	public float GetWatchDistSqr(CVector3D targetPos)
	{
		float x = targetPos.x - this.x;
		float y = targetPos.y - this.y;
		float z = targetPos.z - this.z;
		return x * x + y * y + z * z;
	}
	public static CVector3D Sub(CVector3D v1,CVector3D v2)
	{
		CVector3D result = new CVector3D(0, 0, 0);
		result.x = v1.x - v2.x;
		result.y = v1.y - v2.y;
		result.z = v1.z - v2.z;
		return result;
	}
}
