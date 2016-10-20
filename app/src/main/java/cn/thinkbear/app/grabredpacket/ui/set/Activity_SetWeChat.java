package cn.thinkbear.app.grabredpacket.ui.set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.ui.App;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.dialog.DialogFactory;
import cn.thinkbear.app.grabredpacket.share.view.Widget_ActivityBar;
import cn.thinkbear.app.grabredpacket.utils.Utils_Anim;
import cn.thinkbear.app.grabredpacket.vo.WeChatConfig;

/**
 * 微信端 设置页面
 * @author ThinkBear
 */

public class Activity_SetWeChat extends BaseActivity {
    private Widget_ActivityBar bar = null;


    private View findPanel = null;
    private View openPanel = null;
    private View grabPanel = null;

    private TextView findOption = null;
    private TextView openOption = null;
    private TextView grabOption = null;

    private MyClickEvent myClickEvent = null;

    private App app = null;

    private TextView findDelay = null;
    private TextView openDelay = null;
    private TextView grabDelay = null;

    private SeekBar findSeek = null;
    private SeekBar openSeek = null;
    private SeekBar grabSeek = null;

    private MySeekBarChangeEvent seekBarChangeEvent = null;

    private ImageView groupSet = null;
    private ImageView groupSwitch = null;
    private WeChatConfig config = null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_wechat;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != App.SELECT_OPTION_RESULT || data == null) {
            return;
        }
        int selectIndex = data.getIntExtra(App.SELECTINDEX, 0);
        switch (requestCode) {
            case App.SELECT_OPTION_FIND_REQUEST:
                config.setFind(selectIndex);
                this.findOption.setText(WeChatConfig.FINDS[selectIndex]);
                Utils_Anim.startRandomAnimation(super.getApplicationContext(), this.findOption);
                break;
            case App.SELECT_OPTION_OPEN_REQUEST:
                config.setOpen(selectIndex);
                this.openOption.setText(WeChatConfig.OPENS[selectIndex]);
                Utils_Anim.startRandomAnimation(super.getApplicationContext(), this.openOption);
                break;
            case App.SELECT_OPTION_GRAB_REQUEST:
                config.setGrab(selectIndex);
                this.grabOption.setText(WeChatConfig.GRABS[selectIndex]);
                Utils_Anim.startRandomAnimation(super.getApplicationContext(), this.grabOption);
                break;
        }

        this.app.setWeChatConfig(this.config);
    }


    @Override
    protected void doInitView() {
        this.app = (App) super.getApplication();
        this.config = this.app.getWeChatConfig();

        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);

        this.findPanel = super.findViewById(R.id.findPanel);
        this.openPanel = super.findViewById(R.id.openPanel);
        this.grabPanel = super.findViewById(R.id.grabPanel);

        this.findOption = (TextView) super.findViewById(R.id.findOption);
        this.openOption = (TextView) super.findViewById(R.id.openOption);
        this.grabOption = (TextView) super.findViewById(R.id.grabOption);

        this.myClickEvent = new MyClickEvent();

        this.findDelay = (TextView) super.findViewById(R.id.findDelay);
        this.openDelay = (TextView) super.findViewById(R.id.openDelay);
        this.grabDelay = (TextView) super.findViewById(R.id.grabDelay);

        this.findSeek = (SeekBar) super.findViewById(R.id.findSeek);
        this.openSeek = (SeekBar) super.findViewById(R.id.openSeek);
        this.grabSeek = (SeekBar) super.findViewById(R.id.grabSeek);

        this.groupSet = (ImageView) super.findViewById(R.id.groupSet);
        this.groupSwitch = (ImageView) super.findViewById(R.id.groupSwitch);

        this.seekBarChangeEvent = new MySeekBarChangeEvent();
    }


    @Override
    protected void doSetView() {
        this.bar.setTitle(R.string.wx_set_title);
        this.findPanel.setOnClickListener(this.myClickEvent);
        this.openPanel.setOnClickListener(this.myClickEvent);
        this.grabPanel.setOnClickListener(this.myClickEvent);

        this.groupSet.setOnClickListener(this.myClickEvent);
        this.groupSwitch.setOnClickListener(this.myClickEvent);

        this.findSeek.setOnSeekBarChangeListener(this.seekBarChangeEvent);
        this.openSeek.setOnSeekBarChangeListener(this.seekBarChangeEvent);
        this.grabSeek.setOnSeekBarChangeListener(this.seekBarChangeEvent);

        this.findSeek.setMax(App.MAX_DELAY);
        this.openSeek.setMax(App.MAX_DELAY);
        this.grabSeek.setMax(App.MAX_DELAY);

        this.findSeek.setProgress(this.config.getFindDelay());
        this.openSeek.setProgress(this.config.getOpenDelay());
        this.grabSeek.setProgress(this.config.getGrabDelay());


        this.findOption.setText(WeChatConfig.FINDS[this.config.getFind()]);
        this.openOption.setText(WeChatConfig.OPENS[this.config.getOpen()]);
        this.grabOption.setText(WeChatConfig.GRABS[this.config.getGrab()]);

        this.findDelay.setText(String.valueOf(this.config.getFindDelay())+"毫秒");
        this.openDelay.setText(String.valueOf(this.config.getOpenDelay())+"毫秒");
        this.grabDelay.setText(String.valueOf(this.config.getGrabDelay())+"毫秒");

        this.groupSwitch.setImageResource(this.config.isGroupEnabled()?R.mipmap.open:R.mipmap.close);
    }


    private class MyClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.findPanel:
                    Activity_SelectOption.startActivity(Activity_SetWeChat.this, R.string.wx_find_title, WeChatConfig.FINDS, app.getWeChatConfig().getFind(), App.SELECT_OPTION_FIND_REQUEST);
                    break;
                case R.id.openPanel:
                    Activity_SelectOption.startActivity(Activity_SetWeChat.this, R.string.wx_open_title, WeChatConfig.OPENS, app.getWeChatConfig().getOpen(), App.SELECT_OPTION_OPEN_REQUEST);
                    break;
                case R.id.grabPanel:
                    Activity_SelectOption.startActivity(Activity_SetWeChat.this, R.string.wx_grab_title, WeChatConfig.GRABS, app.getWeChatConfig().getGrab(), App.SELECT_OPTION_GRAB_REQUEST);
                    break;
                case R.id.groupSet:
                    Activity_SetWeChatGroup.startActivity(Activity_SetWeChat.this);
                    break;
                case R.id.groupSwitch:

                    if (config.isGroupEnabled()) {
                        config.setGroupEnabled(false);
                        groupSwitch.setImageResource(R.mipmap.close);
                    } else {
                        config.setGroupEnabled(true);
                        groupSwitch.setImageResource(R.mipmap.open);
                        if (config.getAllGroup().isEmpty()) {
                            DialogFactory dialog = DialogFactory.getInstance(Activity_SetWeChat.this);
                            dialog.setMessage("当前群过滤为空，前往添加!");
                            dialog.setYesAndNoMode("前往添加", "取消", new DialogFactory.OnYesCallback() {
                                @Override
                                public void doYesClick(Dialog dialog) {
                                    dialog.dismiss();
                                    Activity_SetWeChatGroup.startActivity(Activity_SetWeChat.this);
                                }
                            }, null);
                        }
                    }

                    app.setWeChatConfig(config);
                    break;

            }
        }
    }

    private class MySeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                switch(seekBar.getId()){
                    case R.id.findSeek:
                        config.setFindDelay(progress);
                        findDelay.setText(progress+"毫秒");
                        break;
                    case R.id.openSeek:
                        config.setOpenDelay(progress);
                        openDelay.setText(progress+"毫秒");
                        break;
                    case R.id.grabSeek:
                        config.setGrabDelay(progress);
                        grabDelay.setText(progress+"毫秒");
                        break;
                }
                app.setWeChatConfig(config);

            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Activity_SetWeChat.class);
        activity.startActivity(intent);
    }

}
