package cn.thinkbear.app.grabredpacket.core;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import cn.thinkbear.app.grabredpacket.utils.AccessibilityHelper;
import cn.thinkbear.app.grabredpacket.utils.Utils_Screen;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;
import cn.thinkbear.app.grabredpacket.vo.Chat;
import cn.thinkbear.app.grabredpacket.vo.QQConfig;
import cn.thinkbear.app.grabredpacket.vo.WeChatConfig;

import java.util.List;

/**
 * QQ抢红包 具体功能实现类
 * @author ThinkBear
 */

public class QQGrabRedPacket extends BaseGrabRedPacket {

    private int mCurrentWindow = WeChatParams.WINDOW_NONE;
    private QQConfig config = null;
    private boolean isReceivingHongbao = false;

    public QQGrabRedPacket(AccessibilityService service, BaseConfig baseConfig, QQConfig config) {
        super(service, baseConfig);
        this.config = config;
    }

    @Override
    public void doNotificationStateChanged(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            String content = String.valueOf(texts.get(0));//获得通知栏信息内容
            //eg:  Xiao熊: [QQ红包]恭喜发财，大吉大利！
            int index = content.indexOf(":") + 1;
            if (index != 0) {//截取:后面的内容
                content = content.substring(index);
            }

            if (content.contains(QQParams.KEY_QQREDPACKET)) {//如果内容含有红包信息
                Parcelable data = event.getParcelableData();
                if (data == null || !(data instanceof Notification)) {
                    return;
                }
                this.isReceivingHongbao = true;

                if (super.getBaseConfig().isAutoUnlock()&& !Utils_Screen.isUnlocking()) {//用户设置为自动解锁模式
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
    public void doWindowContentChanged(AccessibilityEvent event) {
        if (mCurrentWindow == QQParams.WINDOW_SPLASH_ACTIVITY) { //在聊天界面，去找红包
            this.findRPFromContentChanged(event);
        }
    }

    @Override
    public void doWindowStateChanged(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        if (className == null) {//如果包.类名为空
            return;
        }
        if (QQParams.UI_SPLASH_ACTIVITY.equals(className)) {
            this.mCurrentWindow = QQParams.WINDOW_SPLASH_ACTIVITY;
            //在聊天界面,去打开红包
            this.findRPFromStateChanged();
        } else if (QQParams.UI_QWALLET_PLUGIN_PROXY_ACTIVITY.equals(className)) {
            this.mCurrentWindow = QQParams.WINDOW_QWALLET_PLUGIN_PROXY_ACTIVITY;
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

            switch (this.config.getOpen()){
                case QQConfig.QQ_OPEN_0://什么都不做
                    break;
                case QQConfig.QQ_OPEN_1://关闭页面
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityHelper.performBack(getService());
                        }
                    }, this.config.getOpenDelay());
                    break;
                case QQConfig.QQ_OPEN_2://关闭页面并回到桌面
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityHelper.performBack(getService());
                            AccessibilityHelper.performHome(getService());
                        }
                    }, this.config.getOpenDelay());

                    break;
                case QQConfig.QQ_OPEN_3:
                    this.lookRPDetail();
                    break;
            }
        } else if(QQParams.UI_QQLS_ACTIVITY.equals(className)){
            this.mCurrentWindow = QQParams.WINDOW_QQLS_ACTIVITY;


        }else {
            this.mCurrentWindow = QQParams.WINDOW_NONE;
        }
    }

    /**
     * 找红包、当窗体中的内容发生改变的时候调用
     * 1、在聊天页面中，当有别的聊天信息的时，会在标题栏下弹出新信息提示组件
     * 2、当前聊天，收到新的红包信息
     *
     * @param event
     */
    private synchronized void findRPFromContentChanged(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getService().getRootInActiveWindow();
        if (rootNode == null) {
            return;
        }
        boolean isChatWindow = this.isChatWindow(rootNode);
        if (isChatWindow) {//在聊天窗口里

                if(this.clickOrtherChatMsg(rootNode)){//先寻找是否存在其它群或好友的聊天信息
                   return;
                }

                this.findRPFromStateChanged();

               /* //取得聊天的列表信息
                AccessibilityNodeInfo listView = this.getChatAbsListView(rootNode);

                if (listView != null) {
                    Utils_Log.i(listView.toString());
                    int childCount = listView.getChildCount();
                    if (childCount > 0) {
                        AccessibilityNodeInfo rpNode = listView.getChild(childCount - 1);//取得最新的一条信息
                        if (this.isRPNode(rpNode)) {
                            this.openRPNode(rpNode.getChild(rpNode.getChildCount() - 1));
                        }
                    }
                }*/

        } else {//不在聊天窗口
            //暂时无解决方案
            /*if(QQParams.MSG_LIST_CLASS_NAME.equals(event.getClassName())){
                AccessibilityNodeInfo node = event.getSource();
                if(node!=null){
                    Utils_Log.i("node: "+node.toString());
                    AccessibilityNodeInfo parent = node.getParent();
                    if(parent!=null){
                        Utils_Log.i("parent: "+parent.toString());
                        parent = parent.getParent();
                        if(parent!=null){
                            Utils_Log.i("parent: "+parent.toString());
                        }
                    }

                    this.isReceivingHongbao = true;
                    super.delayClick(node,100);
                }
            }*/
        }
        rootNode.recycle();
    }

    /**
     * 找红包、窗体改变时，调用，遍历当前窗体内容的红包信息，并进行打开红包操作
     * 1、通知栏收到红包，跳转到QQ聊天窗口，会调用此方法
     */
    private synchronized void findRPFromStateChanged() {
        if (!this.isReceivingHongbao) {
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

        Chat chat = this.getChatObject(rootNode);
        if (chat != null) {
            if (find == QQConfig.QQ_FIND_1 && chat.isGroup()) {
                return;
            }
            if (find == QQConfig.QQ_FIND_2 && !chat.isGroup()) {
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
        }
        //取得聊天的列表信息
        AccessibilityNodeInfo listView = this.getChatAbsListView(rootNode);

        if (listView != null) {
            int childCount = listView.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {//从最后一个开始往上读
                AccessibilityNodeInfo rpNode = listView.getChild(i);
                if (this.isRPNode(rpNode)) {//判断是否为红包节点
                    this.openRPNode(rpNode.getChild(rpNode.getChildCount() - 1));
                    break;
                }
            }
            listView.recycle();
        }
        rootNode.recycle();
    }

    private boolean clickOrtherChatMsg(AccessibilityNodeInfo rootNode){
        boolean flag = false;
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_QQREDPACKET);
        if (list != null && !list.isEmpty()) {
            /**
             * 在聊天页面中，当有别的聊天信息的时，会在标题栏下弹出新信息提示组件
             * eg：贁 毞 /xmao:[QQ红包]恭喜发财
             * 如果信息内容 存在[QQ红包],且为TextView，则点击跳转
             */
            AccessibilityNodeInfo node = list.get(0);//取得第一个
            if (QQParams.CLASS_NAME_TEXTVIEW.equals(node.getClassName())) {//如果为TextView类型
                CharSequence charSequence = node.getText();//取得信息内容
                if (charSequence != null) {//信息不为空
                    String content = String.valueOf(charSequence);
                    //eg:  贁 毞 /xmao:[QQ红包]恭喜发财
                    int index = content.indexOf(":") + 1;
                    if (index != 0) {//截取:后面的内容 -> [QQ红包]恭喜发财
                        content = content.substring(index);
                    }
                    if (content.startsWith(QQParams.KEY_QQREDPACKET)) {//开头为红包信息
                        super.delayClick(node, 0);//点击跳转
                        this.isReceivingHongbao = true;//更新标记
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    private void lookRPDetail(){
        AccessibilityNodeInfo rootNode = getService().getRootInActiveWindow();
        if(rootNode==null){
            return;
        }
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_LOOK_GET_DETAIL);
        if(list!=null&&!list.isEmpty()){
            super.delayClick(list.get(list.size()-1),this.config.getOpenDelay());
        }else{
            list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_CLICK_LOOK_DETAIL);
            if(list!=null&&!list.isEmpty()){
                super.delayClick(list.get(list.size()-1),this.config.getOpenDelay());
            }
        }
        rootNode.recycle();
    }


    /**
     * 判断辅助节点是否为红包节点
     * 红包信息大概布局
     * <p/>
     * RelativeLayout（最外窗器） 判断点
     * -- (0)ImageView
     * -- (1)ImageView
     * -- (2)RelativeLayout（content-desc:恭喜发财，点击查看详情） 判断点
     * ------ (0)RelativeLayout
     * ------ (1)ImageView
     * ------ (2)TextView（text:QQ红包个性版）
     *
     * @param rpNode
     * @return
     */
    private boolean isRPNode(AccessibilityNodeInfo rpNode) {
        boolean flag = false;
        //红包的最外窗口为RelativeLayout
        if (QQParams.CLASS_NAME_RELATIVELAYOUT.equals(rpNode.getClassName())) {
            int size = rpNode.getChildCount();
            if (size > 0) {
                //第三个子节点也为RelativeLayout
                if (QQParams.CLASS_NAME_RELATIVELAYOUT.equals(rpNode.getChild(size - 1).getClassName())) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 打开红包，配合 isRPNode 方法使用
     * eg:
     * AccessibilityNodeInfo rpNode;
     * if(isRPNode(rpNode)){//是红包
     * openRPNode(rpNode.getChild(2));
     * }
     *
     * @param rpNode 红包节点的第三个子节点
     */
    private void openRPNode(final AccessibilityNodeInfo rpNode) {
        if (rpNode == null) {//红包不存在
            return;
        }
        int findDelay = this.config.getFindDelay();
        if(super.getBaseConfig().isAutoUnlock() && Utils_Screen.isUnlocking()){
            findDelay = 0;
        }
        CharSequence descCS = rpNode.getContentDescription();
        if (descCS != null) {
            String desc = String.valueOf(descCS);
            if (desc.endsWith(QQParams.KEY_CLICK_GET_COMMAND)) {//指令红包

                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        delayClick(rpNode,0);
                        AccessibilityNodeInfo rootNode = getService().getRootInActiveWindow();
                        if(rootNode!=null){
                            if(clickInputCommand(rootNode)){
                                clickSendCommand(rootNode);
                            }

                        }
                    }
                },findDelay);
                this.isReceivingHongbao = false;
            } else if (desc.endsWith(QQParams.KEY_CLICK_LOOK_DETAIL)) {//普通红包、拼手气红包
                super.delayClick(rpNode, findDelay);//
                this.isReceivingHongbao = false;
            }
        }

    }


    private boolean clickInputCommand(AccessibilityNodeInfo rootNode){
        boolean flag = false;
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_CLICK_INPUT_COMMAND);
        if(list!=null && !list.isEmpty()){
            AccessibilityNodeInfo node = list.get(0);
            super.delayClick(node,0);
            flag = true;
        }
        return flag;
    }

    private void clickSendCommand(AccessibilityNodeInfo rootNode){
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_SEND);
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i = size - 1 ; i >= 0 ; i--){
                AccessibilityNodeInfo node = list.get(i);
                if(QQParams.CLASS_NAME_BUTTON.equals(node.getClassName())){
                    super.delayClick(node,0);
                    break;
                }else{
                    node.recycle();
                }
            }

        }
    }

    private AccessibilityNodeInfo listViewNode = null;

    private AccessibilityNodeInfo getChatAbsListView(AccessibilityNodeInfo rootNode){
        this.listViewNode = null;
        this.findChatAbsListView(rootNode);
        return this.listViewNode;
    }

    /**
     * 递归
     * 寻找聊天列表信息辅助节点,所有聊天信息都放在此节点下，包括红包
     * @param rootNode
     * @return
     */
    private void findChatAbsListView(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if(this.listViewNode != null){
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (QQParams.CLASS_NAME_ABSLISTVIEW.equals(node.getClassName())) {//如果是AbsListView
                this.listViewNode = node;
                return;
            } else {
                this.findChatAbsListView(node);
            }
        }
    }

    /**
     * 判断是否在聊天窗口
     *
     * @param rootNode
     * @return
     */
    private boolean isChatWindow(AccessibilityNodeInfo rootNode) {
        boolean flag = false;
        if (rootNode == null) {
            return flag;
        }
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_RETURN_DESC);
        if (list != null) {
            for (AccessibilityNodeInfo node : list) {
                if (QQParams.CLASS_NAME_TEXTVIEW.equals(node.getClassName())) {
                    if (QQParams.KEY_RETURN_DESC.equals(node.getContentDescription())) {
                        flag = true;
                        node.recycle();
                        break;
                    }else{
                        node.recycle();
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 是否为群聊天
     *
     * @param rootNode
     * @return
     */
    private boolean isGroupChat(AccessibilityNodeInfo rootNode) {
        boolean flag = false;
        if (rootNode == null) {
            return flag;
        }

        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_GROUP_CENTER);
        if (list != null) {
            for (AccessibilityNodeInfo node : list) {
                if (QQParams.CLASS_NAME_IMAGEVIEW.equals(node.getClassName())) {
                    if (QQParams.KEY_GROUP_CENTER.equals(node.getContentDescription())) {
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
        Chat chat = new Chat();
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_GROUP_CENTER);
        if (list != null) {

            AccessibilityNodeInfo parent = null;

            for (AccessibilityNodeInfo node : list) {
                if (QQParams.CLASS_NAME_IMAGEVIEW.equals(node.getClassName())) {
                    if (QQParams.KEY_GROUP_CENTER.equals(node.getContentDescription())) {
                        chat.setGroup(true);
                        break;
                    }else {
                        node.recycle();
                    }
                }
            }

            list = rootNode.findAccessibilityNodeInfosByText(QQParams.KEY_RETURN_DESC);
            if (list != null) {
                for (AccessibilityNodeInfo node : list) {
                    if (QQParams.CLASS_NAME_TEXTVIEW.equals(node.getClassName())) {
                        if (QQParams.KEY_RETURN_DESC.equals(node.getContentDescription())) {
                            parent = node.getParent();
                            break;
                        }
                    }
                }
            }

            if(parent!=null){
                if(parent.getChildCount()>2&&parent.getChild(1).getChildCount()>0){
                    AccessibilityNodeInfo titleNode = parent.getChild(1).getChild(0);
                    CharSequence titleCS = titleNode.getText();
                    if(titleCS!=null){
                        chat.setChatName(String.valueOf(titleCS));
                    }
                    titleNode.recycle();
                }
                parent.recycle();
            }
        }
        return chat;
    }
}
