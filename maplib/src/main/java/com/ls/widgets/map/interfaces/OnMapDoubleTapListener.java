package com.ls.widgets.map.interfaces;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.events.MapTouchedEvent;

/**
 * <h1>Interface overview</h1><p>
 * Used for receiving notifications when MapWidget view is double tapped.
 */
public interface OnMapDoubleTapListener 
{
	/**
	 * Will be called if user double taps the map widget.
	 * @param v - map widget that was double tapped.
	 * @param event - instance of {@link MapTouchedEvent}.
	 * @return true - if you want to intercept this event and provide your own implementation.<br>
	 * false - if you want to leave the default map widget behavior.
	 */
	public boolean onDoubleTap(MapWidget v, MapTouchedEvent event);
}
