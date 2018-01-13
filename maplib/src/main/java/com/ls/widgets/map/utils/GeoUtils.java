package com.ls.widgets.map.utils;

import android.graphics.Point;
import android.location.Location;
import android.util.Log;

import com.ls.widgets.map.MapWidget;

public class GeoUtils 
{
	
	/**
	 * Helper function to translate position on the map to geographic coordinates. Map should be calibrated in order for 
	 * this method to take effect. Result will be returned in last parameter.
	 * @param v - com.ls.MapWidget
	 * @param x - x coordinate in map coordinate system.
	 * @param y - y coordinate in map coordinate system.
	 * @param location - out parameter. Will contain latitude and longitude of point on the map.
	 */
	public static void translate(MapWidget v, int x, int y, Location location)
	{
		MapCalibrationData calibration = v.getConfig().getGpsConfig().getCalibration();
		
		if (calibration == null) {
			Log.w("GeoUtils", "Can't translate. No calibration data!");
		}
		
		calibration.translate(x, y, location);
	}
	
	
	/**
	 * Helper function to convert position on the map to geographic coordinates. Result will be returned in last parameter.
	 * @param v - com.ls.MapWidget
	 * @param point - position on the map in pixels. Instance of android.graphics.Point
	 * @param location - out parameter. Will contain latitude and longitude of point on the map.
	 */
	public static void translate(MapWidget v, Point point, Location location)
	{
		MapCalibrationData calibration = v.getConfig().getGpsConfig().getCalibration();
		
		if (calibration == null) {
			Log.w("GeoUtils", "Can't translate. No calibration data!");
		}
		
		calibration.translate(point.x, point.y, location);
	}
	
	
	/**
	 * Converts location to position on the map. Result will be returned in last parameter.
	 * @param v - com.ls.MapWidget
	 * @param location instance of android.location.Location object.
	 * @param position - out parameter. Can be null.
	 * @return returns the same object that was passed as position, or if it is null - returns new Point object.
	 */
	public static void translate(MapWidget v, Location location, Point point)
	{
		MapCalibrationData calibration = v.getConfig().getGpsConfig().getCalibration();
		
		if (calibration == null) {
			Log.w("GeoUtils", "Can't translate. No calibration data!");
		}
		
		calibration.translate(location, point);
	}
}
