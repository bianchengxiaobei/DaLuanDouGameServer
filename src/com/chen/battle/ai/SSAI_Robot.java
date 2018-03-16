package com.chen.battle.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.chen.battle.ai.config.RobotAIConfig;
import com.chen.battle.ai.structs.EAIRobotType;
import com.chen.battle.bt.BTActionNode;
import com.chen.battle.bt.BTNode;
import com.chen.battle.bt.BTSelectorNode;
import com.chen.battle.bt.BTSequenceNode;
import com.chen.battle.bt.EBTNodeType;
import com.chen.battle.bt.Action.MoveByDirActionNode;
import com.chen.battle.bt.Action.NormalAttackActionNode;
import com.chen.battle.bt.Action.UseSkillActionNode;
import com.chen.battle.bt.Action.WaitTimeActionNode;
import com.chen.battle.bt.Condition.CheckSkillInDistConditionNode;
import com.chen.battle.bt.Condition.CheckSkillTypeConditionNode;
import com.chen.battle.bt.Condition.HasFriendHeroBeHurtedConditionNode;
import com.chen.battle.bt.Condition.HasPrepareSkillConditionNode;
import com.chen.battle.bt.Condition.HasSkillInCDConditionNode;
import com.chen.battle.bt.Condition.HasTargetConditionNode;
import com.chen.battle.bt.Condition.InAttackConditionNode;
import com.chen.battle.bt.Condition.InAutoAttackingConditionNode;
import com.chen.battle.bt.Condition.InControllerConditionNode;
import com.chen.battle.bt.Condition.InDeadConditionNode;
import com.chen.battle.bt.Condition.InHurtedConditionNode;
import com.chen.battle.bt.Condition.InMovingConditionNode;
import com.chen.battle.bt.Condition.InSilenceConditionNode;
import com.chen.battle.bt.Condition.InStartTimeConditionNode;
import com.chen.battle.bt.Condition.InUsingSkillConditionNode;
import com.chen.battle.skill.SSSkill;
import com.chen.battle.structs.SSGameUnit;
import com.chen.battle.structs.SSHero;
import com.chen.data.manager.DataManager;

public class SSAI_Robot extends SSAI_Hero
{
	public SSHero hero;
	public BTNode bTRoot;
	public long lastTimeAction;//最后一次操作时间
	public long waitStartTime;//游戏开始的等待时间
	public BTNode actionNode = null;
	public List<SSSkill> skillList = new ArrayList<>();//英雄技能临时存储空间
	public SSAI_Robot(SSGameUnit _theOwner,EAIRobotType type)
	{
		super(_theOwner);
		if (theOwner != null)
		{
			hero = (SSHero)theOwner;
		}
		//加载行为树和地图节点
		Map<Integer, BTNode> tempNodeMap = new HashMap<Integer, BTNode>();
		for (Entry<Integer, RobotAIConfig> entry : DataManager.getInstance().robotAIConfigLoader.robotAIConfigMap.entrySet())
		{
			RobotAIConfig config = entry.getValue();
			if (config != null)
			{
//				if (config.RobotType != type)
//					continue;
				if (bTRoot == null && config.ParentId == 0)
				{
					bTRoot = CreateBtNode(config.id, config.NodeType, config.NodeType, config.params);
					if (bTRoot != null)
					{
						tempNodeMap.put(bTRoot.GetId(), bTRoot);
					}
					continue;
				}
				if (!tempNodeMap.containsKey(config.ParentId))
				{
					continue;
				}
				BTNode parentNode = tempNodeMap.get(config.ParentId);
				BTNode node = CreateBtNode(config.id, config.NodeType, config.ModelId, config.params);
				if (node == null)
				{
					continue;
				}
				parentNode.AddChild(node);
				tempNodeMap.put(node.GetId(), node);
			}
		}
	}
	
	public BTNode CreateBtNode(int id,int type,int modelId,int[] params)
	{
		if (type == EBTNodeType.Selector.value)
		{
			return new BTSelectorNode(id);
		}
		else if (type == EBTNodeType.Sequence.value)
		{
			return new BTSequenceNode(id);
		}
		else if (type == EBTNodeType.Action.value)
		{
			return this.CreateAction(id, modelId, params);
		}
		else if (type == EBTNodeType.Condition.value)
		{
			return this.CreateCondition(id, modelId, params);
		}
		return null;
	}
	public BTNode CreateCondition(int id,int modelId,int[] params)
	{
		switch (modelId)
		{
		case 80001:
			return new InStartTimeConditionNode(modelId, params);
		case 80002:
			return new InControllerConditionNode(modelId, params);
		case 80003:
			return new InDeadConditionNode(modelId, params);
		case 80004:
			return new InMovingConditionNode(modelId, params);
		case 80005:
			return new InAttackConditionNode(modelId, params);
		case 80006:
			return new InUsingSkillConditionNode(modelId, params);
		case 80007:
			return new InHurtedConditionNode(modelId, params);
		case 80008:
			return new HasTargetConditionNode(modelId, params);
		case 80009:
			return new HasPrepareSkillConditionNode(modelId, params);
		case 80010:
			return new HasSkillInCDConditionNode(modelId, params);
		case 80011:
			return new InSilenceConditionNode(modelId, params);
		case 80012:
			return new CheckSkillTypeConditionNode(modelId, params);
		case 80013:
			return new CheckSkillInDistConditionNode(modelId, params);
		case 80014:
			return new HasFriendHeroBeHurtedConditionNode(modelId, params);
		case 80015:
			return new InAutoAttackingConditionNode(modelId, params);
		}
		return null;
	}
	public BTNode CreateAction(int id,int modelId,int[] params)
	{
		switch (modelId)
		{
		case 90001:
			return new WaitTimeActionNode(modelId, params);
		case 90002:
			return new MoveByDirActionNode(modelId, params);
		case 90003:
			return new UseSkillActionNode(modelId, params);
		case 90004:
			return new NormalAttackActionNode(modelId, params);
		}
		return null;
	}
	@Override
	public void HeartBeat(long now,long tick)
	{
		if (this.bTRoot == null)
		{
			return ;
		}
		//如果死亡，如果长时间无操作,清除所有动作
		if (hero.IsDead() || (lastTimeAction != 0 && now - lastTimeAction > 8000))
		{
			this.ClearAction();
			lastTimeAction = now;
		}
		int actionNum = 0;
		while (true)
		{
			actionNum++;
			if (actionNum >= 3)
			{
				break;
			}
			boolean ifEndHeartBeat = false;
			this.actionNode = null;
			if (bTRoot.Tick(this) == true)
			{
				if (this.actionNode != null)
				{
					BTActionNode actionNode = (BTActionNode)this.actionNode;
					if (actionNode.Action(this) == true)
					{
						if (actionNode.IfNeedTime(this))
						{
							ifEndHeartBeat = true;
						}
					}
				}
			}
			if (ifEndHeartBeat)
			{
				break;
			}
		}
		super.HeartBeat(now, tick);
	}
	public void ClearAction()
	{
		if (this.nowSkill != null)
		{
			this.nowSkill.TryCancle();
		}
		StopWantUseSkill(true);
		StopAttack();
		bIsMoveDir = false;
		if (bIsMoving)
		{
			hero.battle.AskStopMoveTarget(hero);
		}
	}
	public SSSkill GetPrepareSkill()
	{
		return skillList.isEmpty() ? null : skillList.get(0);
	}
}
