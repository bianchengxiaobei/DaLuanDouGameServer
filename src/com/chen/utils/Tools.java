package com.chen.utils;

import java.util.Set;

import com.chen.battle.structs.CVector3D;

public class Tools
{
	public static int GetDirAngle(CVector3D dir)
	{
		return (int) (Math.atan2(dir.z, dir.x) * 10000);
	}
	public static boolean IfEnemy(int leftCamp,int rightCamp)
	{
		if (leftCamp == 0 || rightCamp == 0 || leftCamp == rightCamp)
		{
			return false;
		}
		if (leftCamp % 2 == rightCamp % 2)
		{
			return false;
		}
		return true;
	}
	public static int[] ConvertIntSetToArray(Set<Integer> source)
	{
		int[] a = new int[source.size()];
		int index = 0;
		for (Integer integer : source)
		{
			a[index++] = integer;
		}
		return a;
	}
	static final int HeroStartId = 0;
	static final int HeroEndId = 1000;
	public static boolean IsHeroGood(int goodId)
	{
		if (goodId >= HeroStartId && goodId <= HeroEndId)
		{
			return true;
		}		
		return false;
	}
}
