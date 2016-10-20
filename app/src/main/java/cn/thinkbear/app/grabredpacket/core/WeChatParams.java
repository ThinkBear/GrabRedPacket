package cn.thinkbear.app.grabredpacket.core;

/**
 * 微信端 参数
 * @author ThinkBear
 */

public class WeChatParams {


    /** 微信的包名*/
    public static final String PACKAGENAME = "com.tencent.mm";

    //由AccessibilityEvent的getClassName函数取得
    //微信主页面
    public static final String UI_LAUNCHER = "com.tencent.mm.ui.LauncherUI";
    //抢红包界面
    public static final String UI_LUCKY_MONEY_RECEIVE = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    //红包详情界面
    public static final String UI_LUCKY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";


    public static final String KEY_GETREDPACKET = "领取红包";
    public static final String KEY_WECHATREDPACKET = "[微信红包]";
    public static final String KEY_RETURN_DESC = "返回";



    public static final int WINDOW_NONE = 0;
    public static final int WINDOW_LUCKYMONEY_RECEIVEUI = 1;
    public static final int WINDOW_LUCKYMONEY_DETAIL = 2;
    public static final int WINDOW_LAUNCHER = 3;


    /** 不能再使用文字匹配的最小版本号 */
    public static final int WECHAT_VERSION_MIN = 700;// 6.3.8 对应code为680,6.3.9对应code为700

    public static final String CLASS_NAME_BUTTON = "android.widget.Button";
    public static final String CLASS_NAME_IMAGEVIEW = "android.widget.ImageView";
    public static final String CLASS_NAME_TEXTVIEW = "android.widget.TextView";
    public static final String CLASS_NAME_LISTVIEW = "android.widget.ListView";

}
