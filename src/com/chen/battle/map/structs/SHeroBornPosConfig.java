package com.chen.battle.map.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chen.battle.structs.CVector3D;

public class SHeroBornPosConfig
{
	//随机点
	public List<CVector3D> bornList = new ArrayList<>();
	public CVector3D GetFirstBornPos()
	{		
		if (bornList.isEmpty())
		{
			return null;
		}
		else
		{
			return bornList.get(0);
		}
	}
	/**
	 * 取得随机一个点
	 * @return
	 */
	public CVector3D GetRandomOneBornPos()
	{
		if (bornList.isEmpty())
		{
			return null;
		}
		else
		{
			int len = bornList.size();
			Random random = new Random();
			return bornList.get(random.nextInt(len));
		}
	}
}
