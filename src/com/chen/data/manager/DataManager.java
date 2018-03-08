package com.chen.data.manager;



import com.chen.battle.ball.BallBornPosConfig;
import com.chen.battle.hero.loader.SSHeroConfigXMLLoader;
import com.chen.battle.map.structs.SSHeroBornConfigXMLLoader;
import com.chen.battle.skill.loader.SkillBuffConfigXMLLoader;
import com.chen.battle.skill.loader.SkillCaculateConfigXMLLoader;
import com.chen.battle.skill.loader.SkillConfigXMLLoader;
import com.chen.battle.skill.loader.SkillEffectTypeConfigXmlLoader;
import com.chen.battle.skill.loader.SkillFlyConfigXMLLoader;
import com.chen.battle.skill.loader.SkillMoveConfigXMLLoader;
import com.chen.battle.skill.loader.SkillRangeConfigXMLLoader;
import com.chen.battle.skill.passiveSkill.PassiveSkillConfigXMLLoader;
import com.chen.collection.config.CollectionConfigXMLLoader;
import com.chen.collection.config.DailySignConfigXMLLoader;
import com.chen.data.container.MapContainer;
import com.chen.map.MapConfigLoader;
import com.chen.player.config.PlayerExpConfigXMLLoader;

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
	
	public SkillCaculateConfigXMLLoader skillModelCacuConfigLoader = new SkillCaculateConfigXMLLoader();
	public static final String skillModelCaculateConfigPath = "server-config/skillModelCaculateConfig.xml";
	
	public SkillBuffConfigXMLLoader skillBuffConfigXMLLoader = new SkillBuffConfigXMLLoader();
	public static final String skillBuffConfigPath = "server-config/skillModelBuffConfig.xml";
	
	public SSHeroBornConfigXMLLoader heroBornConfigXMLLoader = new SSHeroBornConfigXMLLoader();
	public static final String heroBornConfigPath = "server-config/heroBornPosConfig.xml";
	
	public DailySignConfigXMLLoader dailySignConfigXMLLoader = new DailySignConfigXMLLoader();
	private final static String dailySignConfigPath = "server-config/dailySignAward.xml";
	
	public CollectionConfigXMLLoader collectionConfigXMLLoader = new CollectionConfigXMLLoader();
	private final static String collectionConfigPath = "server-config/collectionItemConfig.xml";
	
	public MapConfigLoader mapConfigLoader = new MapConfigLoader();
	private final static String mapConfigPath = "server-config/mapConfig.map";
	
	public PlayerExpConfigXMLLoader playerExpConfigXMLLoader = new PlayerExpConfigXMLLoader();
	private final static String playerExpConfigPath = "server-config/playerLevelToExpConfig.xml";
	
	public PassiveSkillConfigXMLLoader passiveSkillConfigXMLLoader = new PassiveSkillConfigXMLLoader();
	private final static String passiveSkillConfigPath = "server-config/passiveSkillConfig.xml";
	
	public BallBornPosConfig ballBornPosConfig = new BallBornPosConfig();
	
	private static Object obj = new Object();
	private static DataManager manager;
	private DataManager()
	{
		
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
	public void Init()
	{
		mapContainer.load();
		heroConfigXMLLoader.load(heroConfigPath);
		skillConfigXMLLoader.load(skillConfigPath);
		skillTypeConfigLoader.load(skillEffectTypeConfigPath);
		skillModelMoveConfigLoader.load(skillModelMoveConfigPath);
		skillModelRangeConfigLoader.load(skillModelRangeConfigPath);
		skillModelFlyConfigLoader.load(skillModelFlyConfigPath);
		skillModelCacuConfigLoader.load(skillModelCaculateConfigPath);
		skillBuffConfigXMLLoader.load(skillBuffConfigPath);
		heroBornConfigXMLLoader.load(heroBornConfigPath);
		dailySignConfigXMLLoader.load(dailySignConfigPath);
		collectionConfigXMLLoader.load(collectionConfigPath);
		mapConfigLoader.load(mapConfigPath);
		playerExpConfigXMLLoader.Load(playerExpConfigPath);
		passiveSkillConfigXMLLoader.load(passiveSkillConfigPath);
		ballBornPosConfig.Init();
	}
}
