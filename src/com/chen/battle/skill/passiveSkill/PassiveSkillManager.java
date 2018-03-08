package com.chen.battle.skill.passiveSkill;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.chen.battle.structs.BattleContext;
import com.chen.battle.structs.SSGameUnit;
import com.chen.data.manager.DataManager;

public class PassiveSkillManager
{
	public BattleContext battleContext;
	public Map<Integer, PassiveSkill> passiveSkills = new HashMap<>();
	public Vector<Integer> delayDeleteVec = new Vector<>();
	public int AddPassiveSkill(SSGameUnit theOwner,int passiveSkillId)
	{
		PassiveSkill skill = this.CreatePassiveSkill(passiveSkillId);
		if (skill == null)
		{
			return 0;
		}
		skill.theOwner = theOwner;
		skill.id = theOwner.battle.GenerateEffectId();
		skill.lastEffectTime = battleContext.battleHeartBeatTime - skill.config.Cooldown;
		if (skill.config.passiveSkillTriggerType != null)
		{
			for (int i=0;i<skill.config.passiveSkillTriggerType.length;i++)
			{
				EPassiveSkillTriggerType type = EPassiveSkillTriggerType.values()[skill.config.passiveSkillTriggerType[i]];
				if (type != EPassiveSkillTriggerType.EPassiveSkillTriggerType_None)
				{
					theOwner.AddPassiveSkill(type, skill.id);
				}
			}
		}
		skill.Begin();
		this.passiveSkills.put(skill.id, skill);
		return skill.id;
	}
	public void RemovePassiveSkill(int passiveSkillId)
	{
		this.delayDeleteVec.addElement(passiveSkillId);
	}
	public PassiveSkill CreatePassiveSkill(int passiveSkillId)
	{
		PassiveSkillConfig config = DataManager.getInstance().passiveSkillConfigXMLLoader.passiveSkillConfig.get(passiveSkillId);
		if (config == null)
		{
			return null;
		}
		PassiveSkill passiveSkill = new PassiveSkill(config);
		passiveSkill.battle = battleContext;
		return passiveSkill;
	}
	public void OnHeartBeat(long now,long tick)
	{
		if (this.delayDeleteVec.isEmpty() == false)
		{
			Iterator<Integer> iterator =  this.delayDeleteVec.iterator();
			while (iterator.hasNext())
			{
				int skillId = iterator.next();
				if (this.passiveSkills.containsKey(skillId))
				{
					PassiveSkill skill = this.passiveSkills.get(skillId);
					if (skill.theOwner != null)
					{
						if (skill.config.passiveSkillTriggerType != null)
						{
							for (int i=0;i<skill.config.passiveSkillTriggerType.length;i++)
							{
								EPassiveSkillTriggerType type = EPassiveSkillTriggerType.values()[skill.config.passiveSkillTriggerType[i]];
								if (type != EPassiveSkillTriggerType.EPassiveSkillTriggerType_None)
								{
									skill.theOwner.RemovePassiveSkill(type, skill.id);
								}
							}
						}					
					}
					this.passiveSkills.remove(skillId);
					iterator.remove();
					skill = null;
				}
			}
		}
		if (this.passiveSkills.isEmpty() == false)
		{
			Iterator<Map.Entry<Integer, PassiveSkill>> iterator = this.passiveSkills.entrySet().iterator();
			while (iterator.hasNext())
			{
				PassiveSkill skill = iterator.next().getValue();
				if (skill == null || skill.theOwner == null)
				{
					iterator.remove();
					continue;
				}
				skill.OnHeartBeat(now, tick);
			}
		}
	}
	public void Trigger(int passEffectId,SSGameUnit target,int pa1,int pa2,int pa3)
	{
		PassiveSkill skill = this.GetPassiveSkill(passEffectId);
		if (skill != null)
		{
			skill.Trigger(target, pa1, pa2, pa3);
		}
	}
	public void DisTrigger(int passEffectId)
	{
		PassiveSkill skill = this.GetPassiveSkill(passEffectId);
		if (skill != null)
		{
			skill.DisTrigger();
		}
	}
	public PassiveSkill GetPassiveSkill(int passiveSkillId)
	{
		if (this.passiveSkills.containsKey((passiveSkillId)))
		{
			return this.passiveSkills.get(passiveSkillId);
		}
		return null;
	}
}
