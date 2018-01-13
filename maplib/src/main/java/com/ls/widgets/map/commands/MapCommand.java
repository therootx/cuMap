package com.ls.widgets.map.commands;

import com.ls.widgets.map.config.OfflineMapConfig;

public abstract class MapCommand
	implements Runnable
{
	private OfflineMapConfig config;
	private MapCommandDelegate delegate;
	
	public MapCommand(OfflineMapConfig config, MapCommandDelegate delegate)
	{
		this.config = config;
		this.delegate = delegate;
	}

	public OfflineMapConfig getConfig() {
		return config;
	}
	
	public void onSuccess(Object data)
	{
		if (delegate != null)
			delegate.onSuccess(data);
	}
	
	public void onError(Exception e)
	{
		e.printStackTrace();
		if (delegate != null)
			delegate.onError(e);
	}
}

