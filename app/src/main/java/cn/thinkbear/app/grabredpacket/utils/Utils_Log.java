package cn.thinkbear.app.grabredpacket.utils;


import android.util.Log;

import cn.thinkbear.app.grabredpacket.ui.App;

/**
 * 日志 工具类
 * @author ThinkBear
 */

public class Utils_Log {
	public static final String TAG = "ThinkBear";
	
	public static void i(String tag, String msg) {
		if (App.DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String msg) {
		if (App.DEBUG) {
			Log.i(TAG, msg);
		}
	}
	


}
