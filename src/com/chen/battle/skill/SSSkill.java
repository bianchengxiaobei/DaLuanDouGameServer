package com.chen.battle.skill;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.ietf.jgss.Oid;

import com.chen.battle.skill.structs.ESkillReleaseWay;
import com.chen.battle.skill.structs.ESkillState;
import com.chen.battle.skill.structs.ESkillTargetType;
import com.chen.battle.skill.structs.SSSkillConfig;
import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.CVector3D;
import com.chen.battle.structs.EGOActionState;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.config.Config;

public class SSSkill 
{
	public SSSkillConfig skillConfig;
	public int normalAttackReleaseTime;
	public boolean bIsRunning;
	public boolean bIfCanCooldown;//是否有cd
	public SSGameUnit theOwner;
	public SSGameUnit target;
	public CVector3D targetPos;
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
