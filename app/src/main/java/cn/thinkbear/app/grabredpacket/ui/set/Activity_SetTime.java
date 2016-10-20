package cn.thinkbear.app.grabredpacket.ui.set;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.adapter.Adapter_SetTime;
import cn.thinkbear.app.grabredpacket.ui.App;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.dialog.DialogFactory;
import cn.thinkbear.app.grabredpacket.share.view.Widget_ActivityBar;
import cn.thinkbear.app.grabredpacket.utils.Utils_Time;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;
import cn.thinkbear.app.grabredpacket.vo.Time;

import java.util.List;

/**
 * 时间段 管理页面
 * @author ThinkBear
 */

public class Activity_SetTime extends BaseActivity{

    private Widget_ActivityBar bar = null;
    private View startTimePanel = null;
    private View endTimePanel = null;
    private TextView startTime = null;
    private TextView endTime = null;

    private TextView addTime = null;
    private ListView main = null;
    private MyClickEvent myClickEvent = null;

    private long startTimeValue = Time.MIN_TIME;
    private long endTimeValue = Time.MAX_TIME;

    private App app = null;
    private BaseConfig config = null;

    private Adapter_SetTime adapter = null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_time;
    }

    @Override
    protected void doInitView() {
        this.app = (App) super.getApplication();
        this.config = this.app.getBaseConfig();

        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);
        this.startTimePanel = super.findViewById(R.id.startTimePanel);
        this.endTimePanel = super.findViewById(R.id.endTimePanel);
        this.startTime = (TextView) super.findViewById(R.id.startTime);
        this.endTime = (TextView) super.findViewById(R.id.endTime);
        this.addTime = (TextView) super.findViewById(R.id.addTime);
        this.main = (ListView) super.findViewById(R.id.main);
        this.myClickEvent = new MyClickEvent();

        this.adapter = new Adapter_SetTime(super.getApplicationContext(),this.config.getAllTime());
    }

    @Override
    protected void doSetView() {
        this.bar.setTitle(R.string.setTimeTitle);
        this.startTimePanel.setOnClickListener(this.myClickEvent);
        this.endTimePanel.setOnClickListener(this.myClickEvent);
        this.addTime.setOnClickListener(this.myClickEvent);

        this.startTime.setText(Utils_Time.formatTimeToString(startTimeValue));
        this.endTime.setText(Utils_Time.formatTimeToString(endTimeValue));
        this.main.setAdapter(this.adapter);
        this.adapter.setOnCallback(new MyCallback());
    }


    private class MyCallback implements Adapter_SetTime.OnCallback{

        @Override
        public void deleteEvent(final Time t) {
            DialogFactory dialog = DialogFactory.getInstance(Activity_SetTime.this);
            dialog.setMessage("确定要删除此时段？");
            dialog.setYesAndNoMode("删除", "取消", new DialogFactory.OnYesCallback() {
                @Override
                public void doYesClick(Dialog dialog) {
                    dialog.dismiss();
                    config.getAllTime().remove(t);
                    app.setBaseConfig(config);
                    adapter.notifyDataSetChanged();
                }
            },null);
        }
    }

    private class MyClickEvent implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.startTimePanel:
                    showTimeDialog(R.string.startTimeTitle,new MyStartTimeSetEvent(),startTimeValue);
                    break;
                case R.id.endTimePanel:
                    showTimeDialog(R.string.endTimeTitle,new MyEndTimeSetEvent(),endTimeValue);
                    break;
                case R.id.addTime:
                    if(startTimeValue >= endTimeValue){
                        Toast.makeText(getApplicationContext(), "开始时间不能大于或等于结束时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Time> allTime = config.getAllTime();
                    for (Time t : allTime){
                        long start = t.getStart();
                        long end = t.getEnd();

                        if(startTimeValue>=start && startTimeValue<end){
                            Toast.makeText(getApplicationContext(),"开始时间已在添加的时段中，请重新选择",Toast.LENGTH_SHORT).show();
                            return;
                        }else if(endTimeValue>start && endTimeValue <= end){
                            Toast.makeText(getApplicationContext(),"结束时间已在添加的时段中，请重新选择",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    Time newTime = new Time();
                    newTime.setStart(startTimeValue);
                    newTime.setEnd(endTimeValue);

                    config.getAllTime().add(newTime);
                    app.setBaseConfig(config);

                    adapter.notifyDataSetChanged();

                    break;
            }
        }

    }

    public void showTimeDialog(int titleId,TimePickerDialog.OnTimeSetListener listener,long time){
        int[] times = Utils_Time.formatTimeToArray(time);
        TimePickerDialog tpd = new TimePickerDialog(this,listener,times[0],times[1],true);
        tpd.setTitle(titleId);
        tpd.show();
    }

    private class MyStartTimeSetEvent implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            startTimeValue = Utils_Time.parseToLong(hourOfDay+":"+minute);
           /* if(time > weChatConfig.getEndTime()){//如果大于结束时间
                Toast.makeText(getApplicationContext(),"开始时间不能大于结束时间",Toast.LENGTH_SHORT).show();
                return;
            }*/
            startTime.setText(Utils_Time.formatTimeToString(startTimeValue));
           }

    }

    private class MyEndTimeSetEvent implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            endTimeValue = Utils_Time.parseToLong(hourOfDay+":"+minute);
            /*if(time < weChatConfig.getStartTime()){//如果大于结束时间
                Toast.makeText(getApplicationContext(),"结束时间不能小于开始时间",Toast.LENGTH_SHORT).show();
                return;
            }*/
            endTime.setText(Utils_Time.formatTimeToString(endTimeValue));
        }

    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Activity_SetTime.class);
        activity.startActivity(intent);
    }
}
