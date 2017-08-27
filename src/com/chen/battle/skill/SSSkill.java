package com.chen.battle.skill;

import java.util.Arrays;

import org.apache.ibatis.annotations.Case;

import com.chen.battle.skill.config.SSSkillConfig;
import com.chen.battle.skill.structs.ESkillReleaseWay;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.battle.skill.structs.ESkillTargetType;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;

public class SSSkill 
{
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
			//pHero->SyncSkillStateToGC(cpsCfg->un32SkillID);
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
		return true;
	}
	private int CheckAndSetTarget()
	{
		if (this.skillConfig.eReleaseWay == ESkillReleaseWay.No_Target)
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
		if (target != null && target.IsDead())
		{
			return 0;
		}
		if (theOwner.IfInReleaseSkillRange(target, this.skillConfig, 0) == false)
		{
			//距离不够
			return 2;
		}
		return 1;
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
				rst = 0;
			}
			if (eSkillState.value <= ESkillState.Releasing.value && theOwner.IfInReleaseSkillRange(target, skillConfig, 1000) == false)
			{
				rst = 2;//NUllPointer
				break;
			}
			//等待 状态
			if (eSkillState == ESkillState.Free)
			{
				eSkillState = ESkillState.Preparing;
				stateTime = now;
				SetSkillDir();
			}
			//准备 状态
			if (eSkillState == ESkillState.Preparing)
			{
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
				int releaseTime = this.skillConfig.releaseTime;
				if (this.skillConfig.bIsNormalAttack)
				{
					
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
				System.out.println("33333");
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
							bIfUsing = true;
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
				System.out.println("111111");
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
				System.out.println("222222");
				rst = 3;
				End();
				break;
			}
		}
		while(false);
		//如果状态改变了，则需要同步到对象身上
		if (heartBeatStartState != eSkillState.value)
		{
			System.out.println("通知就开始");
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
		case Auto:
			if (target != null)
			{
				dir = target.GetCurDir();
				dir.y = 0;
				dir.normalized();
				theOwner.curActionInfo.dir = dir;
			}
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
	}
	public void Clear()
	{
		this.skillConfig = null;
		bIsRunning = false;
		eSkillState = ESkillState.Free;
		stateTime = 9;
		cooldownTime = 0;
		target = null;
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
}
