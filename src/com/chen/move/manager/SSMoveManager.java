package com.chen.move.manager;

import java.util.HashSet;
import java.util.Set;

import com.chen.battle.structs.SSMoveObject;
import com.chen.move.struct.ColSphere;
import com.chen.move.struct.ColVector;
import com.chen.move.struct.EAskStopMoveType;
import com.chen.move.struct.ESSMoveObjectMoveType;
import com.chen.move.struct.SSMoveObjectStatus;

public class SSMoveManager
{
	public Set<SSMoveObject> allMoveObjectSet = new HashSet<>();
	private SSMoveObject[] heartBeatTempArray = new SSMoveObject[1024];
	public void AddMoveObject(SSMoveObject object)
	{
		allMoveObjectSet.remove(object);
		object.moveStatus = SSMoveObjectStatus.SSMoveObjectStatus_Stand;
		object.stepMoveTarget = object.GetColSphere();
		object.beforeMovePos = object.stepMoveTarget.point;
		allMoveObjectSet.add(object);
	}
	public boolean AskStartMoveDir(SSMoveObject obj,ColVector dir)
	{
		if (AskStartMoveCheck(obj) == false)
		{
			return false;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Stand)
		{
			obj.moveType = ESSMoveObjectMoveType.Dir;
			long now = System.currentTimeMillis();
			obj.moveStatus = SSMoveObjectStatus.SSMoveObjectStatus_Move;
			obj.askMoveDir = dir;
			obj.SetDir(dir);;
			obj.startMoveTime = now;
			//计算下一个100毫秒后的位置
			obj.CalculateStepMoveTarget(now+100);
			//检测是否会撞墙
			
			obj.OnStartMove(dir);
			return true;
		}
		return true;
	}
	public boolean AskStopMoveObject(SSMoveObject obj,EAskStopMoveType type)
	{
		if (!this.allMoveObjectSet.contains(obj))
		{
			return false;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Stand)
		{
			return false;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Move)
		{
			if (type == EAskStopMoveType.All || 
					(type == EAskStopMoveType.Dir && obj.moveType == ESSMoveObjectMoveType.Dir) ||
					type == EAskStopMoveType.Target && obj.moveType == ESSMoveObjectMoveType.Target)
			{
				StopLastStep(obj, true);
			}
			
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_ForceMove)
		{
			
		}
		return true;
	}
	/**
	 * 移动心跳检测
	 */
	public void OnHeartBeat()
	{
		long now = System.currentTimeMillis();
		int moveObjectSize = this.allMoveObjectSet.size();
		int index = 0;
		for (SSMoveObject object : this.allMoveObjectSet)
		{
			if (object.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Stand)
			{
				continue;
			}
			if (object.moveType == ESSMoveObjectMoveType.Dir && object.dir.equals(object.askMoveDir) == false)
			{
				ColVector nowDir = object.dir;
				object.SetDir(object.askMoveDir);
				object.CalculateStepMoveTarget(now);
				//检测是否碰撞
				//如果有碰撞就将方向改回原来的方向->nowDir;
				//如果没有,发送消息
				object.OnStartMove(object.dir);
			}
			object.CalculateStepMoveTarget(now);
			this.heartBeatTempArray[index++] = object;
		}
		int moveTargetNum = 0;
		do
		{
			moveTargetNum = 0;
			for (int i=0; i<moveObjectSize; i++)
			{
				SSMoveObject object = this.heartBeatTempArray[i];
				if (object == null) continue;
				float moveDist = this.TryMove(object, now);
				if (moveDist > 0)
				{
					System.out.println("可以移动");
					++moveTargetNum;
					CheckTargetMoveStatus(object, moveDist);
					this.heartBeatTempArray[i] = null;
				}
			}
		}while(moveTargetNum > 0);
		for (int i=0; i<moveObjectSize; i++)
		{
			SSMoveObject obj = this.heartBeatTempArray[i];
			if (obj == null)
			{
				continue;
			}
			obj.Stop(now, true);
		}
		for (SSMoveObject object : this.allMoveObjectSet)
		{
			if (object.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Stand)
			{
				continue;
			}
			if (object.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_ForceMove)
			{
				continue;
			}
			object.CalculateStepMoveTarget(now+100);
			//检测是否碰撞
			//object.Stop(now, true);
		}
	}
	
	public boolean ResetPos(SSMoveObject obj,ColVector pos,boolean bIfImpact)
	{
		ColVector outPos = null;
		if (bIfImpact)
		{
			
		}
		else
		{
			outPos = pos;
		}
		ColSphere sphere = obj.GetColSphere();
		sphere.point = outPos;
		obj.stepMoveTarget = sphere;
		obj.Teleport(System.currentTimeMillis());
		return true;
	}
	private void CheckTargetMoveStatus(SSMoveObject obj,float moveDist)
	{
		if (obj.moveType != ESSMoveObjectMoveType.Target)
		{
			return;
		}
	}
	private boolean AskStartMoveCheck(SSMoveObject obj)
	{
		if (!this.allMoveObjectSet.contains(obj))
		{
			return false;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_ForceMove)
		{
			return false;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Move)
		{
			//先将上次移动终止
			StopLastStep(obj, false);
		}
		return true;
	}
	private void StopLastStep(SSMoveObject obj,boolean bCallback)
	{
		long now = System.currentTimeMillis();
		obj.CalculateStepMoveTarget(now);
		TryMove(obj, now);
		obj.Stop(now, bCallback);
	}
	private float TryMove(SSMoveObject obj,long now)
	{
		//静态碰撞检测
		//移动
		float moveDist =obj.Move(now); 
		return moveDist;
	}
}
