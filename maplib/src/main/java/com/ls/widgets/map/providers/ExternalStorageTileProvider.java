package com.ls.widgets.map.providers;

import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.utils.OfflineMapUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExternalStorageTileProvider extends TileProvider
{
    private StringBuilder sbuilder;
    
    public ExternalStorageTileProvider(OfflineMapConfig config)
    {
        super(config);
        sbuilder = new StringBuilder();
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

        File file = new File(sbuilder.toString());
        if (file.exists()) {
            return new FileInputStream(file);
        }
        
        return null;
    }
}
