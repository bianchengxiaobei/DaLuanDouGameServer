package com.chen.player.structs;

public enum PlayerState
{
	Normal(0),
	Login(1),
	Room(2),
	Match(3),
	Battle(4),
	Quit(5);
	public int value;
	private PlayerState(int value) 
	{
		this.value = value;
	}
}
