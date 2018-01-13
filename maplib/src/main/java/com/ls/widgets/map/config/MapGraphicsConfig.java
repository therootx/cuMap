package com.ls.widgets.map.config;

/**
 * Allows to configure the appearance of the map aspects.
 */

public class MapGraphicsConfig 
{
    public static final int DEFAULT_ACCURACY_AREA_COLOR = 0x331767e9;
    public static final int DEFAULT_ACCURACY_AREA_BORDER_COLOR = 0xFF1767e9;
    
	private int dotPointerDrawableId;
	private int arrowPointerDrawableId;
	
	private int accuracyAreaColor;
	private int accuracyAreaBorderColor;
	
	
	public MapGraphicsConfig()
	{
		dotPointerDrawableId = -1;
		arrowPointerDrawableId = -1;
		accuracyAreaColor = DEFAULT_ACCURACY_AREA_COLOR;
		accuracyAreaBorderColor = DEFAULT_ACCURACY_AREA_BORDER_COLOR;
	}

	
	public int getDotPointerDrawableId() 
	{
		return dotPointerDrawableId;
	}

	/**
	 * Configures the location pointer look when no bearing is available.
	 * @param dotPointerDrawableId - id of the drawable resource.
	 */
	public void setDotPointerDrawableId(int dotPointerDrawableId) 
	{
		this.dotPointerDrawableId = dotPointerDrawableId;
	}

	
	public int getArrowPointerDrawableId() 
	{
		return arrowPointerDrawableId;
	}

	
	/**
	 * Configures the location pointer look when bearing is available.
	 * @param dotPointerDrawableId - id of the drawable resource.
	 */
	public void setArrowPointerDrawableId(int arrowPointerDrawableId) 
	{
		this.arrowPointerDrawableId = arrowPointerDrawableId;
	}

	
	public int getAccuracyAreaColor() 
	{
		return accuracyAreaColor;
	}

	
	/**
	 * Configures the accuracy area color of location pointer. Use this template to set the color: 0xAARRGGBB.
	 * AA - alpha channel, RR - red component, GG - green component, BB - blue component.
	 * @param accuracyAreaColor - color.
	 */
	public void setAccuracyAreaColor(int accuracyAreaColor) 
	{
		this.accuracyAreaColor = accuracyAreaColor;
	}

	
	public int getAccuracyAreaBorderColor() 
	{
		return accuracyAreaBorderColor;
	}


	/**
	 * Configures the accuracy area border color.
	 * @param accuracyAreaBorderColor - color.
	 */
	public void setAccuracyAreaBorderColor(int accuracyAreaBorderColor) 
	{
		this.accuracyAreaBorderColor = accuracyAreaBorderColor;
	}
}
