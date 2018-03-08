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

import com.chen.battle.skill.config.SkillModelBuffConfig;
import com.chen.battle.skill.structs.BuffEffectInfo;
import com.chen.battle.skill.structs.EBuffReplaceType;
import com.chen.battle.skill.structs.EEffectCaculateType;
import com.chen.battle.skill.structs.ESkillBuffType;
import com.chen.battle.skill.structs.ESkillModelTargetType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;
import com.chen.parameter.structs.EParameterCate;

public class SkillBuffConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(SkillBuffConfigXMLLoader.class);
	public Map<Integer, SkillModelBuffConfig> skillModelBuffConfig = new HashMap<>();
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
						SkillModelBuffConfig config = new SkillModelBuffConfig();
						Vector<NextSkillEffectConfig> nextSkillEffectConfigs = new Vector<>();
						BuffEffectInfo bufInfo = new BuffEffectInfo();
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
							else if("eBuffType".equals(schilds.item(j).getNodeName()))
							{
								config.eBuffType = ESkillBuffType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("eBuffReplaceType".equals(schilds.item(j).getNodeName()))
							{
								config.eBuffReplaceType = EBuffReplaceType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("replaceTimes".equals(schilds.item(j).getNodeName()))
							{
								config.replaceTimes = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("rejectId".equals(schilds.item(j).getNodeName()))
							{
								config.rejectId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("replaceId".equals(schilds.item(j).getNodeName()))
							{
								config.replaceId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("effectLastTick".equals(schilds.item(j).getNodeName()))
							{
								config.effectLastTick = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("effectInterval".equals(schilds.item(j).getNodeName()))
							{
								config.effectInterval = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("bIfClearWhenDead".equals(schilds.item(j).getNodeName()))
							{
								config.bIfClearWhenDead = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}
							//BuffEffectInfo
							else if("eParamType".equals(schilds.item(j).getNodeName()))
							{
								bufInfo.eParamType = EParameterCate.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("effectBaseValue".equals(schilds.item(j).getNodeName()))
							{
								bufInfo.effectBaseValue = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("effectRate".equals(schilds.item(j).getNodeName()))
							{
								bufInfo.effectRate = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}		
							else if("eEffectPlusRate".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									continue;
								}
								String[] nextConfigString = content.split(";");
								int size = nextConfigString.length;
								config.buffEffectInfo.eEffectAddCaculType = new EEffectCaculateType[size];
								config.buffEffectInfo.effectAddCaculValue = new int[size];
								for (int g=0; g<size; g++)
								{
									String[] vStrings = nextConfigString[g].split(":");
									bufInfo.eEffectAddCaculType[i] = EEffectCaculateType.values()[Integer.parseInt(vStrings[0])];
									bufInfo.effectAddCaculValue[i] = Integer.parseInt(vStrings[1]);
								}
							}
							else if("eEffectMultiplyRate".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									continue;
								}
								String[] nextConfigString = content.split(";");
								int size = nextConfigString.length;
								config.buffEffectInfo.eEffectMultCaculType = new EEffectCaculateType[size];
								config.buffEffectInfo.effectMultCaculValue = new int[size];
								for (int g=0; g<size; g++)
								{
									String[] vStrings = nextConfigString[g].split(":");
									bufInfo.eEffectMultCaculType[i] = EEffectCaculateType.values()[Integer.parseInt(vStrings[0])];
									bufInfo.effectMultCaculValue[i] = Integer.parseInt(vStrings[1]);
								}
							}
							//Events
							else if("eventId".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									continue;
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
							else if("startEventId".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									continue;
								}
								String[] nextConfigString = content.split(";");
								for (int g=0; g<nextConfigString.length; g++)
								{
									NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
									if (nextConfigString[g].indexOf(":") == -1)
									{
										eConfig.skillEffectId = Integer.parseInt(nextConfigString[g]);
									}
									else 
									{
										String[] vStrings = nextConfigString[g].split(":");
										eConfig.skillEffectId = Integer.parseInt(vStrings[0]);
										eConfig.delay = Integer.parseInt(vStrings[1]);
									}
									config.skillStartModelList[i] = eConfig;
								}
							}
							else if("intervalEventId".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									continue;
								}
								String[] nextConfigString = content.split(";");
								for (int g=0; g<nextConfigString.length; g++)
								{
									NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
									if (nextConfigString[g].indexOf(":") == -1)
									{
										eConfig.skillEffectId = Integer.parseInt(nextConfigString[g]);
									}
									else 
									{
										String[] vStrings = nextConfigString[g].split(":");
										eConfig.skillEffectId = Integer.parseInt(vStrings[0]);
										eConfig.delay = Integer.parseInt(vStrings[1]);
									}
									config.skillIntervalModelList[i] = eConfig;
								}
							}
							else if("endEventId".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("0"))
								{
									continue;
								}
								String[] nextConfigString = content.split(";");
								for (int g=0; g<nextConfigString.length; g++)
								{
									NextSkillEffectConfig eConfig = new NextSkillEffectConfig();
									if (nextConfigString[g].indexOf(":") == -1)
									{
										eConfig.skillEffectId = Integer.parseInt(nextConfigString[g]);
									}
									else 
									{
										String[] vStrings = nextConfigString[g].split(":");
										eConfig.skillEffectId = Integer.parseInt(vStrings[0]);
										eConfig.delay = Integer.parseInt(vStrings[1]);
									}
									config.skillEndModelList[i] = eConfig;
								}
							}
						}
						for (int j=0;j<nextSkillEffectConfigs.size();j++)
						{
							config.skillModelList[j] = nextSkillEffectConfigs.get(j);
						}
						config.buffEffectInfo = bufInfo;
						this.skillModelBuffConfig.put(config.skillModelId, config);
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
