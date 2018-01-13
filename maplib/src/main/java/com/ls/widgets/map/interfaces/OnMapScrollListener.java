package com.ls.widgets.map.interfaces;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.events.MapScrolledEvent;

public interface OnMapScrollListener 
{
	public void onScrolledEvent(MapWidget v, MapScrolledEvent event);
}
