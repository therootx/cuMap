package com.ls.widgets.map.utils;

import android.graphics.Point;
import android.graphics.PointF;

public class MathUtils 
{
	public static double distance(Point start, Point finish)
	{
		return Math.sqrt(Math.pow((start.x - finish.x),2) + Math.pow((start.y - finish.y), 2));
	}
	
	
	public static double distance(float x1, float y1, float x2, float y2)
	{
		return Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2), 2));
	}
	
	
	public static Point middle(Point first, Point second)
	{
		return new Point((first.x + second.x)/2, (first.y + second.y)/2);
	}
	
	
	public static PointF middle(PointF first, PointF second)
	{
		return new PointF((first.x + second.x)/2.0f, (first.y + second.y)/2.0f);
	}
	
	
	public static PointF middle(float x1, float y1, float x2, float y2)
	{
		return new PointF((x1 + x2)/2.0f, (y1 + y2)/2.0f);
	}
}
