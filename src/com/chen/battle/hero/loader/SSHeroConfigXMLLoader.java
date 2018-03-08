package com.chen.battle.hero.loader;

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

import com.chen.battle.hero.config.SHeroConfig;

public class SSHeroConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(SSHeroConfigXMLLoader.class);
	public Map<Integer, SHeroConfig> heroConfigMap = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("heros");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("hero").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						int id = 0;
						SHeroConfig config = new SHeroConfig();
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("id").equals(schilds.item(j).getNodeName()))
							{
								id = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("colliderRadius").equals(schilds.item(j).getNodeName()))
							{
								config.colliderRadius = Float.parseFloat(schilds.item(j).getTextContent().trim());
							}
							else if("skillList".equals(schilds.item(j).getNodeName()))
							{
								String[] skillString = schilds.item(j).getTextContent().trim().split(",");
								for (int k=0;k<skillString.length;k++)
								{
									config.skillList[k] = Integer.parseInt(skillString[k]);
								}
							}
							else if(("maxHp").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.maxHp = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("phyAttack").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.phyAttack = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("attackSpeed").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.attackSpeed = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("reliveTime").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.reliveSecond = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("attackDis").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.attackDist = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("phyDefence").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.phyDefence = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("magicDefence").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.magicDefence = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("costGold").equals(schilds.item(j).getNodeName()))
							{
								config.costGold = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("costDimaond").equals(schilds.item(j).getNodeName()))
							{
								config.costDimaond = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("moveSpeed").equals(schilds.item(j).getNodeName()))
							{
								config.baseFp.moveSpeed = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
						}		
						this.heroConfigMap.put(id, config);
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
