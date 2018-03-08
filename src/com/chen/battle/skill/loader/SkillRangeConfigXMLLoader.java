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

import com.chen.battle.skill.config.SSkillModelRangleConfig;
import com.chen.battle.skill.structs.ESkillAOEType;
import com.chen.battle.skill.structs.ESkillModelTargetType;
import com.chen.battle.skill.structs.ESkillRangeShapeType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;

public class SkillRangeConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(SkillRangeConfigXMLLoader.class);
	public Map<Integer, SSkillModelRangleConfig> skillModelRangeConfig = new HashMap<>();
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
						SSkillModelRangleConfig config = new SSkillModelRangleConfig();
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
							else if("eTargetType".equals(schilds.item(j).getNodeName()))
							{
								config.eTargetType = ESkillModelTargetType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];	
							}
							else if("eSkillAOEType".equals(schilds.item(j).getNodeName()))
							{
								config.eSkillAOEType = ESkillAOEType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("eSkillRangeShapeType".equals(schilds.item(j).getNodeName()))
							{
								config.eSkillRangeShapeType = ESkillRangeShapeType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("rangeIntervalTime".equals(schilds.item(j).getNodeName()))
							{
								config.rangeIntervalTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("rangleTimes".equals(schilds.item(j).getNodeName()))
							{
								config.rangleTimes = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("rangeParam1".equals(schilds.item(j).getNodeName()))
							{
								config.rangeParam1 = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("rangeParam2".equals(schilds.item(j).getNodeName()))
							{
								config.rangeParam2 = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("maxEffectObj".equals(schilds.item(j).getNodeName()))
							{
								config.maxEffectObj = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("lifeTime".equals(schilds.item(j).getNodeName()))
							{
								config.lifeTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("releaseDist".equals(schilds.item(j).getNodeName()))
							{
								config.releaseDist = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("eventId".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									return;
								}
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
						}
						for (int j=0;j<nextSkillEffectConfigs.size();j++)
						{
							config.skillModelList[j] = nextSkillEffectConfigs.get(j);
						}
						this.skillModelRangeConfig.put(config.skillModelId, config);
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
