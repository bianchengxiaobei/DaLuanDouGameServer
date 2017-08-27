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

import com.chen.battle.hero.config.SHeroConfig;
import com.chen.battle.skill.config.SSSkillConfig;
import com.chen.battle.skill.structs.ESkillReleaseWay;
import com.chen.battle.skill.structs.ESkillTargetType;
import com.chen.battle.skill.structs.ESkillTriggerWay;
import com.chen.battle.skill.structs.ESkillType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;

public class SkillConfigXMLLoader
{
	private Logger log = LogManager.getLogger(SkillConfigXMLLoader.class);
	public Map<Integer, SSSkillConfig> skillConfigMap = new HashMap<>();
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
						SSSkillConfig config = new SSSkillConfig();
						Vector<NextSkillEffectConfig> nextSkillEffectConfigs = new Vector<>();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("skillId").equals(schilds.item(j).getNodeName()))
							{
								config.skillId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("eSkillType").equals(schilds.item(j).getNodeName()))
							{
								config.eSkillType = ESkillType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("bIsNormalAttack".equals(schilds.item(j).getNodeName()))
							{
								config.bIsNormalAttack = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if(("eReleaseWay").equals(schilds.item(j).getNodeName()))
							{
								config.eReleaseWay = ESkillReleaseWay.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if ("releaseDis".equals(schilds.item(j).getNodeName()))
							{
								config.releaseDis = Float.parseFloat(schilds.item(j).getTextContent().trim());							
							}
							else if("prepareTime".equals(schilds.item(j).getNodeName()))
							{
								config.prepareTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("cooldownTime".equals(schilds.item(j).getNodeName()))
							{
								config.cooldownTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("releaseTime".equals(schilds.item(j).getNodeName()))
							{
								config.releaseTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("lastTime".equals(schilds.item(j).getNodeName()))
							{
								config.lastTime = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("eTriggerWay".equals(schilds.item(j).getNodeName()))
							{
								config.eTriggerWay = ESkillTriggerWay.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("bImpact".equals(schilds.item(j).getNodeName()))
							{
								config.bImpact = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							else if("eSkillTargetType".equals(schilds.item(j).getNodeName()))
							{
								config.eSkillTargetType = ESkillTargetType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("eventId".equals(schilds.item(j).getNodeName()))
							{
								NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
								String content = schilds.item(j).getTextContent().trim();
								if (content.indexOf(":") == -1)
								{
									eConfig.skillEffectId = Integer.parseInt(content);
								}
								nextSkillEffectConfigs.add(eConfig);
							}
						}
						for (int j=0;j<nextSkillEffectConfigs.size();j++)
						{
							config.skillModelList[j] = nextSkillEffectConfigs.get(j);
						}
						System.out.println("Id:"+config.skillId);
						this.skillConfigMap.put(config.skillId, config);
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
