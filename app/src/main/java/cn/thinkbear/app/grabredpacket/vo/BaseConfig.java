package cn.thinkbear.app.grabredpacket.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThinkBear
 */

public class BaseConfig {
    private boolean agreement = false;//是否同意免责声明
    private boolean qqEnabled = false;//qq抢红包是否启动
    private boolean wcEnabled = false;//微信抢红包是否启动
    private boolean autoUnlock = false;//是否启动锁黑屏自动抢模式

    private boolean timeEnabled = false;//时间段是否启动

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    private List<Time> allTime = new ArrayList<Time>();

    public boolean isTimeEnabled() {
        return timeEnabled;
    }

    public void setTimeEnabled(boolean timeEnabled) {
        this.timeEnabled = timeEnabled;
    }

    public boolean isQqEnabled() {
        return qqEnabled;
    }

    public void setQqEnabled(boolean qqEnabled) {
        this.qqEnabled = qqEnabled;
    }

    public boolean isWcEnabled() {
        return wcEnabled;
    }

    public void setWcEnabled(boolean wcEnabled) {
        this.wcEnabled = wcEnabled;
    }

    public boolean isAutoUnlock() {
        return autoUnlock;
    }

    public void setAutoUnlock(boolean autoUnlock) {
        this.autoUnlock = autoUnlock;
    }

    public List<Time> getAllTime() {
        return allTime;
    }

    public void setAllTime(List<Time> allTime) {
        this.allTime = allTime;
    }
}
