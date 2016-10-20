package cn.thinkbear.app.grabredpacket.utils;

import cn.thinkbear.app.grabredpacket.vo.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by liaokaixiong on 2016/8/21.
 */
public class Utils_Time {

    public static long parseToLong(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            long nowTime = sdf.parse(time).getTime();
            return nowTime;
        } catch (ParseException e) {
        }
        return 0;
    }

    public static int[] formatTimeToArray(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return new int[]{hour,minute};
    }

    public static String formatTimeToString(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String result = null;
        try{
            result = sdf.format(new Date(time));
        }catch(Exception e){
        }
        return result;
    }

    public static boolean isRange(List<Time> allTime){
        if(allTime == null || allTime.isEmpty()){//如果为空，表示没有设置时间段
            return true;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        try {
            long nowTime = sdf.parse(hour+":"+minute).getTime();
            for (Time t : allTime){
                if(t.getStart() <= nowTime && t.getEnd() > nowTime) {
                    return true;
                }
            }
        } catch (ParseException e) {
        }

        return false;

    }
}
