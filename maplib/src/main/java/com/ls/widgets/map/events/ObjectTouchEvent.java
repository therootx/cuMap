package com.ls.widgets.map.events;

/**
 * Object touch event occurs when user touches the map object.
 */
public class ObjectTouchEvent
{
	private Object objectId;
	private long layerId;
	
	public ObjectTouchEvent(Object id, long layerId)
	{
		this.objectId = id;
		this.layerId = layerId;
	}
	
	/**
	 * @return
	 * Returns the ID of the map object.
	 */
	public Object getObjectId() 
	{
		return objectId;
	}
	
	
	public void setObjectId(Object objectId) 
	{
		this.objectId = objectId;
	}
	
	
	/**
	 * @return
	 * Returns the ID of the layer where map object is located. 
	 */
	public long getLayerId() 
	{
		return layerId;
	}
	
	
	public void setLayerId(int layerId) 
	{
		this.layerId = layerId;
	}
}