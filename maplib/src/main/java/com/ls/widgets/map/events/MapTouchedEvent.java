package com.ls.widgets.map.events;

import java.util.ArrayList;

/**
 * <h1>Class Overview</h1><p>
 * MapTouchedEvent represents the touch event that occurs on the map widget.
 * It contains the position of the touch in map coordinates, screen coordinates and a list of object touch events. 
 */
public class MapTouchedEvent 
{
	private int screenX;
	private int screenY;
	private int mapX;
	private int mapY;
	
	private ArrayList<ObjectTouchEvent> touchedObjectIds;
	
	/**
	 * Returns the X coordinate of a point where user has touched in screen coordinates. 
	 * It means that you can use this value to display something on the screen without coordinate transformation.
	 * @return X coordinate of a point in screen coordinates.
	 */
	public int getScreenX() 
	{
		return screenX;
	}
	
	public void setScreenX(int screenX) 
	{
		this.screenX = screenX;
	}
	
	/**
	 * Returns the Y coordinate of a point where user has touched in screen coordinates. 
	 * It means that you can use this value to display something on the screen without coordinate transformation.
	 * @return Y coordinate of a point in screen coordinates.
	 */
	public int getScreenY() 
	{
		return screenY;
	}
	
	public void setScreenY(int screenY) 
	{
		this.screenY = screenY;
	}
	
	/**
	 * Returns the X coordinate of a point where user has touched in your original image coordinates.
	 * @return X coordinate of a point in map coordinates.
	 */
	public int getMapX() 
	{
		return mapX;
	}
	
	public void setMapX(int mapX) 
	{
		this.mapX = mapX;
	}
	
	/**
	 * Returns the Y coordinate of a point where user has touched in your original image coordinates.
	 * @return Y coordinate of a point in map coordinates.
	 */
	public int getMapY() 
	{
		return mapY;
	}
	
	
	public void setMapY(int mapY) 
	{
		this.mapY = mapY;
	}
	
	
	/**
	 * @return ArrayList of ObjectTouchEvent objects.
	 * @deprecated Use getTouchedObjectEvents() instead.
	 */
	public ArrayList<ObjectTouchEvent> getTouchedObjectIds() 
	{
		return touchedObjectIds;
	}
	
	
	/**
	 * Returns the list of ObjectTouchEvent objects. If user has touched the map
	 * where no map objects are located - this will be an empty list. If user
	 * touched on map object - array will contain ObjectTouchEvent for each
	 * touched object.
	 * 
	 * @return ArrayList of ObjectTouchEvent objects.
	 */
	public ArrayList<ObjectTouchEvent> getTouchedObjectEvents() 
	{
		return touchedObjectIds;
	}
	
	
	public void setTouchedObjectEvents(ArrayList<ObjectTouchEvent> touchedObjectIds) 
	{
		this.touchedObjectIds = touchedObjectIds;
	}

}



