package com.ls.widgets.map.interfaces;

public interface MapEventsListener 
{
    /**
     * Is called before zoom in.
     */
	public void onPreZoomIn();
	
	/**
	 * Is called when zoom in finished.
	 */
	public void onPostZoomIn();
	
	/**
	 * Is called before zoom out.
	 */
	public void onPreZoomOut();
	
	/**
	 * Is called when zoom out is finished.
	 */
	public void onPostZoomOut();
}
