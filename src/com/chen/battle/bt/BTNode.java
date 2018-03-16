package com.chen.battle.bt;

import java.util.ArrayList;
import java.util.List;

import com.chen.battle.ai.SSAI_Robot;

public abstract class BTNode 
{
	protected int id;
	protected EBTNodeType type;
	protected List<BTNode> children;
	
	public BTNode(int id,EBTNodeType type)
	{
		this.id = id;
		this.type = type;
		this.children = new ArrayList<>();
	}
	
	public boolean IfLeafNode()
	{
		return type == EBTNodeType.Condition || type == EBTNodeType.Action;
	}
	public int GetId()
	{
		return this.id;
	}
	public EBTNodeType GetType()
	{
		return this.type;
	}
	
	public void AddChild(BTNode node)
	{
		if (node.GetType() == EBTNodeType.Action)
		{
			this.children.add(node);
			return;
		}
		else
		{
			for (int i=0; i<children.size(); i++)
			{
				BTNode childNode = children.get(i);
				if (childNode.GetType() != EBTNodeType.Condition && node.GetType() == EBTNodeType.Condition)
				{
					this.children.add(i, node);
					return;
				}
				if (childNode.GetType() == EBTNodeType.Action && node.GetType().value <= EBTNodeType.Sequence.value)
				{
					this.children.add(i, node);
					return;
				}
			}
			this.children.add(node);
			return ;
		}
	}
	public abstract boolean Tick(SSAI_Robot robot);	
}
