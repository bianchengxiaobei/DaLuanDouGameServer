package com.chen.battle.skill.structs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ElementArray<T>
{
	public class ArrInside<E>
	{
		public E element;
		public boolean isVaild;
		public ArrInside() 
		{
			this.isVaild = false;
		}
		public void Clear()
		{
			this.isVaild = false;
		}
	}
	public List<ArrInside<T>> array;
	public int curSize;
	public int maxSize;
	private int nextIndex;
	public ElementArray(int size) 
	{
		this.maxSize = size;
		this.array = new ArrayList<>(size);
		curSize = 0;
		for (int i=0; i<size; i++)
		{
			this.array.add(new ArrInside<>());
			this.array.get(i).isVaild = false;
		}
	}
	public int GetMaxSize()
	{
		return maxSize;
	}
	public int AddElement(T t)
	{
		if (curSize == maxSize)
		{
			return -1;
		}
		for (int i=0;i<maxSize;i++)
		{
			if (this.array.get(i).isVaild == false)
			{
				this.array.get(i).element = t;
				this.array.get(i).isVaild = true;
				++curSize;
				return i;
			}
		}
		return -1;
	}
	public int RemoveElement(T t)
	{
		if (curSize == 0)
		{
			return -1;
		}
		for (int i=0;i<maxSize;i++)
		{
			if (this.array.get(i).isVaild && this.array.get(i).element == t)
			{
				this.array.get(i).isVaild = false;
				--curSize;
				return i;
			}
		}
		return -1;
	}
	
	public boolean RemoveIndex(int index)
	{
		if (index < 0 || index > maxSize - 1)
		{
			return false;
		}
		this.array.get(index).isVaild = false;
		--curSize;
		return true;
	}
	public boolean HasElement(T t)
	{
		if (curSize == 0)
		{
			return false;
		}
		for (int i=0;i<maxSize;i++)
		{
			if (this.array.get(i).isVaild && this.array.get(i).element.equals(t))
			{
				return true;
			}
		}
		return false;
	}
	public T Begin()
	{
		nextIndex = 0;
		if (curSize == 0)
		{
			return null;
		}
		for (int i=0;i<maxSize;i++)
		{
			if (this.array.get(i).isVaild)
			{
				nextIndex = i+1;
				return this.array.get(i).element;
			}
		}
		return null;
	}
	public T Next()
	{
		if (curSize == 0)
		{
			return null;
		}
		for (int i=nextIndex;i<maxSize;i++)
		{
			if (this.array.get(i).isVaild)
			{
				nextIndex = i+1;
				return this.array.get(i).element;
			}
		}
		return null;
	}

	public T GetElementByIndex(int index)
	{
		if (index < 0 || index > maxSize - 1 || !this.array.get(index).isVaild )
		{
			return null;
		}
		return this.array.get(index).element;
	}
	
	public void Clear()
	{
		for (int i=0;i<maxSize;i++)
		{
			this.array.get(i).Clear();
		}
		this.curSize = 0;
	}
}
