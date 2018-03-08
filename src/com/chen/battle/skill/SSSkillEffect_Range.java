package com.chen.battle.skill;

import com.chen.battle.skill.config.SSkillModelRangleConfig;
import com.chen.battle.skill.message.res.ResSkillRangeShowMessage;
import com.chen.battle.skill.structs.ESkillAOEType;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSGameUnit;
import com.chen.utils.MessageUtil;
import com.chen.utils.Tools;

public class SSSkillEffect_Range extends SSSkillEffect
{
	public long lauchTime;
	public long nextTime;
	public int rangeTimes;
	public CVector3D curPos = new CVector3D();
	public SSkillModelRangleConfig rangeEffectConfig;
	@Override
	public boolean Begin()
	{
		this.rangeEffectConfig = (SSkillModelRangleConfig)this.config;
		if (this.rangeEffectConfig == null)
		{
			return false;
		}
		nextTime = 0;
		rangeTimes = 0;
		lauchTime = System.currentTimeMillis();
		curPos.zero();
		if (rangeEffectConfig.eSkillAOEType == ESkillAOEType.SelfCenter)
		{
			pos = theOwner.GetCurPos();
			curPos = pos;
		}
		else if(rangeEffectConfig.eSkillAOEType == ESkillAOEType.FixDist)
		{
			curPos = CVector3D.Add(theOwner.GetCurPos(), CVector3D.Mul(theOwner.GetCurDir(), this.rangeEffectConfig.releaseDist*0.001f)); 
		}
		else if(rangeEffectConfig.eSkillAOEType == ESkillAOEType.TargetCenter)
		{
			if (this.target != null)
			{
				curPos = target.GetCurPos();
			}
		}
		else if(rangeEffectConfig.eSkillAOEType == ESkillAOEType.CustomPos)
		{
			curPos = theOwner.curActionInfo.skillParams;
			dir = CVector3D.Sub(curPos, theOwner.GetCurPos());
			dir.normalized();
		}
		this.NotifySkillRangeBegin(theOwner, curPos, dir);
		Update(System.currentTimeMillis(), 0);
		return true;
	}
	private void NotifySkillRangeBegin(SSGameUnit obj,CVector3D pos,CVector3D dir)
	{
		ResSkillRangeShowMessage message = new ResSkillRangeShowMessage();
		message.playerId = obj.id;
		message.PosX = (int)(pos.x * 1000);
		message.PosZ = (int)(pos.z * 1000);
		message.dirAngle = Tools.GetDirAngle(dir);
		message.effectId = this.rangeEffectConfig.skillModelId;
		message.projectId = this.id;
		MessageUtil.tell_battlePlayer_message(battle, message);
	}
	@Override
	public boolean Update(long now, long tick)
	{
		if (now < nextTime)
		{
			return true;
		}
		if (theOwner == null)
		{
			return false;
		}
		if (this.rangeEffectConfig.releaseTimeDelay > 0 && now - lauchTime < this.rangeEffectConfig.releaseTimeDelay)
		{
			return true;
		}
		if (this.lauchTime > 0 && now - lauchTime > this.rangeEffectConfig.lifeTime)
		{
			return false;
		}
		if (this.rangeTimes >= this.rangeEffectConfig.rangleTimes && now - lauchTime > this.rangeEffectConfig.lifeTime)
		{
			return false;
		}
		//每隔rangeIntervalTIme检测一次
		if (this.rangeTimes < this.rangeEffectConfig.rangleTimes)
		{
			this.GetBeHittedObjs();
			this.rangeTimes++;
			nextTime = now + this.rangeEffectConfig.rangeIntervalTime;
		}
		return true;
	}
	
	@Override
	public void End()
	{
		lauchTime = 0;
		nextTime = 0;
		rangeTimes = 0;
		IsForceStop = false;
		curPos = null;
	}
	public boolean GetBeHittedObjs()
	{
		SSGameUnit[] objs = new SSGameUnit[20];
		int length = 0;
		for (SSGameUnit unit : battle.gameObjectMap.values())
		{
			if (unit == theOwner)
			{
				continue;
			}
			CVector3D otherPos = unit.GetCurPos();
			boolean result = false;
			switch (this.rangeEffectConfig.eSkillRangeShapeType)
			{
			case Circle:
				result = this.IsInCircle(otherPos, curPos, this.rangeEffectConfig.rangeParam1 * 0.001f);
				break;
			case Rectangle:
				result = this.IsInRectangle(otherPos, curPos, dir, this.rangeEffectConfig.rangeParam1, this.rangeEffectConfig.rangeParam2);
				break;
			case Sector:
				if (otherPos.equals(curPos))
				{
					result = true;
				}
				else
				{
					float radian = this.rangeEffectConfig.rangeParam1;
					float radius = this.rangeEffectConfig.rangeParam2;
					result = this.IsInSetor(otherPos, curPos, radian * 0.5f, radius, dir);
				}
				break;
			}
			if (result)
			{
				objs[length++] = unit;
			}
		}
		if (length == 0)
		{
			return false;
		}
		return this.DoNextModule(objs, length);
	}
	public boolean DoNextModule(SSGameUnit[] hittedObjs,int length)
	{
		if (theOwner == null)
		{
			return false;
		}
		int effctNum = this.rangeEffectConfig.maxEffectObj;
		if (effctNum == 1)
		{
			//随机取得一个英雄
			return true;
		}
		else if(effctNum < 1)
		{
			effctNum = 9999;
		}
		for (int i=0;i<length;i++)
		{
			SSGameUnit obj = hittedObjs[i];
			if (obj == null)
			{
				System.err.println("HittedObj == null");
				continue;
			}
			this.CallNextModule(obj);
			if (i > effctNum)
			{
				break;
			}
		}
		return true;
	}
	private boolean CallNextModule(SSGameUnit obj)
	{
		CVector3D targetDir = CVector3D.Sub(obj.GetCurPos(), theOwner.GetCurPos()) ;
		targetDir.y = 0;
		targetDir.normalized();
		battle.effectManager.AddEffectsFromConfig(this.rangeEffectConfig.skillModelList, theOwner,
				obj, obj.GetCurPos(), targetDir, skill, System.currentTimeMillis(), null);
		//调用技能命中
		System.out.println("Range命中目标");
		return true;
	}
	public boolean IsInCircle(CVector3D checkPos,CVector3D theOwnerPos,float radius)
	{
		return checkPos.CanWatch(radius, theOwnerPos);
	}
	public boolean IsInRectangle(CVector3D checkPos,CVector3D theOwnerPos,CVector3D dir,int length,int width)
	{
		dir.y = 0;
		CVector3D f2 = CVector3D.Sub(checkPos, theOwnerPos);
		float totLen = f2.Length();
		float dirLen = dir.Length();
		if ((dirLen < 0.000001f)  && (dirLen > -0.000001f)){
			return false;
		}
		float angle = CVector3D.Mul(f2, dir) / (totLen * dirLen);
		if (angle < 0){
			return false;
		}

		float curLength = totLen * angle;
		if (curLength > length){
			return false;
		}
		double a = 1 - angle * angle;
		float curWidth = (float) (totLen * Math.sqrt(a));
		if (curWidth > width){
			return false;
		}
		return true;
	}
	public boolean IsInSetor(CVector3D checkPos,CVector3D theOwnerPos,float radian, float radius,CVector3D dir)
	{
		float dx = checkPos.x - theOwnerPos.y;
		float dz = checkPos.z - theOwnerPos.z;

		float squareLength = dx * dx + dz * dz;

		if (squareLength > radius * radius){
			return false;
		}

		float cosTheta = (float) Math.cos(radian);
		float dotT = dx * dir.x + dz * dir.z;

		if (dotT >= 0 && cosTheta >= 0){
			return dotT * dotT > squareLength * cosTheta * cosTheta;
		}
		else if (dotT < 0 && cosTheta < 0){
			return dotT * dotT < squareLength * cosTheta * cosTheta;
		}
		else{
			return dotT >= 0;
		}
	}
}
