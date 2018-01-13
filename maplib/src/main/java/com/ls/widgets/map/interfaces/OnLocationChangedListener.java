package com.ls.widgets.map.interfaces;

import android.location.Location;

import com.ls.widgets.map.MapWidget;

public interface OnLocationChangedListener
{
    public void onLocationChanged(MapWidget v, Location location);
}
