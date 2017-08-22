package com.chen.data.manager;


import com.chen.data.container.MapContainer;

/**
 * 数据加载类
 * @author chen
 *
 */
public class DataManager 
{
	public MapContainer mapContainer = new MapContainer();
	private static Object obj = new Object();
	private static DataManager manager;
	private DataManager()
	{
		mapContainer.load();
	}
	public static DataManager getInstance()
	{
		synchronized (obj)
		{
			if (manager == null)
			{
				manager = new DataManager();
			}
		}
		return manager;
	}
}
