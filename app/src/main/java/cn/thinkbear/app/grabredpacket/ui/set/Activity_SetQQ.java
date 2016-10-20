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
import cn.thinkbear.app.grabredpacket.vo.QQConfig;

/**
 * QQ端 设置页面
 * @author ThinkBear
 */

public class Activity_SetQQ extends BaseActivity {
    private Widget_ActivityBar bar = null;


    private View findPanel = null;
    private View openPanel = null;

    private TextView findOption = null;
    private TextView openOption = null;

    private MyClickEvent myClickEvent = null;

    private App app = null;


    private TextView findDelay = null;
    private TextView openDelay = null;

    private SeekBar findSeek = null;
    private SeekBar openSeek = null;


    private ImageView groupSet = null;
    private ImageView groupSwitch = null;

    private QQConfig config = null;
    private MySeekBarChangeEvent seekBarChangeEvent = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_qq;
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
                this.findOption.setText(QQConfig.FINDS[selectIndex]);
                Utils_Anim.startRandomAnimation(super.getApplicationContext(), this.findOption);
                break;
            case App.SELECT_OPTION_OPEN_REQUEST:
                config.setOpen(selectIndex);
                this.openOption.setText(QQConfig.OPENS[selectIndex]);
                Utils_Anim.startRandomAnimation(super.getApplicationContext(), this.openOption);
                break;
        }

        this.app.setQqConfig(this.config);
    }

    @Override
    protected void doInitView() {
        this.app = (App) super.getApplication();
        this.config = this.app.getQqConfig();

        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);

        this.findPanel = super.findViewById(R.id.findPanel);
        this.openPanel = super.findViewById(R.id.openPanel);

        this.findOption = (TextView) super.findViewById(R.id.findOption);
        this.openOption = (TextView) super.findViewById(R.id.openOption);

        this.myClickEvent = new MyClickEvent();

        this.findDelay = (TextView) super.findViewById(R.id.findDelay);
        this.openDelay = (TextView) super.findViewById(R.id.openDelay);
        this.findSeek = (SeekBar) super.findViewById(R.id.findSeek);
        this.openSeek = (SeekBar) super.findViewById(R.id.openSeek);


        this.groupSet = (ImageView) super.findViewById(R.id.groupSet);
        this.groupSwitch = (ImageView) super.findViewById(R.id.groupSwitch);

        this.seekBarChangeEvent = new MySeekBarChangeEvent();

    }


    @Override
    protected void doSetView() {
        this.bar.setTitle(R.string.qq_set_title);
        this.findPanel.setOnClickListener(this.myClickEvent);
        this.openPanel.setOnClickListener(this.myClickEvent);

        this.groupSet.setOnClickListener(this.myClickEvent);
        this.groupSwitch.setOnClickListener(this.myClickEvent);


        this.findSeek.setOnSeekBarChangeListener(this.seekBarChangeEvent);
        this.openSeek.setOnSeekBarChangeListener(this.seekBarChangeEvent);

        this.findSeek.setMax(App.MAX_DELAY);
        this.openSeek.setMax(App.MAX_DELAY);

        this.findSeek.setProgress(this.config.getFindDelay());
        this.openSeek.setProgress(this.config.getOpenDelay());

        this.findOption.setText(QQConfig.FINDS[this.config.getFind()]);
        this.openOption.setText(QQConfig.OPENS[this.config.getOpen()]);

        this.findDelay.setText(String.valueOf(this.config.getFindDelay())+"毫秒");
        this.openDelay.setText(String.valueOf(this.config.getOpenDelay())+"毫秒");

        this.groupSwitch.setImageResource(this.config.isGroupEnabled()?R.mipmap.open:R.mipmap.close);

    }


    private class MyClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.findPanel:
                    Activity_SelectOption.startActivity(Activity_SetQQ.this, R.string.wx_find_title, QQConfig.FINDS, config.getFind(), App.SELECT_OPTION_FIND_REQUEST);
                    break;
                case R.id.openPanel:
                    Activity_SelectOption.startActivity(Activity_SetQQ.this, R.string.wx_open_title, QQConfig.OPENS, config.getOpen(), App.SELECT_OPTION_OPEN_REQUEST);
                    break;
                case R.id.groupSet:
                    Activity_SetQQGroup.startActivity(Activity_SetQQ.this);
                    break;
                case R.id.groupSwitch:

                    if (config.isGroupEnabled()) {
                        config.setGroupEnabled(false);
                        groupSwitch.setImageResource(R.mipmap.close);
                    } else {
                        config.setGroupEnabled(true);
                        groupSwitch.setImageResource(R.mipmap.open);
                        if (config.getAllGroup().isEmpty()) {
                            DialogFactory dialog = DialogFactory.getInstance(Activity_SetQQ.this);
                            dialog.setMessage("当前群过滤为空，前往添加!");
                            dialog.setYesAndNoMode("前往添加", "取消", new DialogFactory.OnYesCallback() {
                                @Override
                                public void doYesClick(Dialog dialog) {
                                    dialog.dismiss();
                                    Activity_SetQQGroup.startActivity(Activity_SetQQ.this);
                                }
                            }, null);
                        }
                    }

                    app.setQqConfig(config);
                    break;

            }
        }
    }


    private class MySeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                switch (seekBar.getId()) {
                    case R.id.findSeek:
                        config.setFindDelay(progress);
                        findDelay.setText(progress + "毫秒");
                        break;
                    case R.id.openSeek:
                        config.setOpenDelay(progress);
                        openDelay.setText(progress + "毫秒");
                        break;
                }
                app.setQqConfig(config);

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
        Intent intent = new Intent(activity, Activity_SetQQ.class);
        activity.startActivity(intent);
    }

}
