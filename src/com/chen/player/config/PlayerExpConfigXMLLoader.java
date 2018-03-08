package com.chen.player.config;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class PlayerExpConfigXMLLoader 
{
	public Logger logger = LogManager.getLogger(PlayerExpConfigXMLLoader.class);
	public PlayerExpConfig levelToExpConfig = new PlayerExpConfig();
	public void Load(String filePath)
	{
		try{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		InputStream in = new FileInputStream(filePath);
		Document doc = builder.parse(in);
		NodeList list = doc.getElementsByTagName("Roots");
		if (list.getLength() > 0)
		{
			Node node = list.item(0);
			NodeList childs = node.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++)
			{
				if (("root").equals(childs.item(i).getNodeName()))
				{
					NodeList schilds = childs.item(i).getChildNodes();
					int level = 0;
					int exp = 0;
					for (int j=0;j<schilds.getLength();j++)
					{
						if (("level").equals(schilds.item(j).getNodeName()))
						{
							level = Integer.parseInt(schilds.item(j).getTextContent().trim());
						}
						else if(("exp").equals(schilds.item(j).getNodeName()))
						{
							exp = Integer.parseInt(schilds.item(j).getTextContent().trim());
						}
						this.levelToExpConfig.levelToExp.put(level, exp);
					}
				}
			}
			in.close();
		}
	} 
	catch (Exception e)
	{
		logger.error(e);
	}
	}
}
