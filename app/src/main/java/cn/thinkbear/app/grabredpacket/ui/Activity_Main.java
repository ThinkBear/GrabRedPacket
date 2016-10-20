package cn.thinkbear.app.grabredpacket.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.ui.orther.Activity_Agreement_Look;
import cn.thinkbear.app.grabredpacket.ui.orther.Activity_Use;
import cn.thinkbear.app.grabredpacket.ui.set.Activity_SetQQ;
import cn.thinkbear.app.grabredpacket.ui.set.Activity_SetTime;
import cn.thinkbear.app.grabredpacket.ui.set.Activity_SetWeChat;
import cn.thinkbear.app.grabredpacket.service.GRPAccessibilityService;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.dialog.DialogFactory;
import cn.thinkbear.app.grabredpacket.utils.Utils_Activity;
import cn.thinkbear.app.grabredpacket.utils.Utils_Anim;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;

/**
 *
 * @author ThinkBear
 */

public class Activity_Main extends BaseActivity {
    private TextView title = null;
    private ImageView wechatSet = null;
    private ImageView wechatSwitch = null;
    private ImageView qqSet = null;
    private ImageView qqSwitch = null;
    private ImageView asSwitch = null;
    private TextView wxVN = null;
    private TextView qqVN = null;

    private MyClickEvent myClickEvent = null;

    private ImageView autoUnlock = null;

    private TextView use = null;

    private TextView agreement = null;

    private ServiceStateBroadcast serviceStateBroadcast = null;
    private App app = null;
    private BaseConfig baseConfig = null;


    private ImageView timeSet = null;
    private ImageView timeSwitch = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.serviceStateBroadcast != null) {
            super.unregisterReceiver(this.serviceStateBroadcast);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(super.getApplicationContext(), "再按一次退出",
                    Toast.LENGTH_SHORT).show();
            this.exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            Utils_Activity.finishAll();
            /*
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //如果intent不指定category，那么无论intent filter的内容是什么都应该是匹配的。
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);*/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (GRPAccessibilityService.isRunning()) {
            this.asSwitch.setImageResource(R.mipmap.open);
        } else {
            this.asSwitch.setImageResource(R.mipmap.close);
            Utils_Anim.startRandomAnimation(getApplicationContext(), asSwitch);
            if (this.baseConfig.isWcEnabled()) {
                this.wechatSwitch.setImageResource(R.mipmap.close);
                this.baseConfig.setWcEnabled(false);
                this.app.setBaseConfig(this.baseConfig);
            }

            if (this.baseConfig.isQqEnabled()) {
                this.qqSwitch.setImageResource(R.mipmap.close);
                this.baseConfig.setQqEnabled(false);
                this.app.setBaseConfig(this.baseConfig);
            }
        }

    }

    @Override
    protected void doInitView() {
        this.app = (App) super.getApplication();
        this.baseConfig = this.app.getBaseConfig();
        this.title = (TextView) super.findViewById(R.id.title);
        this.wechatSet = (ImageView) super.findViewById(R.id.wechatSet);
        this.wechatSwitch = (ImageView) super.findViewById(R.id.wechatSwitch);
        this.qqSet = (ImageView) super.findViewById(R.id.qqSet);
        this.qqSwitch = (ImageView) super.findViewById(R.id.qqSwitch);
        this.asSwitch = (ImageView) super.findViewById(R.id.asSwitch);
        this.wxVN = (TextView) super.findViewById(R.id.wxVN);
        this.qqVN = (TextView) super.findViewById(R.id.qqVN);
        this.autoUnlock = (ImageView) super.findViewById(R.id.autoUnlock);
        this.timeSet = (ImageView) super.findViewById(R.id.timeSet);
        this.timeSwitch = (ImageView) super.findViewById(R.id.timeSwitch);
        this.use = (TextView) super.findViewById(R.id.use);
        this.agreement = (TextView) super.findViewById(R.id.agreement);
        this.myClickEvent = new MyClickEvent();
        this.serviceStateBroadcast = new ServiceStateBroadcast();

    }

    @Override
    protected void doSetView() {

        this.wechatSet.setOnClickListener(this.myClickEvent);
        this.wechatSwitch.setOnClickListener(this.myClickEvent);
        this.qqSet.setOnClickListener(this.myClickEvent);
        this.qqSwitch.setOnClickListener(this.myClickEvent);
        this.asSwitch.setOnClickListener(this.myClickEvent);
        this.autoUnlock.setOnClickListener(this.myClickEvent);
        this.timeSet.setOnClickListener(this.myClickEvent);
        this.timeSwitch.setOnClickListener(this.myClickEvent);
        this.use.setOnClickListener(this.myClickEvent);
        this.agreement.setOnClickListener(this.myClickEvent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(App.ACTION_ACCESSIBILITY_SERVICE_CONNECT);
        filter.addAction(App.ACTION_ACCESSIBILITY_SERVICE_DISCONNECT);
        super.registerReceiver(this.serviceStateBroadcast, filter);

        this.wechatSwitch.setImageResource(this.baseConfig.isWcEnabled() ? R.mipmap.open : R.mipmap.close);
        this.qqSwitch.setImageResource(this.baseConfig.isQqEnabled() ? R.mipmap.open : R.mipmap.close);
        this.timeSwitch.setImageResource(this.baseConfig.isTimeEnabled() ? R.mipmap.open : R.mipmap.close);
        this.autoUnlock.setImageResource(this.baseConfig.isAutoUnlock() ? R.mipmap.open : R.mipmap.close);

        this.wxVN.setText("微信" + this.app.getWeChatConfig().getVersionName());
        this.qqVN.setText("QQ" + this.app.getQqConfig().getVersionName());

        this.title.setText(R.string.app_name);
        try {
            PackageInfo packageInfo = super.getApplicationContext().getPackageManager().getPackageInfo(super.getPackageName(), 0);
            if(packageInfo!=null){
                this.title.append(packageInfo.versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

    }

    private class MyClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.wechatSet:
                    Activity_SetWeChat.startActivity(Activity_Main.this);
                    break;

                case R.id.wechatSwitch:
                    if (baseConfig.isWcEnabled()) {
                        wechatSwitch.setImageResource(R.mipmap.close);
                        baseConfig.setWcEnabled(false);
                        app.setBaseConfig(baseConfig);
                    } else {
                        if (GRPAccessibilityService.isRunning()) {
                            wechatSwitch.setImageResource(R.mipmap.open);
                            baseConfig.setWcEnabled(true);
                            app.setBaseConfig(baseConfig);
                        } else {
                            asSwitch.setImageResource(R.mipmap.close);
                            Toast.makeText(getApplicationContext(), "开启失败，需先开启辅助服务", Toast.LENGTH_SHORT).show();
                            Utils_Anim.startRandomAnimation(getApplicationContext(), asSwitch);
                        }
                    }
                    break;
                case R.id.qqSet:
                    Activity_SetQQ.startActivity(Activity_Main.this);
                    break;
                case R.id.qqSwitch:
                    if (baseConfig.isQqEnabled()) {
                        qqSwitch.setImageResource(R.mipmap.close);
                        baseConfig.setQqEnabled(false);
                        app.setBaseConfig(baseConfig);
                    } else {
                        if (GRPAccessibilityService.isRunning()) {
                            qqSwitch.setImageResource(R.mipmap.open);
                            baseConfig.setQqEnabled(true);
                            app.setBaseConfig(baseConfig);
                        } else {
                            asSwitch.setImageResource(R.mipmap.close);
                            Toast.makeText(getApplicationContext(), "开启失败，需先开启辅助服务", Toast.LENGTH_SHORT).show();
                            Utils_Anim.startRandomAnimation(getApplicationContext(), asSwitch);
                        }
                    }

                    break;
                case R.id.asSwitch:
                    openAccessibilityServiceSettings(GRPAccessibilityService.isRunning() ? R.string.closeASTips : R.string.openASTips);
                    break;
                case R.id.autoUnlock:
                    if (baseConfig.isAutoUnlock()) {
                        baseConfig.setAutoUnlock(false);
                        autoUnlock.setImageResource(R.mipmap.close);
                    } else {
                        baseConfig.setAutoUnlock(true);
                        autoUnlock.setImageResource(R.mipmap.open);
                    }
                    app.setBaseConfig(baseConfig);
                    break;
                case R.id.timeSet:
                    Activity_SetTime.startActivity(Activity_Main.this);
                    break;
                case R.id.timeSwitch:
                    if (baseConfig.isTimeEnabled()) {
                        baseConfig.setTimeEnabled(false);
                        timeSwitch.setImageResource(R.mipmap.close);
                    } else {
                        baseConfig.setTimeEnabled(true);
                        timeSwitch.setImageResource(R.mipmap.open);
                        if (baseConfig.getAllTime().isEmpty()) {
                            DialogFactory dialog = DialogFactory.getInstance(Activity_Main.this);
                            dialog.setMessage("当前时间段为空，前往添加!");
                            dialog.setYesAndNoMode("前往添加", "取消", new DialogFactory.OnYesCallback() {
                                @Override
                                public void doYesClick(Dialog dialog) {
                                    dialog.dismiss();
                                    Activity_SetTime.startActivity(Activity_Main.this);
                                }
                            }, null);
                        }
                    }
                    app.setBaseConfig(baseConfig);
                    break;
                case R.id.use:
                    Activity_Use.startActivity(Activity_Main.this);
                    break;
                case R.id.agreement:
                    Activity_Agreement_Look.startActivity(Activity_Main.this);
                    break;
            }
        }
    }


    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings(int tipId) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, tipId, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServiceStateBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) {
                return;
            }

            String action = intent.getAction();

            if (App.ACTION_ACCESSIBILITY_SERVICE_CONNECT.equals(action)) {//
                asSwitch.setImageResource(R.mipmap.open);
            } else if (App.ACTION_ACCESSIBILITY_SERVICE_DISCONNECT.equals(action)) {
                asSwitch.setImageResource(R.mipmap.close);
            }
        }
    }



    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Activity_Main.class);
        activity.startActivity(intent);
    }
}
