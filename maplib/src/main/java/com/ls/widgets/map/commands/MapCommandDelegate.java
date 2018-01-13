package com.ls.widgets.map.commands;

public interface MapCommandDelegate 
{
	public void onSuccess(Object data);
	public void onError(Exception e);
}
