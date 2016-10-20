package cn.thinkbear.app.grabredpacket.utils;


import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

/**
 *
 * @author ThinkBear
 */

public class Utils_Static {
    private static int fillWidth = 0;
    private static int fillHeight = 0;
    private static int barHeight = 0;

    /**
     * 取得状态栏的高度
     *
     * @param res
     * @return 返回状态栏的高度
     */
    public static int getBarHeight(Resources res) {
        if (barHeight == 0) {
            int resourceId = res.getIdentifier("status_bar_height", "dimen",
                    "android");
            if (resourceId > 0) {
                barHeight = res.getDimensionPixelSize(resourceId);
            }
        }
        return barHeight;
    }

    /**
     * 取得手机屏幕的宽度
     *
     * @param activity
     * @return
     */
    public static int getFillWidth(Activity activity) {
        if (fillWidth == 0) {
            initFillWidthAndHeight(activity);
        }
        return fillWidth;
    }

    /**
     * 取得手机屏幕的高度
     *
     * @param activity
     * @return
     */
    public static int getFillHeight(Activity activity) {
        if (fillHeight == 0) {
            initFillWidthAndHeight(activity);
        }
        return fillHeight;
    }

    /**
     * 取得手机屏幕的高（不含状态栏）
     *
     * @param activity
     * @return
     */
    public static int getFillHeightNoBar(Activity activity) {
        return getFillHeight(activity) - getBarHeight(activity.getResources());
    }

    public static void initFillWidthAndHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        fillWidth = dm.widthPixels;
        fillHeight = dm.heightPixels;
    }

    /**
     * 关闭软键盘
     *
     * @param activity
     */
    public static void closeInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView()
                        .getWindowToken(), 0);
            }
        }
    }

    public static int getValueByInt(String value) {
        int result = 0;
        if (value != null && !value.equals("")) {
            try {
                result = Integer.valueOf(value);
            } catch (NumberFormatException e) {

            }
        }
        return result;
    }

    public static float getValueByFloat(String value) {
        float result = 0;
        if (value != null && !value.equals("")) {
            try {
                result = Float.valueOf(value);
            } catch (NumberFormatException e) {

            }
        }
        return result;
    }


    /**
     * 中断线程
     *
     * @param task
     */
    public static void cancelAllTask(Thread... task) {
        for (Thread t : task) {
            if (t != null
                    && (t.getState() == Thread.State.RUNNABLE || t.getState() != Thread.State.TERMINATED)) {
                t.interrupt();
                t = null;
            }
        }
    }

}
