package com.chen.battle.bt;

import com.chen.battle.ai.SSAI_Robot;

public class BTSelectorNode extends BTNode
{

	public BTSelectorNode(int id)
	{
		super(id, EBTNodeType.Selector);		
	}

	@Override
	public boolean Tick(SSAI_Robot robot) 
	{
		//System.err.println(id);
		BTNode temp = null;
		for (BTNode child : this.children)
		{
			if (child.GetType() == EBTNodeType.Condition)
			{
				if (child.Tick(robot) == false)
				{
					return false;
				}
			}
			else if (child.GetType() == EBTNodeType.Action)
			{
				temp = child;
			}
			else if (child.Tick(robot) == true)
			{
				if (temp != null)
				{
					robot.actionNode = temp;
				}
				return true;
			}
		}
		if (temp != null)
		{
			robot.actionNode = temp;
			return true;
		}
		return false;
	}
}
