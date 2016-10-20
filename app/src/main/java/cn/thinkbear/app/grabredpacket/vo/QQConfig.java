package cn.thinkbear.app.grabredpacket.vo;

import cn.thinkbear.app.grabredpacket.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThinkBear
 */

public class QQConfig {

    private int versionCode;//手机安装的QQ版本号
    private String versionName = "未安装";//手机安装的QQ版本名称



    public static final int FINDS[] = {R.string.qq_find_0, R.string.qq_find_1, R.string.qq_find_2, R.string.qq_find_3};
    public static final int OPENS[] = {R.string.qq_open_0, R.string.qq_open_1, R.string.qq_open_2, R.string.qq_open_3};

    public static final int QQ_FIND_0 = 0;
    public static final int QQ_FIND_1 = 1;
    public static final int QQ_FIND_2 = 2;
    public static final int QQ_FIND_3 = 3;

    public static final int QQ_OPEN_0 = 0;
    public static final int QQ_OPEN_1 = 1;
    public static final int QQ_OPEN_2 = 2;
    public static final int QQ_OPEN_3 = 3;


    private int find;//抢红包模式
    private int open;//打开红包后

    private int findDelay;//延迟几毫秒打开红包
    private int openDelay;//延迟几毫秒拆红包


    private boolean groupEnabled = false;//群过滤是否启动
    private List<String> allGroup = new ArrayList<String>();
    private boolean optionGroup = true;

    public boolean isOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(boolean optionGroup) {
        this.optionGroup = optionGroup;
    }

    public boolean isGroupEnabled() {
        return groupEnabled;
    }

    public void setGroupEnabled(boolean groupEnabled) {
        this.groupEnabled = groupEnabled;
    }

    public List<String> getAllGroup() {
        return allGroup;
    }

    public void setAllGroup(List<String> allGroup) {
        this.allGroup = allGroup;
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
}
