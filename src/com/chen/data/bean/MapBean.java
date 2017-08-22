package com.chen.data.bean;

public class MapBean 
{
	//地图中文名
	private String m_strMapName;
	//地图编号
	private int m_nMapId;
	//该地图最多玩家数量
	private int maxCount;
	public String getM_strMapName() {
		return m_strMapName;
	}
	public void setM_strMapName(String m_strMapName) {
		this.m_strMapName = m_strMapName;
	}
	public int getM_nMapId() {
		return m_nMapId;
	}
	public void setM_nMapId(int m_nMapId) {
		this.m_nMapId = m_nMapId;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
}
