package com.chen.map;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

public class MapConfigLoader 
{
	public Logger logger = LogManager.getLogger(MapConfigLoader.class);
	public Map<Integer, MapStaticData> mapconfigs = new HashMap<>();
	public void load(String filePath)
	{	
		try 
		{
			File file = new File(filePath);		
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			IoBuffer buffer = IoBuffer.allocate(1024);
			buffer.setAutoExpand(true);
			buffer.put(data);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			buffer.flip();
			int mapId = buffer.getInt();
			int width = buffer.getInt();
			int height = buffer.getInt();
			float border = buffer.getFloat();
			
			MapStaticData staticData = new MapStaticData();
			staticData.bordeSize = border;
			staticData.widthSize =  width;
			staticData.heightSize = height;
			staticData.totleAreaSize = staticData.widthSize * staticData.heightSize;
			
			staticData.widthRegionSize = Math.round(width / border);
			staticData.heightRegionSize = Math.round(height / border);
			staticData.totleRegionSize = staticData.widthRegionSize * staticData.heightRegionSize;
			staticData.regions = new MapRegion[staticData.totleRegionSize];
//			byte[] leftData = new byte[staticData.totleRegionSize];
//			buffer.get(leftData);
			for (int i=0;i<staticData.widthRegionSize;i++)
			{
				for(int j=0;j<staticData.heightRegionSize;j++)
				{
					int idx = j + staticData.heightRegionSize * i;
					if (staticData.regions[idx] == null)
					{
						staticData.regions[idx] = new MapRegion();
					}
					staticData.regions[idx].regionX = i;
					staticData.regions[idx].regionY = j;
					byte type = buffer.get();
					staticData.regions[idx].blockType = EBlockType.values()[type];
				}
			}
			this.mapconfigs.put(mapId, staticData);
		} 
		catch (Exception e) 
		{
			logger.error(e);
		}
	}
}
