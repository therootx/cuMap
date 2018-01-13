package com.ls.widgets.map.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;

public class GetTileTask extends AsyncTask<Integer, Integer, Boolean> {

	private InputStream is;
	private Drawable result;
	private static final Drawable transparent = new ColorDrawable(Color.TRANSPARENT);
	
	public GetTileTask(InputStream is)
	{
		this.is = is;
	}
	
	
	public Drawable getResult()
	{
		return result;
	}
	
	
	public void closeStream() throws IOException
	{
         is.close();
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) 
	{
		BitmapDrawable tileDrawable = null;
		
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(is);
		
			if (bitmap != null) {
				tileDrawable = (BitmapDrawable) new BitmapDrawable(bitmap);
				result = new TransitionDrawable(new Drawable[]{transparent, tileDrawable});
				result.setBounds(0, 0, tileDrawable.getBitmap().getWidth(), tileDrawable.getBitmap().getHeight());
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (OutOfMemoryError e) {
			return Boolean.FALSE;
		}
	}
}
