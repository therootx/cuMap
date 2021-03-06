package com.ls.widgets.map.providers;

import android.util.Log;

import com.ls.widgets.map.commands.GetTileTask;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.interfaces.TileManagerDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;

public abstract class TileProvider
{
    protected static final String TAG = TileProvider.class.getSimpleName();
	protected boolean paused;
    protected OfflineMapConfig config;
    protected Queue<GetTileTask> commandQueue;

    public TileProvider(OfflineMapConfig config)
    {
        this.config = config;
        commandQueue = new LinkedList<GetTileTask>();
    }
    
    public void requestTile(final int zoomLevel, final int col, final int row, final TileManagerDelegate delegate)
    {
        InputStream is = null;
       
        try {
            is = openTile(zoomLevel, col, row);
          
            if (is == null) {
                delegate.onError(null);
                return;
            }
          
            GetTileTask task = new GetTileTask(is)
            {
                @Override
                protected void onPostExecute(Boolean result) {
                    try {
	                	if (result.equals(Boolean.TRUE)) {
	                        delegate.onTileReady(zoomLevel, col, row, getResult());
	                    } else {
	                    	Log.w(TAG, "Can't load tile " + zoomLevel + "\\" + col + "_" + row + "." + config.getImageFormat());
	                        delegate.onError(null);
	                    }
                    } finally {
	                    try {
	                       closeStream();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                        Log.w(TAG, "Can't close input stream due to exception:" + e);
	                        delegate.onError(null);
	                    }
                    }
                }
            };
            
            if (paused) {
                synchronized (commandQueue) {
                    commandQueue.add(task);
                }
                return;
            }
            
            try {
            	task.execute();
            } catch (RejectedExecutionException e) {
            	delegate.onError(e);
            }
        } catch (IOException e){
            delegate.onError(e);
        }
    }
    
    
    protected abstract InputStream openTile(int zoomLevel, final int col, final int row) throws IOException;
    
    
    public void startProcessingCommands()
    {	
    	this.paused = false;
    	
    	synchronized (commandQueue) {
    		for (GetTileTask task:commandQueue) {
    			task.execute();
    		}
    		
    		commandQueue.clear();
    	}
    }

    public void pauseProcessingCommands()
    {
    	this.paused = true;
    }

    public void stopProcessingCommands()
    {
    	synchronized (commandQueue) {
    		commandQueue.clear();
    	}
    }

}
