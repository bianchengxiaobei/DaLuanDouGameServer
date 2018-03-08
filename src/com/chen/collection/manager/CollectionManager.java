package com.chen.collection.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.collection.config.CollectionConfig;
import com.chen.collection.config.DailySignConfig;
import com.chen.collection.structs.CollectionItem;
import com.chen.data.manager.DataManager;
import com.chen.db.bean.Collection;
import com.chen.db.dao.CollectionDao;
import com.chen.player.manager.PlayerManager;
import com.chen.player.structs.Player;

public class CollectionManager 
{
	private static Logger logger = LogManager.getLogger(CollectionManager.class);
	
	public Map<Integer, CollectionItem> collectionItems = new HashMap<>();

	private Player theOwner;
	
	private CollectionDao dao;
	public CollectionManager(Player player)
	{
		this.theOwner = player;
	}
	public void Init()
	{
		dao = PlayerManager.getInstance().collectionDao;
		List<Collection> collections = dao.selectById(theOwner.getId());
		if (collections != null)
		{
			for (Collection collection : collections)
			{
				int id = collection.getCollectionId();
				CollectionItem item = new CollectionItem(id,collection.getNum(),collection.getLevel());
				this.collectionItems.put(id, item);
			}
		}
	}
	/**
	 * 更新日常奖励并更新数据库
	 */
	public boolean UpdateDaliyAward()
	{
		try 
		{
			DailySignConfig config = GetCurDayAwardCollectionConfig();
			if (config == null || config.awardId <= 0)
			{
				logger.error("每日签到配置文件找不到:"+theOwner.getDailyCount());
				return false;
			}
			if (HasColletionItem(config.awardId))
			{
				//更新数量,判断是否可以解锁
				CollectionItem item = this.collectionItems.get(config.awardId);
				item.num += config.num; 
				CollectionConfig collectionConfig = this.GetCollectionConfig(config.awardId);
				int max = 0;
				if (collectionConfig != null)
				{
					max = item.level * item.level * collectionConfig.rate + collectionConfig.addRate * (1 + item.level);
				}								
				if (item.num >= max)
				{
					//item.canLocked = true;
					item.level++;
					//发送给客户端通知可解锁
					logger.debug("角色解锁此碎片:"+config.awardId);
				}
				if (dao != null)
				{
					Collection collection = new Collection();
					collection.setRoleId(theOwner.getId());
					collection.setNum(item.num);
					collection.setLevel(item.level);
					collection.setCollectionId(config.awardId);
					dao.update(collection);
				}
			}
			else
			{
				CollectionItem item = new CollectionItem(config.awardId, config.num,0);
				//加入，更新数据库
				this.collectionItems.put(config.awardId, item);
				if (dao != null)
				{
					Collection collection = new Collection();
					collection.setRoleId(theOwner.getId());
					collection.setNum(item.num);
					collection.setLevel(item.level);
					collection.setCollectionId(config.awardId);
					dao.insert(collection);
				}
			}
			return true;
		} catch (Exception e) 
		{		
			logger.error(e,e);
			return false;
		}
	}
	/**
	 * 是否拥有该Collection
	 * @param collectId
	 * @return
	 */
	public boolean HasColletionItem(int collectId)
	{
		if (collectionItems != null && collectionItems.size() > 0)
		{
			return collectionItems.containsKey(collectId);
		}
		else
		{
			return false;
		}
	}
	private DailySignConfig GetCurDayAwardCollectionConfig()
	{
		DailySignConfig config = DataManager.getInstance().dailySignConfigXMLLoader.dailySignConfig.get(theOwner.getDailyCount());
		if (config != null)
		{
			return config;
		}	
		return null;
	}
	private CollectionConfig GetCollectionConfig(int collectionId)
	{
		CollectionConfig config = DataManager.getInstance().collectionConfigXMLLoader.collectionConfig.get(collectionId);
		if (config != null)
		{
			return config;
		}
		return null;
	}
}
