package com.chen.battle.skill;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.ibatis.jdbc.Null;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.skill.config.SkillModelFlyConfig;
import com.chen.battle.skill.message.res.ResSkillFlyEmitDestoryMessage;
import com.chen.battle.skill.message.res.ResSkillFlyEmitMessage;
import com.chen.battle.skill.structs.ElementArray;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.SSGameUnit;
import com.chen.utils.MessageUtil;
import com.chen.utils.Tools;

public class SSSkillEffect_Emit extends SSSkillEffect
{
	/**
	 * 飞行道具实体
	 * @author Administrator
	 *
	 */
	public class SSBulletEntity
	{
		public long targetId;
		public SSGameUnit target;
		public CVector3D curPos = new CVector3D();
		public CVector3D curDir = new CVector3D();
		public long launchTime;
		public boolean bIfEnd;
		public int bulletId;
		public long lastEffectTime;
		public boolean bIfTurnBack;
		public ElementArray<Long> hitTargetArrays = new ElementArray<Long>(32);
		public void Clear()
		{
			this.hitTargetArrays.Clear();
		}
	}
	/**
	 * 飞行道具打到的目标信息
	 * @author Administrator
	 *
	 */
	public class SSEmitHitTargetInfo
	{
		public SSGameUnit target;
		public CVector3D pos;
		public CVector3D dir;
		
		public SSEmitHitTargetInfo(SSGameUnit target,CVector3D pos,CVector3D dir)
		{
			this.target = target;
			this.pos = pos;
			this.dir = dir;
		}
	}
	public Logger logger = LogManager.getLogger(SSSkillEffect_Emit.class);
	public ElementArray<SSBulletEntity> bulletEntityArrays = new ElementArray<>(16);
	public ElementArray<SSEmitHitTargetInfo> bulletHitTargetArrays = new ElementArray<>(12);
	public SkillModelFlyConfig flyConfig;
	@Override
	public boolean Begin() 
	{
		logger.debug("FlyBegin");
		if (theOwner == null)
		{
			logger.error("没有主实体");
			return false;
		}
		this.bulletEntityArrays.Clear();
		this.bulletHitTargetArrays.Clear();
		this.flyConfig = (SkillModelFlyConfig)this.config;
		int flyNum = this.flyConfig.num;
		int leftAngle = -(int)(this.flyConfig.angle * (flyNum - 1) * 0.5f);
		for (int i=0;i<flyNum;i++)
		{
			CVector3D curDir = theOwner.GetCurDir();
			int angle = leftAngle + this.flyConfig.angle * i;
			curDir.RotateAngle(angle);
			SSBulletEntity entity = new SSBulletEntity();
			if (target != null)
			{
				entity.targetId = target.id;
			}
			entity.target = target;
			SSGameUnit startObj = theOwner;
			if (startSkillGO != null)
			{
				startObj = startSkillGO;
			}
			
			entity.curPos = startObj.GetEmitPos();
			entity.curDir = curDir;
			entity.launchTime = System.currentTimeMillis();
			entity.lastEffectTime = entity.launchTime;
			entity.bIfEnd = false;
			entity.bIfTurnBack = false;
			entity.bulletId = battle.GenerateEffectId();
			boolean isCreated = true;
			if (isCreated)
			{
				this.bulletEntityArrays.AddElement(entity);
				this.NotifyBulletObjectShow(startObj, entity);
			}
		}
		return true;
	}
	/**
	 * 通知客户端飞行特效显示
	 */
	private void NotifyBulletObjectShow(SSGameUnit startObj,SSBulletEntity entity)
	{
		if (theOwner == null)
		{
			logger.error("TheOwner == null");
			return;
		}
		ResSkillFlyEmitMessage message = new ResSkillFlyEmitMessage();
		message.playerId = startObj.id;
		message.effectId = this.flyConfig.skillModelId;
		message.dirAngle = Tools.GetDirAngle(entity.curDir);
		message.emitId = entity.bulletId;
		if (entity.target != null)
		{
			message.targetId = entity.target.id;
			message.PosX = (int)(entity.target.GetCurPos().x * 1000);
			message.PosZ = (int)(entity.target.GetCurPos().z * 1000);
		}
		MessageUtil.tell_battlePlayer_message(battle, message);
	}
	private void NotifyBulletDestory(SSBulletEntity entity)
	{
		if (theOwner == null)
		{
			return ;
		}
		ResSkillFlyEmitDestoryMessage message = new ResSkillFlyEmitDestoryMessage();
		message.emitId = entity.bulletId;
		MessageUtil.tell_battlePlayer_message(battle, message);
	}
	@Override
	public boolean Update(long now, long tick) 
	{
		if (theOwner == null || theOwner.bExpire)
		{
			return false;
		}
		int maxHitTargetSize = this.bulletHitTargetArrays.GetMaxSize();
		for (int i=0;i<maxHitTargetSize;i++)
		{
			if (this.bulletHitTargetArrays.array.get(i).isVaild)
			{
				SSEmitHitTargetInfo targetInfo = this.bulletHitTargetArrays.array.get(i).element;
				if (targetInfo.target == null)
				{
					this.bulletHitTargetArrays.array.get(i).isVaild = false;
					--this.bulletHitTargetArrays.curSize;
					continue;
				}
				if (targetInfo.target.bExpire == false && targetInfo.target.IsDead() == false)
				{
					battle.effectManager.AddEffectsFromConfig(this.flyConfig.skillModelList, theOwner, targetInfo.target, targetInfo.pos, targetInfo.dir,
							skill, System.currentTimeMillis(), null);
					//技能命中目标
					//OnSkillHitTarget(entity.pTarget);
					logger.debug("技能命中目标");
				}
				this.bulletHitTargetArrays.array.get(i).isVaild = false;
				--this.bulletHitTargetArrays.curSize;
			}
		}
		
		int maxBulletSize = this.bulletEntityArrays.maxSize;
		for (int i=0;i<maxBulletSize;i++)
		{
			if (this.bulletEntityArrays.array.get(i).isVaild)
			{
				SSBulletEntity entity = this.bulletEntityArrays.array.get(i).element;
				this.OnHeartBeatSkillEnityImpact(entity, now, tick);
				if (entity.bIfEnd == true)
				{
					//通知客户端摧毁特效
					switch (this.flyConfig.eSkillModelFlyType)
					{
					case Direct:
						this.NotifyBulletDestory(entity);
						break;
					}
					entity.Clear();
					this.bulletEntityArrays.array.get(i).isVaild = false;
					--this.bulletEntityArrays.curSize;
				}
			}
		}
		if (this.bulletEntityArrays.curSize == 0 && this.bulletHitTargetArrays.curSize == 0)
		{
			return false;
		}
		return true;
	}
	public void OnHeartBeatSkillEnityImpact(SSBulletEntity entity,long now,long tick)
	{
		if (now - entity.launchTime < 50)
		{
			return;
		}
		if (battle == null)
		{
			return ;
		}
		if (this.flyConfig == null)
		{
			this.flyConfig = (SkillModelFlyConfig)this.config;
		}
		switch (this.flyConfig.eSkillModelFlyType) 
		{
		case Direct:
			if (entity.launchTime + this.flyConfig.lifeTime < now)
			{
				entity.bIfEnd = true;
				return ;
			}
			CVector3D entityPos = entity.curPos;
			CVector3D entityDir = entity.curDir;
			float fCanMoveDist = this.flyConfig.flySpeed * tick * 0.001f * 0.001f;
			Vector<SSGameUnit> hitTargetList = new Vector<>();
			this.CheckSkillEntityImpact(entity, fCanMoveDist, hitTargetList, this.flyConfig.bIsPenetrate, false);
			for (SSGameUnit obj : hitTargetList)
			{
				SSEmitHitTargetInfo info = new SSEmitHitTargetInfo(obj, obj.GetCurPos(),entityDir);
				this.bulletHitTargetArrays.AddElement(info);
			}
			if (hitTargetList.isEmpty() == false && this.flyConfig.bIsPenetrate == false)
			{
				entity.bIfEnd = true;
				return;
			}
			entityDir.normalized();
			entity.curPos = CVector3D.Add(entityPos, CVector3D.Mul(entityDir, fCanMoveDist));
			break;
		}
	}
	/**
	 * 检测飞行道具和哪些物体发生碰撞
	 * @param entity
	 * @param moveDist
	 * @param targetList
	 * @param bIsPenetrate 是否穿透
	 * @param bIsRepeat
	 */
	private void CheckSkillEntityImpact(SSBulletEntity entity,float moveDist, Vector<SSGameUnit> targetList,boolean bIsPenetrate,boolean bIsRepeat)
	{
		Set<SSGameUnit> vecAreaObj = new HashSet<>();
		battle.FindAroundGo(entity.curPos, this.flyConfig.colliderRaius, vecAreaObj);	
		for (SSGameUnit unit : vecAreaObj)
		{
			if (theOwner == unit)
			{
				continue;
			}
			if (unit.bExpire || unit.IsDead())
			{
				continue;
			}
			boolean bIsExist = false;
			if (bIsRepeat == false)
			{
				boolean exist = entity.hitTargetArrays.HasElement(unit.id);
				
				if (exist == false)
				{
					entity.hitTargetArrays.AddElement(unit.id);
				}
				else
				{
					bIsExist = true;
				}
			}
			if (bIsExist == false)
			{
				targetList.addElement(unit);
				if (bIsPenetrate == false)
				{
					break;
				}
			}
		}
	}
	@Override
	public void End()
	{
		
	}

}
