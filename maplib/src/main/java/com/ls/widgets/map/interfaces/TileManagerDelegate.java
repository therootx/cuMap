package com.ls.widgets.map.interfaces;

import android.graphics.drawable.Drawable;

public interface TileManagerDelegate 
{
	public void onTileReady(int zoomLevel, int col, int row, Drawable drawable);
	public void onError(Exception e);
}
