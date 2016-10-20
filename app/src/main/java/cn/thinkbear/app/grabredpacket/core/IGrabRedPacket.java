package cn.thinkbear.app.grabredpacket.core;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * 核心业务接口
 *
 * 子类：BaseGrabRedPacket
 *
 * @author ThinkBear
 */

public interface IGrabRedPacket {
    /**
     *  延迟打开操作
     * @param nodeInfo 模拟点击的节点
     * @param time 延迟毫秒数
     */
    public void delayClick(AccessibilityNodeInfo nodeInfo , int time);

    /**
     * 通知栏事件
     * @param event
     */
    public void doNotificationStateChanged(AccessibilityEvent event);

    /**
     *表示改变一个窗口的内容的事件，更具体地说是植根于事件的源的子树
     * @param event
     */
    public void doWindowContentChanged(AccessibilityEvent event);

    /**
     * 代表打开PopupWindow，菜单，对话框的事件，等等。
     * @param event
     */
    public void doWindowStateChanged(AccessibilityEvent event);
}
