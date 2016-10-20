package cn.thinkbear.app.grabredpacket.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

/**
 * Activity 管理类
 * @author ThinkBear
 */

public class Utils_Activity {
	public static List<Activity> allA = new ArrayList<Activity>();
	/**
	 * 添加一个Activity对象到集合中
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		allA.add(activity);
	}

	/**
	 * 删除一个Activity对象
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		allA.remove(activity);
	}

	/**
	 * 关闭所有的Activity页面 —— 除了主页面
	 */
	public static void finishAllNoMainActivity() {
		for (Activity activity : allA) {
			if (!activity.getClass().getSimpleName().equals("Activity_Main")) {
				activity.finish();
			}
		}
	}
	/**
	 * 关闭所有的Activity页面 —— 最后杀死本程序进程
	 */
	public static void finishAll() {
		for (Activity activity : allA) {
			activity.finish();
		}
		// 杀死该应用进程
		//android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 判断当前App是否运行在后台
	 * @param context
	 * @return
	 */
	public static boolean isRunningBackground(Context context) {
		boolean isInBackground = true;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
				//前台程序
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					for (String activeProcess : processInfo.pkgList) {
						if (activeProcess.equals(context.getPackageName())) {
							isInBackground = false;
						}
					}
				}
			}
		} else {
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			if (componentInfo.getPackageName().equals(context.getPackageName())) {
				isInBackground = false;
			}
		}

		return isInBackground;
	}
}