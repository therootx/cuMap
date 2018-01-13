package com.ls.widgets.map.interfaces;

import com.ls.widgets.map.model.MapObject;

public interface Layer 
{
	/**
	 * Adds map object to the layer.
	 * @param mapObject - map object.
	 */
	public void addMapObject(MapObject mapObject);
	
	/**
	 * Removes map object from the layer.
	 * @param id - id of the map object.
	 */
	public void removeMapObject(Object id);
	
	/**
	 * Returns map object.
	 * @param id - id of the map object.
	 */
	public MapObject getMapObject(Object id);
	

	/**
	 * Returns map object by index
	 * @param index
	 * @return instance of MapObject
	 */
	public MapObject getMapObjectByIndex(int index);
	
	/**
	 * Returns the count of map objects on the layer
	 * @return number of map objects
	 */
	public int getMapObjectCount();
	
	/**
	 * Removes all map objects from the layer.
	 */
	public void clearAll();
	
	/**
	 * Shows whether the layer is visible or not.
	 * @return - true if layer is visible, false otherwise.
	 */
	public boolean isVisible();

	/**
	 * Sets layer visibility. 
	 * @param visible - true if layer should be visible, false otherwise.
	 */
	public void setVisible(boolean visible);
}
