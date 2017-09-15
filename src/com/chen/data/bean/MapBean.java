package com.chen.data.bean;

import java.util.Vector;

public class MapBean 
{
	//地图中文名
	private String m_strMapName;
	//地图编号
	private int m_nMapId;
	//该地图最多玩家数量
	private int maxCount;
	//有多少个阵营
	private String playerModel;
	public Vector<Integer> playerModels = new Vector<>();
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
	public String getPlayerModel() {
		return playerModel;
	}
	public void setPlayerModel(String playerModel) {
		this.playerModel = playerModel;
	}
	
	public Vector<Integer> getPlayerModels()
	{
		if (this.playerModels.size() == 0)
		{
			if (this.playerModel.equals("") || this.playerModel == null)
			{
				System.err.println("地图加载Model错误");
				return null;
			}
			String[] models = this.playerModel.split(",");
			for (String num : models)
			{
				try {
					this.playerModels.add(Integer.valueOf(num));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

			}		
		}
		return this.playerModels;
	}
}
