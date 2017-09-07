package com.chen.battle.structs;

public class PlayerBattleInfo
{
	public EBattleType battleTyoe;
	public EBattleState battleState;
	public long battleId;
	public int battleCampType;
	public int getBattleCampType() {
		return battleCampType;
	}
	public void setBattleCampType(int battleCampType) {
		this.battleCampType = battleCampType;
	}
	public PlayerBattleInfo()
	{
		battleTyoe = EBattleType.eBattleType_Free;
		battleState = EBattleState.eBattleState_Free;
		battleId = 0L;
		battleCampType = 0;
	}
	public EBattleType getBattleTyoe() {
		return battleTyoe;
	}
	public void setBattleTyoe(EBattleType battleTyoe) {
		this.battleTyoe = battleTyoe;
	}
	public EBattleState getBattleState() {
		return battleState;
	}
	public void setBattleState(EBattleState battleState) {
		this.battleState = battleState;
	}
	public long getBattleId() {
		return battleId;
	}
	public void setBattleId(long battleId) {
		this.battleId = battleId;
	}
	public void changeTypeWithState(EBattleType battleType,EBattleState battleState)
	{
		this.battleTyoe = battleType;
		this.battleState = battleState;
	}
	public void changeState(EBattleState state)
	{
		this.battleState = state;
	}
	public void reset()
	{
		battleTyoe = EBattleType.eBattleType_Free;
		battleState = EBattleState.eBattleState_Free;
		battleId = 0L;
	}
}
