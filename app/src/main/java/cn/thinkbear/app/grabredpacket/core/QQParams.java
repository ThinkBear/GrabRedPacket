package cn.thinkbear.app.grabredpacket.core;

/**
 * QQ端 参数
 * @author ThinkBear
 */

public class QQParams {

    /** QQ的包名*/
    public static final String PACKAGENAME = "com.tencent.mobileqq";
    //由AccessibilityEvent的getClassName函数取得
    //QQ主页面
    public static final String UI_SPLASH_ACTIVITY = "com.tencent.mobileqq.activity.SplashActivity";
    //打开红包后的弹框
    public static final String UI_QWALLET_PLUGIN_PROXY_ACTIVITY = "cooperation.qwallet.plugin.QWalletPluginProxyActivity";

    public static final String UI_QQLS_ACTIVITY = "com.tencent.mobileqq.activity.QQLSActivity";


    public static final String KEY_QQREDPACKET = "[QQ红包]";
    public static final String KEY_LOOK_GET_DETAIL = "查看领取详情";
    public static final String KEY_CLICK_LOOK_DETAIL = "点击查看详情";
    public static final String KEY_CLICK_GET_COMMAND = "点击领取口令";
    public static final String KEY_CLICK_INPUT_COMMAND = "点击输入口令";
    public static final String KEY_SEND = "发送";

    public static final String KEY_RETURN_DESC = "返回消息界面";
    public static final String KEY_GROUP_CENTER = "群通知中心";

    public static final int WINDOW_NONE = 0;
    public static final int WINDOW_SPLASH_ACTIVITY = 1;
    public static final int WINDOW_QWALLET_PLUGIN_PROXY_ACTIVITY = 2;
    public static final int WINDOW_QQLS_ACTIVITY = 3;

    public static final String CLASS_NAME_TEXTVIEW = "android.widget.TextView";

    public static final String CLASS_NAME_IMAGEVIEW = "android.widget.ImageView";

    public static final String CLASS_NAME_BUTTON = "android.widget.Button";
    public static final String CLASS_NAME_RELATIVELAYOUT = "android.widget.RelativeLayout";

    public static final String CLASS_NAME_ABSLISTVIEW = "android.widget.AbsListView";

}
