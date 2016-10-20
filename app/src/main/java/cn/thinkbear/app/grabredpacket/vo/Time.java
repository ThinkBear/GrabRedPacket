package cn.thinkbear.app.grabredpacket.vo;

/**
 *
 * @author ThinkBear
 */

public class Time {

    public static final long MIN_TIME = -28800000;//最小时间,格式为时：分。即 00：00
    public static final long MAX_TIME = 57540000;//最大时间，格式为时：分，即 23：59

    private long start;
    private long end;

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
