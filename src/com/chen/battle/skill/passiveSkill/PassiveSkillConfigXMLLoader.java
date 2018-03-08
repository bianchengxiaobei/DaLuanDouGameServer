package com.chen.battle.skill.passiveSkill;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.chen.battle.skill.structs.NextSkillEffectConfig;

public class PassiveSkillConfigXMLLoader
{
	private Logger log = LogManager.getLogger(PassiveSkillConfigXMLLoader.class);
	public Map<Integer, PassiveSkillConfig> passiveSkillConfig = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("passiveSkillConfigs");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("passiveSkill").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						PassiveSkillConfig config = new PassiveSkillConfig();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("passiveSkillId").equals(schilds.item(j).getNodeName()))
							{
								config.passiveSkillId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("Cooldown").equals(schilds.item(j).getNodeName()))
							{
								config.Cooldown = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}		
							else if("bIfClearWhenDead".equals(schilds.item(j).getNodeName()))
							{
								config.bIfClearWhenDead = Boolean.parseBoolean(schilds.item(j).getTextContent().trim());
							}		
							else if("passiveSkillTargetType".equals(schilds.item(j).getNodeName()))
							{
								config.passiveSkillTargetType = EPassiveSkillTargetType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
							else if("passiveSkillTriggerType".equals(schilds.item(j).getNodeName()))
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("") || content == null)
								{
									continue;
								}
								String[] nextConfigString = content.split(",");
								int size = nextConfigString.length;
								config.passiveSkillTriggerType = new int[size];
								for (int g=0; g<size; g++)
								{
									config.passiveSkillTriggerType[g] = Integer.parseInt(nextConfigString[g]);
								}
							}
							else if ("passiveEffectArray".equals(schilds.item(j).getNodeName())) 
							{
								String content =  schilds.item(j).getTextContent().trim();
								if (content.equals("") || content == null)
								{
									continue;
								}
								String[] nextConfigString = content.split(",");
								int size = nextConfigString.length;
								config.passiveEffectArray = new int[size];
								for (int g=0; g<size; g++)
								{
									config.passiveEffectArray[g] = Integer.parseInt(nextConfigString[g]);
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
						this.passiveSkillConfig.put(config.passiveSkillId, config);
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
