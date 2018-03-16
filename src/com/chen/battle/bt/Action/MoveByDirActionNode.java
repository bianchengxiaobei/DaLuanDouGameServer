package com.chen.battle.bt.Action;

import com.chen.battle.ai.SSAI_Robot;
import com.chen.battle.bt.BTActionNode;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSHero;

public class MoveByDirActionNode extends BTActionNode
{
	public MoveByDirActionNode(int id, int[] params)
	{
		super(id, params);
	}
	@Override
	public boolean Action(SSAI_Robot robot)
	{
		SSHero hero = robot.hero;
		robot.lastTimeAction = hero.battle.battleHeartBeatTime;
		if (this.params[0] == 1)
		{
			//进攻
			robot.AskMoveDir(hero.GetCurDir());
			return true;
		}
		else if(this.params[0] == 2)
		{
			//回退
			CVector3D curDir = CVector3D.Mul(hero.GetCurDir(), -1);
			robot.AskMoveDir(curDir);
			return true;
		}
		return false;
	}
	
}
