package com.chen.data.manager;



import com.chen.battle.hero.loader.SSHeroConfigXMLLoader;
import com.chen.battle.skill.loader.SkillConfigXMLLoader;
import com.chen.battle.skill.loader.SkillEffectTypeConfigXmlLoader;
import com.chen.battle.skill.loader.SkillFlyConfigXMLLoader;
import com.chen.battle.skill.loader.SkillMoveConfigXMLLoader;
import com.chen.battle.skill.loader.SkillRangeConfigXMLLoader;
import com.chen.data.container.MapContainer;

/**
 * 数据加载类
 * @author chen
 *
 */
public class DataManager 
{
	public MapContainer mapContainer = new MapContainer();
	public SSHeroConfigXMLLoader heroConfigXMLLoader = new SSHeroConfigXMLLoader();
	public static final String heroConfigPath = "server-config/heroConfig.xml";
	
	public SkillConfigXMLLoader skillConfigXMLLoader = new SkillConfigXMLLoader();
	public static final String skillConfigPath = "server-config/skillConfig.xml";
	
	public SkillEffectTypeConfigXmlLoader skillTypeConfigLoader = new SkillEffectTypeConfigXmlLoader();
	public static final String skillEffectTypeConfigPath = "server-config/skillEffectTypeConfig.xml";
	
	public SkillMoveConfigXMLLoader skillModelMoveConfigLoader = new SkillMoveConfigXMLLoader();
	public static final String skillModelMoveConfigPath = "server-config/skillModelMoveConfig.xml";
	
	public SkillRangeConfigXMLLoader skillModelRangeConfigLoader = new SkillRangeConfigXMLLoader();
	public static final String skillModelRangeConfigPath = "server-config/skillModelRangeConfig.xml";
	
	public SkillFlyConfigXMLLoader skillModelFlyConfigLoader = new SkillFlyConfigXMLLoader();
	public static final String skillModelFlyConfigPath = "server-config/skillModelFlyConfig.xml";
	
	private static Object obj = new Object();
	private static DataManager manager;
	private DataManager()
	{
		mapContainer.load();
		heroConfigXMLLoader.load(heroConfigPath);
		skillConfigXMLLoader.load(skillConfigPath);
		skillTypeConfigLoader.load(skillEffectTypeConfigPath);
		skillModelMoveConfigLoader.load(skillModelMoveConfigPath);
		skillModelRangeConfigLoader.load(skillModelRangeConfigPath);
		skillModelFlyConfigLoader.load(skillModelFlyConfigPath);
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
