package com.ls.widgets.map.interfaces;

import android.location.Location;

public interface MapLocationListener 
{
	public void onMovePinTo(Location location);
	public void onChangePinVisibility(boolean visible);
	
}
