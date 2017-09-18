package com.chen.utils;

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
}
