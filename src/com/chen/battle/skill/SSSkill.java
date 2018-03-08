package com.chen.battle.skill;

import java.util.Arrays;

import org.apache.ibatis.annotations.Case;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.battle.skill.config.SSSkillConfig;
import com.chen.battle.skill.structs.ESkillReleaseWay;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.battle.skill.structs.ESkillTargetType;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.parameter.structs.EParameterCate;

public class SSSkill 
{
	private Logger logger = LogManager.getLogger(SSSkill.class);
	public SSSkillConfig skillConfig;
	public int normalAttackReleaseTime;
	public boolean bIsRunning;
	public boolean bIfCanCooldown;//是否有cd
	public SSGameUnit theOwner;
	public SSGameUnit target;
	public CVector3D targetPos = new CVector3D(0, 0, 0);
	public CVector3D dir;
	public ESkillState eSkillState;
	public long stateTime;//状态切换时间
	public long cooldownTime;//技能冷却时间
	public long beginTime;//技能开始时间
	public int[] usingEffectArray = new int[32];
	public int[] passitiveSkillArray = new int[16];
	/**
	 * 技能是否满足条件，可以使用
	 * @return
	 */
	public boolean IfSkillUsable()
	{
		boolean status = CheckStatus();
		if (status == false)
		{
			return false;
		}
		//检查cd
		status = IfInCd();
		if (status)
		{
			return false;
		}
		return true;
	}
	/**
	 * 检查技能是否在释放之前
	 * @return
	 */
	public boolean IfSkillBeforeRelease()
	{
		return eSkillState.value <= ESkillState.Releasing.value;
	}
	public void DoCooldown()
	{
		stateTime = System.currentTimeMillis();
		//CD需要将普通攻击和技能攻击分开计算
		if (this.skillConfig.bIsNormalAttack)
		{
			int attackSpeed = theOwner.GetFPData(EParameterCate.AttackSpeed);
			int releaseTime = (int)(this.skillConfig.releaseTime * attackSpeed * 0.001f);
			int standCooldownTime = (int)((this.skillConfig.cooldownTime + this.skillConfig.releaseTime)*attackSpeed*0.001f);
			if (cooldownTime + releaseTime + 150 >= stateTime)
			{				
				cooldownTime += standCooldownTime;
				//System.err.println("1:"+cooldownTime);
			}
			else
			{
				cooldownTime = beginTime + standCooldownTime;
				//System.err.println("2:"+cooldownTime);
			}
		}
		//如果是普通技能，直接从技能放出来的时间加上CD即可
		else
		{

			//这里可能还有技能冷却
			this.cooldownTime = stateTime + skillConfig.cooldownTime;
			//tCooldownMilsec = tStateMilsec + ( cpsCfg->n32CoolDown * (1.0f - pcMasterGU->GetFPData(eEffectCate_CooldownReduce) / 1000.0f) );
		}
		//通知客户端技能信息改变
		SSHero hero = (SSHero)theOwner;
		if (hero != null)
		{
			hero.SyncSkillState(this.skillConfig.skillId);
		}
	}
	/**
	 * 是否是瞬发技能，没有引导过程
	 * @return
	 */
	public boolean IfImpactSkill()
	{
		return IfHasPreTime() == false && this.skillConfig.bImpact == true;
	}
	/**
	 * 是否技能有预备时间
	 * @return
	 */
	public boolean IfHasPreTime()
	{
		return this.skillConfig.prepareTime > 0 || this.skillConfig.releaseTime > 0;
	}
	private boolean CheckStatus()
	{
		if (theOwner.curActionInfo.eOAS.value > EGOActionState.PassiveState.value)
		{
			return false;
		}
		//是否在沉默、眩晕等等状态
		if (theOwner.GetFPData(EParameterCate.Dizziness) > 0)
		{
			return false;
		}
		if (theOwner.GetFPData(EParameterCate.Silence) > 0)
		{
			return false;
		}		
		return true;
	}
	private int CheckAndSetTarget()
	{
		if (this.skillConfig.eReleaseWay == ESkillReleaseWay.No_Target_Pos || this.skillConfig.eReleaseWay == ESkillReleaseWay.Auto)
		{
			this.target = null;
		}
		else
		{
			SSHero hero = (SSHero)theOwner;
			this.target = hero.battle.GetGameObjectById(hero.GetLockTargetId());
			if (target == null)
			{
				return 0;
			}
		}
		//判断英雄是否有该技能
		return 1;
	}
	/**
	 * 是否在cd状态
	 * @return
	 */
	public boolean IfInCd()
	{
		if (System.currentTimeMillis() < cooldownTime)
		{
			return true;
		}
		return false;
	}
	public int IfSkillUsableWithNowTarget()
	{
		boolean statu = this.CheckStatus();
		if (statu == false)
		{
			return 0;
		}
		int rst = CheckAndSetTarget();
		if (rst != 1)
		{
			return rst;
		}
		if (target != null && (target.IsDead() || target.curActionInfo.eOAS == EGOActionState.Reliving))
		{
			return 0;
		}
		if (theOwner.IfInReleaseSkillRange(target, this.skillConfig, 0) == false)
		{
			//距离不够
			return 2;
		}
		return 1;//normal
	}
	/**
	 * 开始技能
	 */
	public void Start()
	{
		boolean value = IfSkillUsable();
		if (value == false)
		{
			return;
		}
		eSkillState = ESkillState.Free;
		beginTime = System.currentTimeMillis();
		bIsRunning = true;
		if (target != null)
		{
			targetPos = target.GetCurPos();
		}
		this.CheckAndDoInstanceSkill();
	}
	
	public int HeartBeat(long now,long tick)
	{
		//ESkillState heartBeatStartState = eSkillState;
		int heartBeatStartState = eSkillState.value;
		int rst = 1;//normal
		do
		{
			if (CheckStatus() == false)
			{
				logger.error("状态不行");
				rst = 0;
				break;
			}
			if (eSkillState.value <= ESkillState.Releasing.value && theOwner.IfInReleaseSkillRange(target, skillConfig, 1) == false)
			{
				logger.error("距离不够，取消释放技能");
				rst = 2;//NUllPointer
				break;
			}
			//等待 状态
			if (eSkillState == ESkillState.Free)
			{
				//System.out.println("Free");
				eSkillState = ESkillState.Preparing;
				stateTime = now;
				SetSkillDir();
			}
			//准备 状态
			if (eSkillState == ESkillState.Preparing)
			{
				//System.out.println("Preparing");
				long deltaTime = now - stateTime;
				//如果需要等待,直接返回
				if (deltaTime < this.skillConfig.prepareTime)
				{
					rst = 1;
					break;
				}
				//如果不需要等待就进入技能的释放前摇
				eSkillState = ESkillState.Releasing;
				stateTime = now;
			}
			//技能前摇
			if (eSkillState == ESkillState.Releasing)
			{
				//System.out.println("Releasing");
				int releaseTime = this.skillConfig.releaseTime;
				//理应加上角色的攻击速度
				if (this.skillConfig.bIsNormalAttack)
				{
					if (normalAttackReleaseTime == 0)
					{
						normalAttackReleaseTime = (int)(releaseTime * theOwner.GetFPData(EParameterCate.AttackSpeed) * 0.001f);
					}
					releaseTime = normalAttackReleaseTime;
				}
				long span = now - stateTime;
				if (span < releaseTime)
				{
					rst = 1;
					break;
				}
				MakeSkillEffect(now);
				eSkillState = ESkillState.Using;
				stateTime = now;
				normalAttackReleaseTime = 0;
			}
			//使用 状态
			if (eSkillState == ESkillState.Using)
			{
				//System.out.println("Using");
				boolean bIfUsing = false;
				//遍历所有的使用中的技能效果,看其是否依然在占用中
				for (int i=0; i<32; i++)
				{
					int effectId = this.usingEffectArray[i];
					if (effectId != 0)
					{
						SSSkillEffect effect = theOwner.battle.effectManager.GetEffect(effectId);
						if (effect != null)
						{
							if (effect.IsUsingSkill() == true)
							{
								bIfUsing = true;
							}
						}
						else
						{
							this.usingEffectArray[i] = 0;
						}
					}
				}
				//
				if (bIfUsing == false)
				{
					ClearUsingEffects();
					eSkillState = ESkillState.Lasting;
					stateTime = now;
				}
			}
			//后摇状态
			if (eSkillState == ESkillState.Lasting)
			{
				//System.out.println("Lasting");
				if (stateTime + skillConfig.lastTime > now)
				{
					rst = 1;
					break;
				}
				eSkillState = ESkillState.End;
				stateTime = now;
			}
			//结束状态
			if (eSkillState == ESkillState.End)
			{
				rst = 3;
				End();
				break;
			}
		}
		while(false);
		//如果状态改变了，则需要同步到对象身上
		if (heartBeatStartState != eSkillState.value)
		{
			if (eSkillState == ESkillState.Preparing)
			{
				theOwner.BeginActionPrepareSkill(this, dir, true);
			}
			else if(eSkillState == ESkillState.Releasing)
			{
				theOwner.BeginActionReleaseSkill(this, dir, true);
			}
			else if(eSkillState == ESkillState.Using)
			{
				theOwner.BeginActionUsingSkill(this, dir, true);
			}
			else if (eSkillState == ESkillState.Lasting)
			{
				theOwner.BeginActionLastingSkill(this, dir, true);
			}
		}
		return rst;
	}
	
	
	public void CheckAndDoInstanceSkill()
	{
		if (IfImpactSkill())
		{
			dir = theOwner.GetCurDir();
			this.MakeSkillEffect(this.beginTime);
			if (this.skillConfig.lastTime <= 0)
			{
				this.End();
			}
			else
			{
				eSkillState = ESkillState.Using;
				this.stateTime = System.currentTimeMillis();
			}
		}
	}
	/**
	 * 设置技能方向
	 */
	public void SetSkillDir()
	{
		this.dir = theOwner.GetCurDir();
		switch (this.skillConfig.eReleaseWay) {
		case Need_Target:
			if (target != null)
			{
				dir = CVector3D.Sub(target.GetCurPos(), theOwner.GetCurPos());
				dir.y = 0;
				dir.normalized();//引用
				theOwner.curActionInfo.dir = dir;
			}
			break;
		case No_Target_Pos:
			if (theOwner.curActionInfo.skillParams != null)
			{
				this.dir = CVector3D.Sub(theOwner.curActionInfo.skillParams, theOwner.GetCurPos());
				theOwner.curActionInfo.dir = this.dir;
			}
			break;
		case No_Target_Dir:
			if (theOwner.curActionInfo.skillParams != null)
			{
				//this.dir = CVector3D.Sub(theOwner.curActionInfo.skillParams, theOwner.GetCurPos());
				this.dir = theOwner.curActionInfo.skillParams.clone();
				theOwner.curActionInfo.dir = this.dir;
			}
			break;
		case Auto:
			this.dir = theOwner.GetCurDir();
			break;
		}
	}
	public void MakeSkillEffect(long now)
	{
		CVector3D pos = theOwner.GetCurPos();
		if (target != null)
		{
			pos = target.GetCurPos();
		}
		if (this.skillConfig.eSkillTargetType == ESkillTargetType.Self)
		{
			target = theOwner;
		}
		//调用被动技能
//		if(TRUE == cpsCfg->bIfNomalAttack)
//		{
//			pcMasterGU->OnPassitiveSkillCalled(EPassiveSkillTriggerType_Attack, pcTarGU);
//		}
//		//调用使用技能前的被动技能
//		if(FALSE == cpsCfg->bIfNomalAttack)
//		{
//			pcMasterGU->OnPassitiveSkillCalled(EPassiveSkillTriggerType_UseSkill, pcTarGU);
//		}
		BattleContext battle = theOwner.battle;
		battle.effectManager.AddEffectsFromConfig(skillConfig.skillModelList, theOwner, target, pos, theOwner.GetCurDir(), this, now, null);
	}
	/**
	 * 取消技能
	 * @return
	 */
	public boolean TryCancle()
	{
		boolean ifCanCancle = false;
		if (eSkillState.value <= ESkillState.Releasing.value)
		{
			ifCanCancle = true;
		}
		//当引导状态时，需要依次询问模块时候可以取消
		else if(eSkillState == ESkillState.Using)
		{
			boolean ifCanStopUsing  = true;
			for (int i=0;i<32;i++)
			{
				int id = this.usingEffectArray[i];
				if (id != 0)
				{
					SSSkillEffect effect = theOwner.battle.effectManager.GetEffect(id);
					if (effect != null)
					{
						if (effect.IsCanStopUsing() == false)
						{
							ifCanStopUsing = false;
							break;
						}
					}				
				}
			}
			if (ifCanStopUsing)
			{
				ifCanCancle = true;
			}
		}
		//当后摇阶段的时候，可以取消
		else if(eSkillState == ESkillState.Lasting)
		{
			ifCanCancle = true;
		}
		if (ifCanCancle)
		{
			logger.debug("成功取消技能："+this.skillConfig.skillId);
			if (eSkillState == ESkillState.Using)
			{
				for (int i=0;i<32;i++)
				{
					int id = this.usingEffectArray[i];
					if (id != 0)
					{
						SSSkillEffect effect = theOwner.battle.effectManager.GetEffect(id);
						if (effect != null)
						{
							effect.ForceStop();
						}				
					}
				}
				this.ClearUsingEffects();
			}
			End();
			return true;
		}
		else
		{
			return false;
		}
	}
	public void End()
	{
		bIsRunning = false;
		eSkillState = ESkillState.End;		
		if (!this.IfImpactSkill())
		{
			theOwner.ai.bIsStandAttack = false;
		}
	}
	public void Clear()
	{
		this.skillConfig = null;
		bIsRunning = false;
		eSkillState = ESkillState.Free;
		stateTime = 9;
		cooldownTime = 0;
		target = null;
		bIfCanCooldown = true;
		beginTime = 0;
		normalAttackReleaseTime = 0;
		targetPos.zero();
		ClearUsingEffects();
		ClearPassSkill();
	}
	private void ClearUsingEffects()
	{
		Arrays.fill(usingEffectArray, 0);
	}
	private void ClearPassSkill()
	{
		Arrays.fill(passitiveSkillArray, 0);
	}
	/**
	 * 是否在该阶段能否移动
	 * @return
	 */
	public boolean IfMasterMoveable()
	{
		//如果不是引导阶段，那么可以移动
		if (eSkillState != ESkillState.Using)
		{
			return true;
		}
		else if(eSkillState == ESkillState.Using)
		{
			for (int i=0;i<32;i++)
			{
				int effectId = usingEffectArray[i];
				if (effectId == 0)
				{
					continue;
				}
				SSSkillEffect effect = theOwner.battle.effectManager.GetEffect(effectId);
				if (effect != null)
				{
					if (effect.IfCanMove() == false)
					{
						return false;
					}
				}
			}
		}
		return true;
	}
}
