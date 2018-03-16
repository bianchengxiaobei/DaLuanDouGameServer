package com.chen.battle.ai.structs;

public enum EAIRobotType 
{
	BallRobot(0),
	GuideRobot(1);
	public int value;
	private EAIRobotType(int value)
	{
		this.value = value;
	}
}
