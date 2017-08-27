package com.chen.battle.skill.loader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.chen.battle.skill.structs.ESkillEffectType;
import com.chen.battle.skill.structs.ESkillTargetType;

public class SkillEffectTypeConfigXmlLoader
{
	private Logger log = LogManager.getLogger(SkillEffectTypeConfigXmlLoader.class);
	public  Map<Integer, ESkillEffectType> skillEffectTypeMap = new HashMap<>();
	public void load(String file)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("skills");
			if (list.getLength() > 0)
			{
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++)
				{
					if (("skill").equals(childs.item(i).getNodeName()))
					{
						NodeList schilds = childs.item(i).getChildNodes();
						int id = 0;
						ESkillEffectType type = ESkillEffectType.None;
						for (int j=0;j<schilds.getLength();j++)
						{
							if (("id").equals(schilds.item(j).getNodeName()))
							{
								id = Integer.parseInt(schilds.item(j).getTextContent().trim());
							}
							else if(("type").equals(schilds.item(j).getNodeName()))
							{
								type = ESkillEffectType.values()[Integer.parseInt(schilds.item(j).getTextContent().trim())];
							}
						}
						this.skillEffectTypeMap.put(id, type);
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
