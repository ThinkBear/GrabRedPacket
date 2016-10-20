package cn.thinkbear.app.grabredpacket.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import cn.thinkbear.app.grabredpacket.core.IGrabRedPacket;
import cn.thinkbear.app.grabredpacket.core.QQGrabRedPacket;
import cn.thinkbear.app.grabredpacket.core.QQParams;
import cn.thinkbear.app.grabredpacket.core.WeChatGrabRedPacket;
import cn.thinkbear.app.grabredpacket.core.WeChatParams;
import cn.thinkbear.app.grabredpacket.ui.App;
import cn.thinkbear.app.grabredpacket.utils.Utils_Log;
import cn.thinkbear.app.grabredpacket.utils.Utils_Time;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;

import java.util.Iterator;
import java.util.List;

/**
 * 抢红包辅助服务类
 * @author ThinkBear
 */

public class GRPAccessibilityService extends AccessibilityService {

    private static GRPAccessibilityService service = null;

    private IGrabRedPacket weChatGrabRedPacket = null;
    private IGrabRedPacket qqGrabRedPacket = null;

    private App app = null;
    private BaseConfig baseConfig = null;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = (App) super.getApplication();
        this.baseConfig = this.app.getBaseConfig();
        this.weChatGrabRedPacket = new WeChatGrabRedPacket(this, this.baseConfig,this.app.getWeChatConfig());
        this.qqGrabRedPacket = new QQGrabRedPacket(this,this.baseConfig,this.app.getQqConfig());
    }

    /**
     * 接收事件,如触发了通知栏变化、界面变化等
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(this.baseConfig.isTimeEnabled() && !Utils_Time.isRange(this.baseConfig.getAllTime())){//判断时间段
            return;
        }

        final int eventType = event.getEventType();
        CharSequence packageName = event.getPackageName();
        if (WeChatParams.PACKAGENAME.equals(packageName)) {//当前为微信操作
            if (this.baseConfig.isWcEnabled()){
                //微信的抢红包操作 由WeChatGrabRedPacket类处理
                switch (eventType) {
                    //通知栏事件
                    case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                        this.weChatGrabRedPacket.doNotificationStateChanged(event);
                        break;
                    //代表打开PopupWindow，菜单，对话框的事件，等等。
                    case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                        this.weChatGrabRedPacket.doWindowStateChanged(event);
                        break;
                    //表示改变一个窗口的内容的事件，更具体地说是植根于事件的源的子树
                    case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                        this.weChatGrabRedPacket.doWindowContentChanged(event);
                        break;
                }
            }
        }else if(QQParams.PACKAGENAME.equals(packageName)){//当前为QQ操作
            if (this.baseConfig.isQqEnabled()){
                //QQ的抢红包操作 由QQGrabRedPacket类处理
                switch (eventType) {
                    //通知栏事件
                    case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                        this.qqGrabRedPacket.doNotificationStateChanged(event);
                        break;
                    //代表打开PopupWindow，菜单，对话框的事件，等等。
                    case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                        this.qqGrabRedPacket.doWindowStateChanged(event);
                        break;
                    //表示改变一个窗口的内容的事件，更具体地说是植根于事件的源的子树
                    case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                        this.qqGrabRedPacket.doWindowContentChanged(event);
                        break;
                }
            }
        }


    }


    /*
     * 服务中断，如授权关闭或者将服务杀死
     */
    @Override
    public void onInterrupt() {
        Utils_Log.i("onInterrupt");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils_Log.i("onDestroy");
        service = null;
        //发送广播 -> 辅助服务已断开
        super.sendBroadcast(new Intent(App.ACTION_ACCESSIBILITY_SERVICE_DISCONNECT));
    }

    /*
             * 连接服务后,一般是在授权成功后会接收到
             */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Utils_Log.i("onServiceConnected");
        //发送广播 -> 服务成功连接
        super.sendBroadcast(new Intent(App.ACTION_ACCESSIBILITY_SERVICE_CONNECT));
        service = this;

    }

    /**
     * 判断当前服务是否正在运行
     */
    public static boolean isRunning() {
        Utils_Log.i("Service -> " + service);
        if (service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }
        return true;
    }

}
