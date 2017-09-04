package com.chen.battle.skill.loader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.chen.battle.skill.config.SkillModelFlyConfig;
import com.chen.battle.skill.structs.ESkillModelFlyType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;

public class SkillFlyConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(SkillFlyConfigXMLLoader.class);
	public Map<Integer, SkillModelFlyConfig> skillModelFlyConfig = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("skillConfigs");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("skill").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						SkillModelFlyConfig config = new SkillModelFlyConfig();
						Vector<NextSkillEffectConfig> nextSkillEffectConfigs = new Vector<>();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("skillModelId").equals(schilds.item(j).getNodeName()))
							{
								config.skillModelId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("bIsCooldown").equals(schilds.item(j).getNodeName()))
							{
								config.bIsCooldown = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if("bIsCanMove".equals(schilds.item(j).getNodeName()))
							{
								config.bIsCanMove = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if ("bIsCanBreak".equals(schilds.item(j).getNodeName()))
							{
								config.bIsCanBreak = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());						
							}
							else if("releaseTimeDelay".equals(schilds.item(j).getNodeName()))
							{
								config.releaseTimeDelay = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}	
							else if("eSkillModelFlyType".equals(schilds.item(j).getNodeName()))
							{
								config.eSkillModelFlyType = ESkillModelFlyType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}	
							else if("flySpeed".equals(schilds.item(j).getNodeName()))
							{
								config.flySpeed = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("flyParam1".equals(schilds.item(j).getNodeName()))
							{
								config.flyParam1 = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("flyParam2".equals(schilds.item(j).getNodeName()))
							{
								config.flyParam2 = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("flyParam3".equals(schilds.item(j).getNodeName()))
							{
								config.flyParam3 = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("lifeTime".equals(schilds.item(j).getNodeName()))
							{
								config.lifeTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("num".equals(schilds.item(j).getNodeName()))
							{
								config.num = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("angle".equals(schilds.item(j).getNodeName()))
							{
								config.angle = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("bIsPenetrate".equals(schilds.item(j).getNodeName()))
							{
								config.bIsPenetrate = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if("colliderRaius".equals(schilds.item(j).getNodeName()))
							{
								config.colliderRaius = Float.parseFloat(schilds.item(j).getTextContent().trim());
							}
							else if("eventId".equals(schilds.item(j).getNodeName()))
							{
								NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
								String content =  schilds.item(j).getTextContent().trim();
								String[] nextConfigString = content.split(";");
								for (String value : nextConfigString)
								{
									if (value.indexOf(":") == -1)
									{
										eConfig.skillEffectId = Integer.parseInt(value);
									}
									else 
									{
										String[] vStrings = value.split(":");
										eConfig.skillEffectId = Integer.parseInt(vStrings[0]);
										eConfig.delay = Integer.parseInt(vStrings[1]);
									}
								}
								nextSkillEffectConfigs.add(eConfig);
							}
						}
						for (int j=0;j<nextSkillEffectConfigs.size();j++)
						{
							config.skillModelList[j] = nextSkillEffectConfigs.get(j);
						}
						this.skillModelFlyConfig.put(config.skillModelId, config);
					}
				}
				in.close();
			}
		} 
		catch (Exception e)
		{
			log.error(e);
		}
	}
}
