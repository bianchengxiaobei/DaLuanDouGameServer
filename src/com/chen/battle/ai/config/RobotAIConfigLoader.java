package com.chen.battle.ai.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class RobotAIConfigLoader
{
	private Logger log = LogManager.getLogger(RobotAIConfigLoader.class);
	public Map<Integer, RobotAIConfig> robotAIConfigMap = new TreeMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("robots");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("robot").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						RobotAIConfig config = new RobotAIConfig();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("id").equals(schilds.item(j).getNodeName()))
							{
								config.id = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
//							else if(("AIType").equals(schilds.item(j).getNodeName()))
//							{
//								config.RobotType = EAIRobotType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
//							}
							else if(("ModelId").equals(schilds.item(j).getNodeName()))
							{
								config.ModelId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("NodeType").equals(schilds.item(j).getNodeName()))
							{
								config.NodeType = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("ParentId").equals(schilds.item(j).getNodeName()))
							{
								config.ParentId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("Values").equals(schilds.item(j).getNodeName()))
							{
								String content = schilds.item(j).getTextContent().trim();
								if (content.equals("0") || content.equals(""))
								{
									continue;
								}
								String[] values = content.split(",");
								config.params = new int[values.length];
								for (int t=0;t<values.length;t++)
								{
									config.params[t] = Integer.parseInt(values[t]);
								}
							}
						}		
						this.robotAIConfigMap.put(config.id, config);
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
