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

import com.chen.battle.skill.config.SkillModelCaculateConfig;
import com.chen.battle.skill.structs.EEffectCaculateType;
import com.chen.battle.skill.structs.ESkillModelTargetType;
import com.chen.battle.skill.structs.NextSkillEffectConfig;
import com.chen.parameter.structs.EParameterCate;

public class SkillCaculateConfigXMLLoader
{
	private Logger log = LogManager.getLogger(SkillCaculateConfigXMLLoader.class);
	public Map<Integer, SkillModelCaculateConfig> skillModelCaculateConfig = new HashMap<>();
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
						SkillModelCaculateConfig config = new SkillModelCaculateConfig();
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
							else if("eEffectCate".equals(schilds.item(j).getNodeName()))
							{
								config.eEffectCate = EParameterCate.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}	
							else if("EffectBaseValue".equals(schilds.item(j).getNodeName()))
							{
								config.EffectBaseValue = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("EffectRate".equals(schilds.item(j).getNodeName()))
							{
								config.EffectRate = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("eEffectPlusRate".equals(schilds.item(j).getNodeName()))
							{
								if (!schilds.item(j).getTextContent().trim().equals("0"))
								{
									String[] vecs = schilds.item(j).getTextContent().trim().split(";");
									for (int index=0; index<vecs.length; index++)
									{
										String[] vecs2 = vecs[index].split(":");
										config.eEffectAddCacuType[index] = EEffectCaculateType.values()[Integer.parseInt(vecs2[0])];
										config.eEffectAddCacuValue[index] = Integer.parseInt(vecs2[1]);
									}
								}
							}
							else if("eEffectMultiplyRate".equals(schilds.item(j).getNodeName()))
							{
								if (!schilds.item(j).getTextContent().trim().equals("0"))
								{
									String[] vecs = schilds.item(j).getTextContent().trim().split(";");
									for (int index=0; index<vecs.length; index++)
									{
										String[] vecs2 = vecs[index].split(":");
										config.eEffectMultCacuType[index] = EEffectCaculateType.values()[Integer.parseInt(vecs2[0])];
										config.eEffectMultCacuValue[index] = Integer.parseInt(vecs2[1]);
									}
								}
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
						}
						for (int j=0;j<nextSkillEffectConfigs.size();j++)
						{
							config.skillModelList[j] = nextSkillEffectConfigs.get(j);
						}
						this.skillModelCaculateConfig.put(config.skillModelId, config);
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
