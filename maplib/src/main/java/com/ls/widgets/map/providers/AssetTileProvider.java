package com.ls.widgets.map.providers;

import android.content.Context;
import android.content.res.AssetManager;

import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.utils.OfflineMapUtil;

import java.io.IOException;
import java.io.InputStream;

public class AssetTileProvider extends TileProvider
{
	private AssetManager assetManager;
	private StringBuilder sbuilder;
	
	public AssetTileProvider(Context context, OfflineMapConfig config)
	{
	    super(config);
	    
		this.assetManager = context.getAssets();
		
		sbuilder = new StringBuilder(256);
	}
	
    @Override
    protected InputStream openTile(int zoomLevel, int col, int row) throws IOException
    {
        // This is more memory efficient than regular concatenation
        String filesFolder = OfflineMapUtil.getFilesPath(config.getMapRootPath());
        sbuilder.delete(0, sbuilder.length());
        sbuilder.append(filesFolder);
        sbuilder.append(zoomLevel);
        sbuilder.append("/");
        sbuilder.append(col);
        sbuilder.append("_");
        sbuilder.append(row);
        sbuilder.append(".");
        sbuilder.append(config.getImageFormat());

        return assetManager.open(sbuilder.toString());
    }
}
