package com.chen.battle.structs;


public class SGOActionStateInfo
{
	public EGOActionState eOAS;
	public long time;
	public CVector3D pos;
	public CVector3D dir;
	public CVector3D skillDir;//技能方向
	public int skillId;
	public CVector3D skillParams;//技能目标位置
	public long skillTargetId;
	public float distMove;
	public SGOActionStateInfo()
	{
		eOAS = EGOActionState.Idle;
		time = System.currentTimeMillis();
	}
}
