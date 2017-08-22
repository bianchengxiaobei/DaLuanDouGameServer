package com.chen.move.struct;

public class ColVector
{
	public float x;
	public float y;
	public float z;
	public ColVector(float x,float y,float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public ColVector(ColVector v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	public boolean equals(ColVector other)
	{
		if (this.x != other.x || this.y != other.y || this.z != other.z)
		{
			return false;
		}
		return true;
	}
	public float Length()
	{
		float ls = this.LengthSqrt();
		return (float) Math.sqrt(ls);
	}
	public float LengthSqrt()
	{
		return x*x + y*y + z*z;
	}
	public void AddVector(ColVector vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}
	public ColVector Normalize()
	{
		ColVector temp = new ColVector(0,0,0);
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
	public static ColVector Multiply(ColVector o, float value)
	{
		ColVector temp = new ColVector(o);
		temp.x = temp.x * value;
		temp.y = temp.y * value;
		temp.z = temp.z * value;
		return temp;
	}
	public static ColVector Sub(ColVector o1,ColVector o2)
	{
		ColVector temp = new ColVector(0,0,0);
		temp.x = o1.x - o2.x;
		temp.y = o1.y - o2.y;
		temp.z = o1.z - o2.z;
		return temp;
	}
}
