package com.ls.widgets.map.utils;


public class LogUtils {
	
	public static final String APP = "MAP WIDGET";
	
	public static void logHeap(Class clazz) 
	{
//	    Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
//	    Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
//	    Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
//	    DecimalFormat df = new DecimalFormat();
//	    df.setMaximumFractionDigits(2);
//	    df.setMinimumFractionDigits(2);
//
//	    Log.d(APP, "debug. =================================");
//	    Log.d(APP, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) in [" + clazz.getName().replaceAll("com.myapp.android.","") + "]");
//	    Log.d(APP, "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
//	    System.gc();
//	    System.gc();

	    // don't need to add the following lines, it's just an app specific handling in my app        
//	    if (allocated>=(new Double(Runtime.getRuntime().maxMemory())/new Double((1048576))-MEMORY_BUFFER_LIMIT_FOR_RESTART)) {
//	        android.os.Process.killProcess(android.os.Process.myPid());
//	    }
	}
	
}
