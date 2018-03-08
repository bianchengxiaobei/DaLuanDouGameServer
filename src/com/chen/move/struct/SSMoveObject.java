package com.chen.move.struct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SSMoveObject
{
	public Logger logger = LogManager.getLogger(SSMoveObject.class);
	public SSMoveObjectStatus moveStatus;
	public ESSMoveObjectMoveType moveType;
	public long startMoveTime;//开始移动时间
	public long lastFailedMoveTime;//上次尝试移动失败
	public long endForceMoveTime;//强制移动结束时间
	public float forceMoveSpeed;//强制移动时间
	private float oldSpeed;//缓存的速度
	public boolean bIfSpeedChanged;//是否速度发生改变
	public ColVector askMoveDir;
	public ColVector dir;//实际方向
	public ColVector beforeMovePos;//上次位移坐标
	public ColSphere stepMoveTarget;
	public long bTryTrunTime;//上次尝试自动转向时间
	public boolean bIfForceMoveImapce;//强制位移是否需要检测碰撞
	
	public abstract ColVector GetColVector();
	public abstract ColSphere GetColSphere();
	public abstract float GetColRadius();
	public abstract boolean IfCanImpact();
	public abstract float GetSpeed();
	public abstract void OnMoved(ColVector pos);
	public abstract void OnMoveBlock();
	public abstract void OnStartMove(ColVector dir);
	public abstract void OnChangeDir(ColVector dir);
	public void CalculateStepMoveTarget(long now)
	{
		long timeDiff = now - startMoveTime;
		if (timeDiff > 500)
		{
			logger.error("时间太大:"+timeDiff);
			timeDiff = 500;			
		}
		if (timeDiff < 0)
		{
			logger.error("时间不对");
			timeDiff = 0;
		}
		float speed = moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_ForceMove ? forceMoveSpeed : GetSpeed();
		if (speed != oldSpeed)
		{
			oldSpeed = speed;
			bIfSpeedChanged = true;
		}
		ColVector moveVec = ColVector.Multiply(dir, speed * timeDiff * 0.001f);
		ColSphere sphere = GetColSphere();
		sphere.point.AddVector(moveVec);
		stepMoveTarget = sphere;
	}
	public float Move(long now)
	{
		beforeMovePos = GetColVector();
		ColSphere sph = stepMoveTarget;
		float dist = ColVector.Sub(sph.point, beforeMovePos).Length();
		OnMoved(sph.point);
		startMoveTime = now;
		if (bIfSpeedChanged)
		{
			//logger.debug("移动速度发生改变，通知客户端");
			bIfSpeedChanged = false;
			OnStartMove(dir);
		}
		return dist;
	}
	/**
	 * 传送，和位移基本相同，但是不算是移动，不触发位移的回调
	 * @param now
	 */
	public void Teleport(long now)
	{
		ColSphere pColSphere = stepMoveTarget;
		beforeMovePos = pColSphere.point;
		OnMoved(pColSphere.point);
		startMoveTime = now;
	}
	public void Stop(long now,boolean bCallback)
	{
		this.moveStatus = SSMoveObjectStatus.SSMoveObjectStatus_Stand;
		lastFailedMoveTime = now;
		ColSphere nowPos = GetColSphere();
		stepMoveTarget = nowPos;
		beforeMovePos = nowPos.point;
		if (bCallback)
		{
			OnMoveBlock();
		}
	}
	public void SetDir(ColVector dir)
	{
		this.dir = dir.Normalize();
		this.OnChangeDir(this.dir);
	}
}
