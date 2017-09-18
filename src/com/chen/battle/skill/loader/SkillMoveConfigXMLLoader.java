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

import com.chen.battle.skill.config.SkillModelMoveConfig;
import com.chen.battle.skill.structs.ESkillEffectMoveToTargetType;
import com.chen.battle.skill.structs.ESkillEffectMoveType;
import com.chen.battle.skill.structs.ESkillEffectMovedTargetType;
import com.chen.battle.skill.structs.ESkillModelTargetType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;


public class SkillMoveConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(SkillMoveConfigXMLLoader.class);
	public Map<Integer, SkillModelMoveConfig> skillModelMoveConfig = new HashMap<>();
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
						SkillModelMoveConfig config = new SkillModelMoveConfig();
						Vector<NextSkillEffectConfig> nextSkillEffectConfigs = new Vector<>();
						Vector<NextSkillEffectConfig> impactEvents = new Vector<>();
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
							else if("releaseTimeDelay".equals(schilds.item(j).getNodeName()))
							{
								config.releaseTimeDelay = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("eTargetType".equals(schilds.item(j).getNodeName()))
							{
								config.eTargetType = ESkillModelTargetType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];	
							}
							else if("bIsPenetrate".equals(schilds.item(j).getNodeName()))
							{
								config.bIsPenetrate = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if("bIsImpact".equals(schilds.item(j).getNodeName()))
							{
								config.bIsImpact = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if ("bIsCanBreak".equals(schilds.item(j).getNodeName()))
							{
								config.bIsCanBreak = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());						
							}
							else if("eMoveType".equals(schilds.item(j).getNodeName()))
							{
								config.eMoveType = ESkillEffectMoveType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("eMovedTargetType".equals(schilds.item(j).getNodeName()))
							{
								config.eMovedTargetType = ESkillEffectMovedTargetType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("eMoveToTargetType".equals(schilds.item(j).getNodeName()))
							{
								config.eMoveToTargetType = ESkillEffectMoveToTargetType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("angle".equals(schilds.item(j).getNodeName()))
							{
								config.angle = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("speed".equals(schilds.item(j).getNodeName()))
							{
								config.speed = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("distance".equals(schilds.item(j).getNodeName()))
							{
								config.distance = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("eventId".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								String[] nextConfigString = content.split(";");
								for (String value : nextConfigString)
								{
									NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
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
									nextSkillEffectConfigs.add(eConfig);
								}

							}
							else if("impactEvents".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								String[] nextConfigString = content.split(";");
								for (String value : nextConfigString)
								{
									NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
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
									impactEvents.add(eConfig);
								}
								
							}
						}
						for (int j=0;j<nextSkillEffectConfigs.size();j++)
						{
							config.skillModelList[j] = nextSkillEffectConfigs.get(j);
						}
						for (int j=0;j<impactEvents.size();j++)
						{
							config.impactEvents[j] = impactEvents.get(j);
						}
						this.skillModelMoveConfig.put(config.skillModelId, config);
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
