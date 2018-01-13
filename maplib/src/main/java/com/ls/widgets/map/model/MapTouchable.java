package com.ls.widgets.map.model;

import android.graphics.Rect;

public class MapTouchable
{
	private Object id; //Used to identify the touchable
	private Rect rect; //Used to define the position and a size of the touchable
	private MapObject drawableRef;
	
	public MapTouchable(Object id, MapObject drawable, Rect rect)
	{
		this.id = id;
		this.rect = rect;
		drawableRef = drawable;
	}
	
	public Object getId()
	{
		return id;
	}
	
	public boolean isTouched(int x, int y)
	{
		return rect.contains(x, y);
	}
	
	public boolean isTouched(Rect touchRect)
	{
		return Rect.intersects(rect, touchRect);
	}
	
	public MapObject getDrawable()
	{
		return drawableRef;
	}
	
}