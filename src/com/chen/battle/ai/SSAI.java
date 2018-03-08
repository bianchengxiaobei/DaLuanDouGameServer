package com.chen.battle.ai;

import javax.imageio.spi.IIOServiceProvider;

import com.chen.battle.ai.structs.EAttackState;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.parameter.structs.EParameterCate;

public class SSAI
{
	public SSGameUnit theOwner;
	public boolean bIsMoving;
	public long lastTryMoveTime;//上次尝试移动失败的时间
	public CVector3D moveTarPos;//当前前进的目标地址
	public long lastCheckMoveTarTime;//上一次检查目标坐标的时间
	public SSSkill attackSkill;
	public EAttackState eAttackState;
	public boolean bIsStandAttack = false;//站立攻击
	public SSAI(SSGameUnit _theOwner)
	{
		this.theOwner = _theOwner;
	}
	public boolean IfPassitiveState()
	{
		if (theOwner == null)
		{
			return true;
		}
		if (theOwner.curActionInfo.eOAS.value > EGOActionState.PassiveState.value)
		{
			return true;
		}
		//如果是昏迷状态
		if (theOwner.GetFPData(EParameterCate.Dizziness) > 0)
		{
			return true;
		}
		return false;
	}
	public void OnMoveBlock()
	{
		bIsMoving = false;
	}
	public void CancleAttack()
	{
		//this.moveTarPos.zero();
		if (eAttackState == EAttackState.Pursue)
		{
			//停止移动到目的地
		}
		if (this.attackSkill != null)
		{
			this.attackSkill.TryCancle();
		}
	}
	public void StopAttack()
	{
		this.CancleAttack();
		this.SetAttackTarget(null);
		this.moveTarPos.zero();
		lastCheckMoveTarTime = 0;
	}
	public void SetAttackTarget(SSGameUnit target)
	{
		if (attackSkill != null)
		{
			attackSkill.target = target;
		}
	}
	public void TryFree()
	{
		if (theOwner.curActionInfo.eOAS.value < EGOActionState.PassiveState.value)
		{
			theOwner.BeginActionIdle(true);
		}	
	}
	public void HeartBeat(long now,long tick)
	{
		
	}
	public void OnTeleport()
	{
		
	}
}
