package com.chen.battle.map.structs;

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
import com.chen.battle.hero.loader.SSHeroConfigXMLLoader;
import com.chen.battle.structs.CVector3D;

public class SSHeroBornConfigXMLLoader 
{
	private Logger log = LogManager.getLogger(SSHeroBornConfigXMLLoader.class);
	public Map<Integer,Map<Integer,SHeroBornPosConfig>> heroBornPosConfigMap = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("born");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("map").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						int id = 0;
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("id").equals(schilds.item(j).getNodeName()))
							{
								id = Integer.parseInt(schilds.item(j).getTextContent().trim());
								Map<Integer, SHeroBornPosConfig> Posmap = new HashMap<Integer, SHeroBornPosConfig>();
								this.heroBornPosConfigMap.put(id, Posmap);
							}
							else if("pos_1".equals(schilds.item(j).getNodeName()))
							{
								SHeroBornPosConfig config = new SHeroBornPosConfig();
								String[] skillString = schilds.item(j).getTextContent().trim().split(";");
								for (int k=0;k<skillString.length;k++)
								{
									String[] poString = skillString[k].split(",");
									float x = Float.parseFloat(poString[0]);
									float y = Float.parseFloat(poString[1]);
									float z = Float.parseFloat(poString[2]);
									CVector3D pos = new CVector3D(x, y, z);
									config.bornList.add(pos);
								}
								System.err.println("3er3r3");						
								this.heroBornPosConfigMap.get(id).put(1, config);;
							}
							else if("pos_2".equals(schilds.item(j).getNodeName()))
							{
								SHeroBornPosConfig config = new SHeroBornPosConfig();
								String[] skillString = schilds.item(j).getTextContent().trim().split(";");
								for (int k=0;k<skillString.length;k++)
								{
									String[] poString = skillString[k].split(",");
									float x = Float.parseFloat(poString[0]);
									float y = Float.parseFloat(poString[1]);
									float z = Float.parseFloat(poString[2]);
									CVector3D pos = new CVector3D(x, y, z);
									config.bornList.add(pos);
								}
								System.err.println("3e234232r3r3");
								this.heroBornPosConfigMap.get(id).put(2, config);;														
							}
						}		
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
