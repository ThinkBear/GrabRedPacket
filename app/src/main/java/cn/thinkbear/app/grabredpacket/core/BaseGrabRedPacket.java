package cn.thinkbear.app.grabredpacket.core;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityNodeInfo;

import cn.thinkbear.app.grabredpacket.utils.AccessibilityHelper;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;

/**
 * QQ端和微信端通过继承此类，完成具体的功能实现
 * @author ThinkBear
 */

public abstract class BaseGrabRedPacket implements IGrabRedPacket {
    private Handler myHandler = null;
    private AccessibilityService service = null;
    private BaseConfig baseConfig = null;

    public BaseGrabRedPacket(AccessibilityService service, BaseConfig baseConfig){
        this.service = service;
        this.baseConfig = baseConfig;
    }

    @Override
    public void delayClick(final AccessibilityNodeInfo nodeInfo, int time) {
        if(time==0){
            AccessibilityHelper.performClick(nodeInfo);
            nodeInfo.recycle();
        }else{
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityHelper.performClick(nodeInfo);
                    nodeInfo.recycle();
                }
            }, time);
        }
    }

    public AccessibilityService getService(){
        return this.service;
    }

    public BaseConfig getBaseConfig(){
        return this.baseConfig;
    }

    public Handler getHandler() {
        if(myHandler == null) {
            myHandler = new Handler(Looper.getMainLooper());
        }
        return myHandler;
    }
}
