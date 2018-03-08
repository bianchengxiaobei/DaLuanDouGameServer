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

public class CollectionConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(CollectionConfigXMLLoader.class);
	public Map<Integer, CollectionConfig> collectionConfig = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("collectionConfigs");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("collection").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						CollectionConfig config = new CollectionConfig();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("collectionId").equals(schilds.item(j).getNodeName()))
							{
								config.collectionId = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("gold".equals(schilds.item(j).getNodeName()))
							{
								config.gold = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}	
							else if("rate".equals(schilds.item(j).getNodeName()))
							{
								config.rate = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if("addRate".equals(schilds.item(j).getNodeName()))
							{
								config.addRate = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
						}
						this.collectionConfig.put(config.collectionId, config);
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
