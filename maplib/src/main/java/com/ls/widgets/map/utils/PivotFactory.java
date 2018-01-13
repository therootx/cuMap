package com.ls.widgets.map.utils;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class PivotFactory 
{
	public enum PivotPosition 
	{
		PIVOT_TOP_LEFT,
		PIVOT_CENTER,
		PIVOT_BOTTOM_CENTER
	}
	
	public static Point createPivotPoint(Drawable drawable, PivotPosition type)
	{
		if (drawable == null) {
			throw new IllegalArgumentException();
		}
		
		switch (type) {
		case PIVOT_TOP_LEFT:
			return new Point(0,0);
		case PIVOT_CENTER:
			return new Point(drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
		case PIVOT_BOTTOM_CENTER:
			return new Point(drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight());
		}
		
		return null;
	}
}
