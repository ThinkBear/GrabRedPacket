package cn.thinkbear.app.grabredpacket.vo;

import cn.thinkbear.app.grabredpacket.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThinkBear
 */

public class WeChatConfig {
    public static final int FINDS[] = {R.string.wx_find_0, R.string.wx_find_1, R.string.wx_find_2, R.string.wx_find_3};
    public static final int OPENS[] = {R.string.wx_open_0, R.string.wx_open_1};
    public static final int GRABS[] = {R.string.wx_grab_0, R.string.wx_grab_1, R.string.wx_grab_2};

    public static final int WX_FIND_0 = 0;
    public static final int WX_FIND_1 = 1;
    public static final int WX_FIND_2 = 2;
    public static final int WX_FIND_3 = 3;

    public static final int WX_OPEN_0 = 0;
    public static final int WX_OPEN_1 = 1;

    public static final int WX_GRAB_0 = 0;
    public static final int WX_GRAB_1 = 1;
    public static final int WX_GRAB_2 = 2;

    private int versionCode;//手机安装的微信版本号
    private String versionName = "未安装";//手机安装的微信版本名称
    private int find;//抢红包模式
    private int open;//打开红包后
    private int grab;//抢到红包后

    private int findDelay;//延迟几毫秒打开红包
    private int openDelay;//延迟几毫秒拆红包
    private int grabDelay;//延迟几毫秒关闭红包详情

    private boolean groupEnabled = false;//群过滤是否启动

    private boolean optionGroup = true;

    public boolean isOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(boolean optionGroup) {
        this.optionGroup = optionGroup;
    }

    private List<String> allGroup = new ArrayList<String>();

    public List<String> getAllGroup() {
        return allGroup;
    }

    public void setAllGroup(List<String> allGroup) {
        this.allGroup = allGroup;
    }

    public boolean isGroupEnabled() {
        return groupEnabled;
    }

    public void setGroupEnabled(boolean groupEnabled) {
        this.groupEnabled = groupEnabled;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getFind() {
        return find;
    }

    public void setFind(int find) {
        this.find = find;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getGrab() {
        return grab;
    }

    public void setGrab(int grab) {
        this.grab = grab;
    }

    public int getFindDelay() {
        return findDelay;
    }

    public void setFindDelay(int findDelay) {
        this.findDelay = findDelay;
    }

    public int getOpenDelay() {
        return openDelay;
    }

    public void setOpenDelay(int openDelay) {
        this.openDelay = openDelay;
    }

    public int getGrabDelay() {
        return grabDelay;
    }

    public void setGrabDelay(int grabDelay) {
        this.grabDelay = grabDelay;
    }

}
