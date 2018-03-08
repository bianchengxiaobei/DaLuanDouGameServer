package com.chen.battle.ball;

import com.chen.battle.structs.CVector3D;

public class BallBornPosConfig
{
	public CVector3D[] bornPos = new CVector3D[4];
	public void Init()
	{
		bornPos[0] = new CVector3D(1.25f,0,9.22f);
		bornPos[1] = new CVector3D(1.25f,0,7.24f);
		bornPos[2] = new CVector3D(8.21f,0,7.24f);
		bornPos[3] = new CVector3D(8.21f,0,9.22f);
	}
}
