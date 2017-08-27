package com.chen.battle.structs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.ai.SSAI_Hero;
import com.chen.battle.hero.config.SHeroConfig;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.skill.config.SSSkillConfig;
import com.chen.battle.skill.message.res.ResSkillInfoChangeMessage;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.data.manager.DataManager;
import com.chen.move.struct.ColVector;
import com.chen.utils.MessageUtil;

public class SSHero extends SSGameUnit
{
	private Logger log = LogManager.getLogger(SSHero.class);
	SSPlayer player;
	public SHeroConfig config;
	public CVector3D bornPos;
	public float colliderRadius;
	public SSSkill[] skillArray = new SSSkill[7];//最多7个技能
	public long lockedTargetId;//技能锁定目标id
	public SSHero(long playerId, BattleContext battle)
	{
		super(playerId, battle);
		for (int i=0;i<skillArray.length;i++)
		{
			skillArray[i] = new SSSkill();
		}
	}
	public void LoadHeroConfig(SHeroConfig config)
	{
		this.config = config;
		for (int i=0;i<this.skillArray.length;i++)
		{
			this.skillArray[i].Clear();
		}
		for (int i=0;i<this.skillArray.length;i++)
		{
			int skillId = this.config.skillList[i];
			if (skillId == 0)
			{
				continue;
			}
			System.out.println("Skillconfig:"+DataManager.getInstance().skillConfigXMLLoader.skillConfigMap.size());
			//通过xml加载成配置类
			SSSkillConfig cpSkillConfig = DataManager.getInstance().skillConfigXMLLoader.skillConfigMap.get(skillId);
			if (cpSkillConfig == null)
			{
				log.error("找不到该技能配置："+skillId);
				continue;
			}
			skillArray[i].skillConfig = cpSkillConfig;
			skillArray[i].theOwner = this;
			skillArray[i].eSkillState = ESkillState.Free;
			skillArray[i].stateTime = 0;
			
			if (cpSkillConfig.bIsNormalAttack)
			{
				normalAttackSkill = skillArray[i];
			}
		}
		this.colliderRadius = config.colliderRadius;
		//加入基础属性
		//加满血量，通知客户端血量改变
	}
	@Override
	public float GetColliderRadius()
	{	
		return colliderRadius;
	}
	@Override
	public float GetSpeed() 
	{	
		return 1;
	}
	@Override
	public void OnMoved(ColVector pos)
	{
		CVector3D cPos = new CVector3D(0, 0, 0);
		cPos.x = pos.x;
		cPos.y = pos.y;
		cPos.z = pos.z;
		curActionInfo.pos = cPos;
	}
	public void AskMoveDir(CVector3D dir)
	{
		SSAI_Hero hAi = (SSAI_Hero)ai;
		if (hAi != null)
			hAi.AskMoveDir(dir);
	}
	public void AskStopMove()
	{
		SSAI_Hero hAi = (SSAI_Hero)ai;
		if (hAi != null)
			hAi.AskStopMove();
	}
	public void AskUseSkill(int skillId)
	{
		SSAI_Hero hAi = (SSAI_Hero)ai;
		if (hAi != null)
			hAi.AskUseSkill(skillId);
	}
	public void ResetAI()
	{
		if (ai != null)
		{
			ai = null;
		}
		ai = new SSAI_Hero(this);
	}
	/**
	 * 是否拥有该技能
	 * @param skillId
	 * @return
	 */
	public SSSkill GetSkillById(int skillId)
	{
		if (skillId == 0)
		{		
			return null;
		}
		//最多7个技能
		for (int i=0;i<7;i++)
		{
			if (this.skillArray[i] != null)
			{
				SSSkillConfig config = this.skillArray[i].skillConfig;
				if (config != null && config.skillId == skillId)
				{
					return this.skillArray[i];
				}
			}
		}
		//可能还会去物品栏里面找技能
		return null;
	}
	public long GetLockTargetId()
	{
		boolean bIfExist = true;
		SSGameUnit go = this.battle.GetGameObjectById(this.lockedTargetId);
		if (go == null || go.bExpire || !go.IfCanBeTarget())
		{
			bIfExist = false;
		}
		if (bIfExist == false)
		{
			SSGameUnit target = null;
			float dist = 30000000;
			for (SSGameUnit unit : this.battle.gameObjectMap.values())
			{
				if (unit.bExpire == false && GetCurPos().CanWatch(30, unit.GetCurPos()))
				{
					float tempDist = GetCurPos().GetWatchDistSqr(unit.GetCurPos());
					if (tempDist < dist)
					{
						dist = tempDist;
						target = unit;
					}
				}
			}
			if (target != null)
			{
				this.lockedTargetId = target.id;
			}
		}
		return this.lockedTargetId;
	}
	public void SyncSkillState(int skillId)
	{
		if (skillId != 0)
		{
			for (int i=0;i<this.skillArray.length;i++)
			{
				if (this.skillArray[i].skillConfig != null && this.skillArray[i].skillConfig.skillId == skillId)
				{
					this.SyncSkillState(this.skillArray[i],i);
					break;
				}
			}
		}
	}
	private void SyncSkillState(SSSkill skill,int skillSlotIndex)
	{
		if (skill == null)
		{
			return;
		}
		if (player == null)
		{
			return;
		}
		ResSkillInfoChangeMessage message = new ResSkillInfoChangeMessage();
		message.playerId = this.id;
		message.skillSlotIndex = skillSlotIndex;
		message.skillId = skill.skillConfig.skillId;
		message.coolTime = skill.skillConfig.cooldownTime;
		int tDiff = (int)(skill.cooldownTime - System.currentTimeMillis());
		if (tDiff < 0)
		{
			System.err.println("Skill.Time < 0");
			tDiff = 0;
		}
		message.time = tDiff;
		MessageUtil.tell_player_message(this.player.player, message);
	}
	@Override
	public int OnHeartBeat(long now, long tick)
	{
		OnGameUnitHeartBeat(now, tick);
		return 0;
	}
	public void OnGameUnitHeartBeat(long now,long tick)
	{
		if (ai != null)
		{
			ai.HeartBeat(now, tick);
		}
	}
	
}
