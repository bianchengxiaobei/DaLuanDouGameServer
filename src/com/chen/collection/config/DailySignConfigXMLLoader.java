package com.chen.collection.config;

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

public class DailySignConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(DailySignConfigXMLLoader.class);
	public Map<Integer, DailySignConfig> dailySignConfig = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("dailyConfig");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("daily").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						DailySignConfig config = new DailySignConfig();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("dayIndex").equals(schilds.item(j).getNodeName()))
							{
								config.dayIndex = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("num").equals(schilds.item(j).getNodeName()))
							{
								config.num = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("awardId".equals(schilds.item(j).getNodeName()))
							{
								config.awardId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}					
						}
						this.dailySignConfig.put(config.dayIndex, config);
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
