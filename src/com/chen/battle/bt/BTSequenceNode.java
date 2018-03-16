package com.chen.battle.bt;

import com.chen.battle.ai.SSAI_Robot;

public class BTSequenceNode extends BTNode
{

	public BTSequenceNode(int id)
	{
		super(id, EBTNodeType.Sequence);		
	}

	@Override
	public boolean Tick(SSAI_Robot robot) 
	{
		//System.err.println(id);
		int nodeNum = 0;
		BTNode temp = null;
		for (BTNode child : this.children)
		{
			if (child.GetType() == EBTNodeType.Action)
			{
				temp = child;
			}
			else
			{
				nodeNum++;
			}
			if (child.Tick(robot) == false)
			{
				return false;
			}
		}
		if (temp != null)
		{
			robot.actionNode = temp;
		}
		if (robot.actionNode == null)
		{
			return false;
		}
		if (nodeNum == 0)
		{
			return false;
		}
		return true;
	}
	
}
