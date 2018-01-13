package com.ls.widgets.map.events;

/**
 * Represents scroll event. Event will be fired when map is scrolled.
 */
public class MapScrolledEvent 
{
	private int dx;
	private int dy;
	boolean byUser;
	
	public MapScrolledEvent(int originX, int originY)
	{
		this.dx = originX;
		this.dy = originY;
	}
	
	public void setData(int dx, int dy, boolean byUser)
	{
	    this.dx = dx;
	    this.dy = dy;
	    this.byUser = byUser;
	}
	
	/**
	 * @return
	 * Returns the number of pixels the map was scrolled from it's last position by X axis.
	 */
	public int getDX() 
	{
		return dx;
	}
	
	/**
	 * @return
	 * Returns the number of pixels the map was scrolled from it's last position by Y axis.
	 */	
	public int getDY() 
	{
		return dy;
	}
	
	/**
	 * @returns Returns true if map scroll was caused by the user, false otherwise.
	 */
	public boolean isByUser()
	{
	    return byUser;
	}
}
