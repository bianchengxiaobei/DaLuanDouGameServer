package com.chen.utils;

import com.chen.battle.structs.CVector3D;

public class Tools
{
	public static int GetDirAngle(CVector3D dir)
	{
		return (int) (Math.atan2(dir.z, dir.x) * 10000);
	}
}
