package cn.thinkbear.app.grabredpacket.core;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import cn.thinkbear.app.grabredpacket.utils.AccessibilityHelper;
import cn.thinkbear.app.grabredpacket.utils.Utils_Screen;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;
import cn.thinkbear.app.grabredpacket.vo.Chat;
import cn.thinkbear.app.grabredpacket.vo.WeChatConfig;

import java.util.List;

/**
 * 微信抢红包 具体功能实现类
 * @author ThinkBear
 */

public class WeChatGrabRedPacket extends BaseGrabRedPacket {
    private WeChatConfig config = null;
    private int mCurrentWindow = WeChatParams.WINDOW_NONE;

    private boolean isReceivingHongbao;

    public WeChatGrabRedPacket(AccessibilityService service, BaseConfig baseConfig, WeChatConfig config) {
        super(service, baseConfig);
        this.config = config;
    }

    @Override
    public void doWindowContentChanged(AccessibilityEvent event) {
        if (mCurrentWindow != WeChatParams.WINDOW_LAUNCHER) { //不在聊天列表界面，不处理
            return;
        }
        this.findRP();
    }

    @Override
    public void doNotificationStateChanged(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            String content = String.valueOf(texts.get(0));//获得通知栏信息内容
            //eg:  Xiao熊: [微信红包]恭喜发财，大吉大利！
            int index = content.indexOf(":");
            if (index != -1) {//截取:后面的内容
                content = content.substring(index + 1);
            }
            if (content.contains(WeChatParams.KEY_WECHATREDPACKET)) {//如果内容含有红包信息
                Parcelable data = event.getParcelableData();
                if (data == null || !(data instanceof Notification)) {
                    return;
                }

                this.isReceivingHongbao = true;
                if (super.getBaseConfig().isAutoUnlock()&&!Utils_Screen.isUnlocking()) {//用户设置为自动解锁模式
                    Utils_Screen.getInitialize(getService().getApplicationContext()).doUnlockScreen();
                }

                //打开通知信息
                Notification notification = (Notification) data;
                PendingIntent pendingIntent = notification.contentIntent;
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void doWindowStateChanged(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        if (className == null) {//如果包.类名为空
            return;
        }
        if (WeChatParams.UI_LAUNCHER.equals(className)) {
            this.mCurrentWindow = WeChatParams.WINDOW_LAUNCHER;
            //在聊天界面,去打开红包
            this.findRP();
        } else if (WeChatParams.UI_LUCKY_MONEY_RECEIVE.equals(className)) {
            this.mCurrentWindow = WeChatParams.WINDOW_LUCKYMONEY_RECEIVEUI;

            switch (this.config.getOpen()) {
                case WeChatConfig.WX_OPEN_0://去拆红包
                    this.grabRedPacket();
                    break;
                case WeChatConfig.WX_OPEN_1://什么都不做
                    break;
            }
            //用户设置为自动解锁模式 和 是否处于自动解锁
            if (super.getBaseConfig().isAutoUnlock() && Utils_Screen.isUnlocking()) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils_Screen.getInitialize(getService().getApplicationContext()).doLockScreen();
                    }
                }, 3000);
                return;
            }
        } else if (WeChatParams.UI_LUCKY_MONEY_DETAIL.equals(className)) {
            this.mCurrentWindow = WeChatParams.WINDOW_LUCKYMONEY_DETAIL;
            //用户设置为自动解锁模式 和 是否处于自动解锁
            if (super.getBaseConfig().isAutoUnlock() && Utils_Screen.isUnlocking()) {
                return;
            }
            switch (this.config.getGrab()) {
                case WeChatConfig.WX_GRAB_0://关闭当前页面
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityHelper.performBack(getService());
                        }
                    }, this.config.getGrabDelay());
                    break;
                case WeChatConfig.WX_GRAB_1://关闭当前页面并返回桌面
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityHelper.performBack(getService());
                            AccessibilityHelper.performHome(getService());
                        }
                    }, this.config.getGrabDelay());
                    break;
            }

        } else {
            this.mCurrentWindow = WeChatParams.WINDOW_NONE;
        }
    }

    /**
     * 拆红包事件
     */
    private void grabRedPacket() {
        AccessibilityNodeInfo rootNode = getService().getRootInActiveWindow();
        if (rootNode == null) {
            return;
        }
        AccessibilityNodeInfo targetNode = null;

        if (this.config.getVersionCode() < WeChatParams.WECHAT_VERSION_MIN) {
            targetNode = AccessibilityHelper.findNodeInfosByText(rootNode, "拆红包");
        } else {
            String buttonId = "com.tencent.mm:id/b43";
            int versionCode = this.config.getVersionCode();
            if (versionCode == 700) {
                buttonId = "com.tencent.mm:id/b2c";
            } else if (versionCode == 840) {
                buttonId = "com.tencent.mm:id/ba_";
            }
            if (buttonId != null) {//如果ID不为空，则先通过ID寻找
                targetNode = AccessibilityHelper.findNodeInfosById(rootNode, buttonId);
            }
            if (targetNode == null) {
                targetNode = this.getGrabNode(rootNode);
            }
        }
        if (targetNode != null) {
            int openDelay = this.config.getOpenDelay();
            if (openDelay > 0 && super.getBaseConfig().isAutoUnlock() && Utils_Screen.isUnlocking()) {
                openDelay = 0;
            }
            super.delayClick(targetNode, openDelay);
        }
    }

    /**
     * 从聊天窗口寻找红包
     *
     * @param rootNode
     * @return
     */
    private AccessibilityNodeInfo findRPFromMsgListWindow(AccessibilityNodeInfo rootNode) {
        AccessibilityNodeInfo result = null;
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(WeChatParams.KEY_WECHATREDPACKET);
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }

    /**
     * 从聊天窗口寻找红包
     *
     * @param rootNode
     * @return
     */
    private AccessibilityNodeInfo findRPFromChatWindow(AccessibilityNodeInfo rootNode) {
        AccessibilityNodeInfo result = null;
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(WeChatParams.KEY_GETREDPACKET);
        if (list != null && !list.isEmpty()) {
            result = list.get(list.size() - 1);
        }
        return result;
    }


    /**
     * 判断是否在聊天窗口
     *
     * @param nodeInfo
     * @return
     */
    private boolean isChatWindow(AccessibilityNodeInfo nodeInfo) {
        boolean flag = false;
        if (nodeInfo == null) {
            return flag;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(WeChatParams.KEY_RETURN_DESC);
        if (list != null) {
            for (AccessibilityNodeInfo node : list) {
                if (WeChatParams.CLASS_NAME_IMAGEVIEW.equals(node.getClassName())) {
                    if (WeChatParams.KEY_RETURN_DESC.equals(node.getContentDescription())) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }


    public Chat getChatObject(AccessibilityNodeInfo rootNode) {
        if (rootNode == null) {
            return null;
        }
        String id = "com.tencent.mm:id/ces";
        int wv = this.config.getVersionCode();
        if (wv <= 680) {
            id = "com.tencent.mm:id/ew";
        } else if (wv <= 700) {
            id = "com.tencent.mm:id/cbo";
        } else if (wv <= 840) {
            id = "com.tencent.mm:id/ey";

        }
        String title = null;
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosById(rootNode, id);
        if (target != null) {
            title = String.valueOf(target.getText());
        }
        if (title == null) {
            AccessibilityNodeInfo parent = null;
            List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(WeChatParams.KEY_RETURN_DESC);
            if (list != null) {
                for (AccessibilityNodeInfo node : list) {
                    if (WeChatParams.CLASS_NAME_IMAGEVIEW.equals(node.getClassName())) {
                        if (WeChatParams.KEY_RETURN_DESC.equals(node.getContentDescription())) {
                            parent = node.getParent();//取得返回按钮的上一级
                            break;
                        }
                    }
                }
            }

            if (parent != null) {
                parent = parent.getParent();//再向上取上一级
            }
            if (parent != null) {
                if (parent.getChildCount() >= 2) {
                    AccessibilityNodeInfo node = parent.getChild(1);//标题在第2个节点上
                    if (node.getChildCount() > 0) {
                        AccessibilityNodeInfo titleNode = node.getChild(0);
                        if (WeChatParams.CLASS_NAME_TEXTVIEW.equals(titleNode.getClassName())) {
                            title = String.valueOf(titleNode.getText());//取得标题内容
                            titleNode.recycle();
                        }
                    }
                }
            }
        }

        Chat chat = null;
        if (!TextUtils.isEmpty(title)) {
            chat = new Chat();
            // title -> eg: 群聊名称(125)
            int startIndex = 0;
            if (title.endsWith(")") && (startIndex = title.lastIndexOf("(")) > 0) {
                String chatName = title.substring(0, startIndex);
                String numberStr = title.substring(startIndex + 1, title.length() - 1);//取得（）里的内容信息：125
                chat.setChatName(chatName);
                try {
                    int number = Integer.valueOf(numberStr);
                    if (number > 2) {//人数都是大于2的
                        chat.setNumber(number);
                        chat.setGroup(true);
                    }
                } catch (Exception e) {
                }

            } else {
                chat.setChatName(title);
            }
        }
        return chat;
    }


    private synchronized void findRP() {
        if (!isReceivingHongbao) {
            return;
        }

        int find = this.config.getFind();
        if (WeChatConfig.WX_FIND_3 == find) {//手动打开模式
            return;
        }

        AccessibilityNodeInfo rootNode = getService().getRootInActiveWindow();
        if (rootNode == null) {
            return;
        }

        if (this.isChatWindow(rootNode)) {
            Chat chat = this.getChatObject(rootNode);
            if (chat != null) {
                if (find == WeChatConfig.WX_FIND_1 && chat.isGroup()) {
                    return;
                }
                if (find == WeChatConfig.WX_FIND_2 && !chat.isGroup()) {
                    return;
                }

                boolean isGroupEnabled = (this.config.isGroupEnabled() && !this.config.getAllGroup().isEmpty());

                if (isGroupEnabled && chat.isGroup()) {
                    boolean flag = this.config.getAllGroup().contains(chat.getChatName());

                    if(this.config.isOptionGroup() && !flag){//如果添加的群过滤 被设为要抢的群
                        return;
                    }

                    if(!this.config.isOptionGroup() && flag){
                        return;
                    }
                }

                AccessibilityNodeInfo result = this.findRPFromChatWindow(rootNode);
                if (result != null) {
                    int findDelay = this.config.getFindDelay();
                    if (findDelay > 0 && super.getBaseConfig().isAutoUnlock() && Utils_Screen.isUnlocking()) {
                        findDelay = 0;
                    }
                    super.delayClick(result, findDelay);
                    this.isReceivingHongbao = false;
                }
            }

        } else {
            AccessibilityNodeInfo result = this.findRPFromMsgListWindow(rootNode);
            if (result != null) {
                super.delayClick(result, 0);
            }

        }
        rootNode.recycle();
    }


    private AccessibilityNodeInfo grabNode = null;

    /**
     * 取得拆红包节点
     *
     * @param rootNode
     * @return
     */
    private AccessibilityNodeInfo getGrabNode(AccessibilityNodeInfo rootNode) {
        this.grabNode = null;
        this.findGrabNode(rootNode);
        return this.grabNode;
    }

    /**
     * 递归遍历，寻找Button节点
     *
     * @param rootNode
     */
    private void findGrabNode(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.grabNode != null) {
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (WeChatParams.CLASS_NAME_BUTTON.equals(node.getClassName())) {//如果是Button
                this.grabNode = node;
                return;
            } else {
                this.findGrabNode(node);
            }
        }
    }
}
