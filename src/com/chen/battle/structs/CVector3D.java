package com.chen.battle.structs;

import com.chen.move.struct.ColVector;

public class CVector3D implements Cloneable
{
	public static double dDegToRad = Math.PI / 180.0f;
	public float x;
	public float y;
	public float z;
	public CVector3D()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
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
	public boolean IsZero()
	{
		return this.x == 0 && this.y == 0 && this.z == 0;
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
		if(y != 0)
		{
			System.err.println("y != null");
		}
		float z = targetPos.z - this.z;
		return x * x + y * y + z * z;
	}
	public void RotateAngle(double angle)
	{
		angle *= dDegToRad;
		double cs = Math.cos(angle);
		double sn = Math.sin(angle);
		float tempX = this.x;
		float tempZ = this.z;
		this.x = (float)(tempX * cs - tempZ * sn);
		this.z = (float)(tempX * sn + tempZ * cs);
	}
	public boolean equals(CVector3D v)
	{
		return this.x == v.x && this.y == v.y && this.z == v.z;
	}
	public ColVector toColVector()
	{
		ColVector point = new ColVector(this.x,this.y,this.z);
		return point;
	}
	public static CVector3D Sub(CVector3D v1,CVector3D v2)
	{
		CVector3D result = new CVector3D(0, 0, 0);
		result.x = v1.x - v2.x;
		result.y = v1.y - v2.y;
		result.z = v1.z - v2.z;
		return result;
	}
	public static CVector3D Add(CVector3D v1,CVector3D v2)
	{
		CVector3D result = new CVector3D();
		result.x = v1.x + v2.x;
		result.y = v1.y + v2.y;
		result.z = v1.z + v2.z;
		return result;
	}
	public static CVector3D Mul(CVector3D vector3d,float value)
	{
		CVector3D result = new CVector3D();
		result.x = vector3d.x * value;
		result.y = vector3d.y * value;
		result.z = vector3d.z * value;	
		return result;
	}
	public static float Mul(CVector3D vector3d,CVector3D vector3d2)
	{
		return vector3d.x * vector3d2.x + vector3d.y * vector3d2.y + vector3d.z * vector3d2.z;
	}
	@Override 
	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("X:"+this.x);
		stringBuffer.append("Y:"+this.y);
		stringBuffer.append("Z:"+this.z);
		return stringBuffer.toString();
	}
	@Override 
	public CVector3D clone() 
	{
		try 
		{
			return (CVector3D)super.clone();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
	}
}
