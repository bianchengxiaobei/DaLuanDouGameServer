package com.chen.move.manager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.map.EBlockType;
import com.chen.map.MapRegion;
import com.chen.map.MapStaticData;
import com.chen.move.struct.ColSphere;
import com.chen.move.struct.ColVector;
import com.chen.move.struct.EAskStopMoveType;
import com.chen.move.struct.ESSMoveObjectMoveType;
import com.chen.move.struct.SSMoveObject;
import com.chen.move.struct.SSMoveObjectStatus;
import com.chen.utils.Tools;

public class SSMoveManager
{
	public Logger logger = LogManager.getLogger(SSMoveManager.class);
	public Set<SSMoveObject> allMoveObjectSet = new HashSet<>();
	public MapStaticData staticData;
	public BattleContext battle;
	private SSMoveObject[] heartBeatTempArray = new SSMoveObject[6];
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
			long now = battle.battleHeartBeatTime;
			obj.moveStatus = SSMoveObjectStatus.SSMoveObjectStatus_Move;
			obj.askMoveDir = dir.Normalize();
			obj.SetDir(dir);;
			obj.startMoveTime = now;			
			//计算下一个100毫秒后的位置
			obj.CalculateStepMoveTarget(now+100);
			//检测是否会撞墙
			if (TestNextStepMove(obj, true, true) == false)
			{
				if (TryTurnDir(obj) == false)
				{
					obj.Stop(now, false);
					return false;
				}
			}
			obj.OnStartMove(obj.dir);
			return true;
		}
		return true;
	}
	public boolean AskStartMoveToTarget(SSMoveObject obj,CVector3D targetPos,boolean bIfMoveToBlackPoint,boolean bIfFilter)
	{
		long now = battle.battleHeartBeatTime;
		if (now - obj.lastFailedMoveTime < 150)
		{
			//同一个单位的最短尝试时间为150;
			return false;
		}
		if (this.AskStartMoveCheck(obj) == false)
		{
			obj.lastFailedMoveTime = now;
			return false;
		}
		obj.moveType = ESSMoveObjectMoveType.Target;
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Stand)
		{
			obj.startMoveTime = now;
			this.SetNextMovePoint(obj, targetPos);
			obj.CalculateStepMoveTarget(now+100);
			if (TestNextStepMove(obj, true, false) == false)
			{
				obj.Stop(now, false);
				return false;
			}
			obj.OnStartMove(obj.dir);			
			return true;
		}
		obj.lastFailedMoveTime = now;
		return false;
	}
	/**
	 * 请求强制位移
	 * @param obj
	 * @param dir
	 * @param speed
	 * @param bIfImpact
	 * @return
	 */
	public boolean AskMoveForced(SSMoveObject obj,ColVector dir,float speed, boolean bIfImpact,long endtime)
	{
		if (AskStartForceMoveCheck(obj) == false)
		{
			return false;
		}
		obj.moveType = ESSMoveObjectMoveType.ForceMove;
		long now = battle.battleHeartBeatTime;
		obj.SetDir(dir);
		obj.startMoveTime = now;
		obj.moveStatus = SSMoveObjectStatus.SSMoveObjectStatus_ForceMove;
		obj.bIfForceMoveImapce = bIfImpact;
		obj.forceMoveSpeed = speed;
		obj.endForceMoveTime = endtime;
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
			if (type == EAskStopMoveType.All || type == EAskStopMoveType.ForceMove)
			{
				StopLastStep(obj, true);
				//这里可能要处理碰撞
				
			}
		}
		return true;
	}
	public boolean TestNextStepMove(SSMoveObject object,boolean bIfCheckStatic,boolean bIfCheckDynamic)	
	{
		if (bIfCheckStatic && IfStaticImpact(object))
		{
			if (object.moveType == ESSMoveObjectMoveType.Dir)
			{
				//改变方向
				if (TryTurnDir(object))
				{
					return true;
				}
			}
			return false;
		}
		return true;
	}
	public boolean IfStaticImpact(SSMoveObject object)
	{

//		try {
			ColSphere nextPoint = object.stepMoveTarget;
			float x = nextPoint.point.x;
			float y = nextPoint.point.z;
//			if (Math.round(object.dir.x) == 1)
//			{
//				x = nextPoint.point.x + colRadius;
//				if (object.dir.z > 0)
//				{
//					y = nextPoint.point.z + colRadius;
//				}
//				else
//				{
//					y = nextPoint.point.z - colRadius;
//				}
//			}
//			else if(Math.round(object.dir.x) == -1)
//			{
//				x = nextPoint.point.x - colRadius;
//				if (object.dir.z > 0)
//				{
//					y = nextPoint.point.z + colRadius;
//				}
//				else
//				{
//					y = nextPoint.point.z - colRadius;
//				}
//			}
//			else 
//			{
//				if (object.dir.z > 0)
//				{
//					y = nextPoint.point.z + colRadius;
//				}
//				else
//				{
//					y = nextPoint.point.z - colRadius;
//				}
//			}
			
			int regionId = GetRegionId_Float(x, y);
			//System.out.println(regionId+",x:"+x+",y:"+y);
			try {
				MapRegion region = this.staticData.regions[regionId];
				if (region.blockType == EBlockType.Block)
				{
					return true;
				}
			} catch (Exception e) {
				logger.error("Regin == null:"+regionId);
				logger.error("X:"+this.GetRegionX(x));
				logger.error("Y"+this.GetRegionY(y));
				return false;
			}
			return false;
//		}
//		catch (Exception e) 
//		{
//			logger.error(e);
//			return false;		
//		}

	}
	public boolean TryTurnDir(SSMoveObject object)
	{
		try 
		{
			if (object.moveType != ESSMoveObjectMoveType.Dir)
			{
				logger.error("不是摇杆不能转向");
				return false;
			}
			ColVector nowDir = object.dir;
			ColVector[] tryDirs = new ColVector[4];
			tryDirs[0] = new ColVector(0, 0, 1);
			tryDirs[1] = new ColVector(0, 0, -1);
			tryDirs[2] = new ColVector(1, 0, 0);
			tryDirs[3] = new ColVector(-1, 0, 0);
			long now = battle.battleHeartBeatTime;
			for (int i=0;i<4;i++)
			{
				ColVector newDir = tryDirs[i];
				int dirIndex = Math.round(nowDir.z);
				if (dirIndex == 1 || dirIndex == -1)
				{
					if (i == 0 || i == 1)
					{
						continue;
					}
				}
				object.SetDir(newDir);
				object.CalculateStepMoveTarget(now+100);
				if (IfStaticImpact(object) == false)
				{
					object.OnStartMove(newDir);
					object.bTryTrunTime = now;
					return true;
				}
			}
			object.SetDir(nowDir);
			return false;
		} 
		catch (Exception e)
		{
			logger.error(e);
			return false;
		}	
	}
	/**
	 * 移动心跳检测
	 */
	public void OnHeartBeat()
	{
		long now = battle.battleHeartBeatTime;
		int moveObjectSize = this.allMoveObjectSet.size();
		for (int i=0;i<6;i++)
		{
			this.heartBeatTempArray[i] = null;
		}
		int index = 0;
		for (SSMoveObject object : this.allMoveObjectSet)
		{
			if (object.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Stand)
			{
				continue;
			}
			if (object.moveType == ESSMoveObjectMoveType.Dir && object.dir.equals(object.askMoveDir) == false)
			{
				//logger.error("移动方向改变，发送移动消息");
				ColVector nowDir = object.dir;
				object.SetDir(object.askMoveDir);
				object.CalculateStepMoveTarget(now);
				//检测是否碰撞
				//如果有碰撞就将方向改回原来的方向->nowDir;
				//如果没有,发送消息
				if (IfStaticImpact(object))
				{
					object.SetDir(nowDir);
				}
				else
				{
					object.OnStartMove(object.dir);
				}
				//object.OnStartMove(object.dir);
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
					//System.out.println("可以移动");
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
			if (TestNextStepMove(object, object.moveType == ESSMoveObjectMoveType.Dir, true) == false)
			{
				object.Stop(now, true);
			}					
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
		obj.Teleport(battle.battleHeartBeatTime);
		return true;
	}
	public int GetRegionX(float x)
	{		
		int indexX = Math.round(x / staticData.bordeSize);
		if (indexX < 0)
		{
			indexX = 0;
		}
		if (indexX >= staticData.widthRegionSize)
		{
			indexX = staticData.widthRegionSize - 1;
		}
		return indexX;
	}
	public int GetRegionY(float y)
	{
		int indexY = Math.round(y / staticData.bordeSize);
		if (indexY < 0)
		{
			indexY = 0;
		}
		if (indexY >= staticData.heightRegionSize)
		{
			indexY = staticData.heightRegionSize - 1;
		}
		return indexY;
	}
	public int GetRegionId_Int(int x, int y)
	{
		return x * staticData.heightRegionSize + y;
	}
	public int GetRegionId_Float(float x, float y)
	{
		return this.GetRegionId_Int(this.GetRegionX(x), GetRegionY(y));
	}
	private void CheckTargetMoveStatus(SSMoveObject obj,float moveDist)
	{
		if (obj.moveType != ESSMoveObjectMoveType.Target)
		{
			return;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_ForceMove)
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
			logger.error("当前是强制位移状态，不能移动");
			return false;
		}
		if (obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_Move)
		{
			//先将上次移动终止
			StopLastStep(obj, false);
		}
		return true;
	}
	private boolean AskStartForceMoveCheck(SSMoveObject obj)
	{
		if (!this.allMoveObjectSet.contains(obj))
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
		long now = battle.battleHeartBeatTime;
		obj.CalculateStepMoveTarget(now);
		TryMove(obj, now);
		obj.Stop(now, bCallback);
	}
	private float TryMove(SSMoveObject obj,long now)
	{
		float moveDist = 0;
		//静态碰撞检测
		boolean bIfForceMoveImpact = obj.moveStatus == SSMoveObjectStatus.SSMoveObjectStatus_ForceMove && obj.bIfForceMoveImapce;
		if (bIfForceMoveImpact)
		{
			if (IfStaticImpact(obj))
			{
				if (obj.endForceMoveTime - obj.startMoveTime < 500)
				{
					obj.CalculateStepMoveTarget(obj.endForceMoveTime);
				}
				else
				{
					obj.CalculateStepMoveTarget(obj.startMoveTime + 500);
				}
				if(IfStaticImpact(obj))
				{
					obj.CalculateStepMoveTarget(now);
					if (bIfForceMoveImpact)
					{
						moveDist = 0;
						return moveDist;
					}
				}
				else
				{
					obj.CalculateStepMoveTarget(now);
				}
			}
		}
		//移动
		moveDist =obj.Move(now); 
		return moveDist;
	}
	private boolean SetNextMovePoint(SSMoveObject obj,CVector3D targetPos)
	{
		ColVector dir = ColVector.Sub(targetPos.toColVector(), obj.GetColVector()).Normalize();
		obj.SetDir(dir);
		obj.moveStatus = SSMoveObjectStatus.SSMoveObjectStatus_Move;
		return true;
	}	
}
