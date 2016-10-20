package cn.thinkbear.app.grabredpacket.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.core.QQParams;
import cn.thinkbear.app.grabredpacket.core.WeChatParams;
import cn.thinkbear.app.grabredpacket.utils.Utils_Activity;
import cn.thinkbear.app.grabredpacket.utils.Utils_Log;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;
import cn.thinkbear.app.grabredpacket.vo.QQConfig;
import cn.thinkbear.app.grabredpacket.vo.WeChatConfig;


/**
 *
 * @author ThinkBear
 */

public class App extends Application implements Thread.UncaughtExceptionHandler{

    public static final boolean DEBUG = false;
    //辅助服务连接成功
    public static final String ACTION_ACCESSIBILITY_SERVICE_CONNECT = "action.accessibility.service.connect";
    //辅助服务连接中断
    public static final String ACTION_ACCESSIBILITY_SERVICE_DISCONNECT = "action.accessibility.service.disconnect";

    public static final int SELECT_OPTION_FIND_REQUEST = 1;
    public static final int SELECT_OPTION_OPEN_REQUEST = 2;
    public static final int SELECT_OPTION_GRAB_REQUEST = 3;
    public static final int SELECT_OPTION_RESULT = 11;

    public static final int MAX_DELAY = 3000;

    public static final String TITLE = "Title";
    public static final String ITEMS = "Items";
    public static final String SELECTINDEX = "SelectIndex";

    public static final String SP_WECHATCONFIG = "SP_WeChatConfig";
    public static final String SP_BASECONFIG = "SP_BaseConfig";
    public static final String SP_QQCONFIG = "SP_QQConfig";

    private WeChatConfig weChatConfig = null;
    private BaseConfig baseConfig = null;
    private QQConfig qqConfig = null;

    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;
    private Gson gson = null;

    @Override
    public void onCreate() {
        super.onCreate();

        this.sp = super.getSharedPreferences(super.getResources().getString(R.string.config_file), Activity.MODE_PRIVATE);
        this.editor = this.sp.edit();
        this.gson = new Gson();

        this.initVo();

        if (!DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    private void initVo(){

        String baseConfigData = this.sp.getString(App.SP_BASECONFIG,null);
        if(!TextUtils.isEmpty(baseConfigData)){
            try{
                this.baseConfig = this.gson.fromJson(baseConfigData,BaseConfig.class);
            }catch(Exception e){
            }
        }
        if(this.baseConfig == null){
            this.baseConfig = new BaseConfig();
        }

        String weChatConfigData = this.sp.getString(App.SP_WECHATCONFIG,null);
        if(!TextUtils.isEmpty(weChatConfigData)){
            try{
                this.weChatConfig = this.gson.fromJson(weChatConfigData,WeChatConfig.class);
            }catch(Exception e){
            }
        }
        if(this.weChatConfig==null){
            this.weChatConfig = new WeChatConfig();
        }
        try {
            PackageInfo wechatPackageInfo = super.getApplicationContext().getPackageManager().getPackageInfo(WeChatParams.PACKAGENAME, 0);
            this.weChatConfig.setVersionCode(wechatPackageInfo.versionCode);
            this.weChatConfig.setVersionName(wechatPackageInfo.versionName);
            Utils_Log.i("wc version: "+wechatPackageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String qqConfigData = this.sp.getString(App.SP_QQCONFIG,null);
        if(!TextUtils.isEmpty(qqConfigData)){
            try{
                this.qqConfig = this.gson.fromJson(qqConfigData,QQConfig.class);
            }catch(Exception e){
            }
        }
        if(this.qqConfig == null){
            this.qqConfig = new QQConfig();
        }
        try {
            PackageInfo qqPackageInfo = super.getApplicationContext().getPackageManager().getPackageInfo(QQParams.PACKAGENAME, 0);
            this.qqConfig.setVersionCode(qqPackageInfo.versionCode);
            this.qqConfig.setVersionName(qqPackageInfo.versionName);
            Utils_Log.i("qq version: "+qqPackageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



    }

    public QQConfig getQqConfig() {
        return qqConfig;
    }

    public void setQqConfig(QQConfig qqConfig) {
        this.qqConfig = qqConfig;
        this.editor.putString(App.SP_QQCONFIG,this.gson.toJson(this.qqConfig));
        this.editor.commit();
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
        this.editor.putString(App.SP_BASECONFIG,this.gson.toJson(this.baseConfig));
        this.editor.commit();
    }

    public WeChatConfig getWeChatConfig() {
        return weChatConfig;
    }

    public void setWeChatConfig(WeChatConfig weChatConfig) {
        this.weChatConfig = weChatConfig;
        this.editor.putString(App.SP_WECHATCONFIG,this.gson.toJson(this.weChatConfig));
        this.editor.commit();
    }



    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(getApplicationContext(),
                Activity_Main.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                0); // 退出程序
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用
        Utils_Activity.finishAll();
    }
}
