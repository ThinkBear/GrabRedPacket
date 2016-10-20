package cn.thinkbear.app.grabredpacket.ui.orther;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.adapter.Adapter_Use;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.view.Widget_ActivityBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户须知
 * @author ThinkBear
 */

public class Activity_Use extends BaseActivity {
    private Widget_ActivityBar bar = null;
    private ListView main = null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_use;
    }

    @Override
    protected void doInitView() {
        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);
        this.main = (ListView) super.findViewById(R.id.main);
    }

    @Override
    protected void doSetView() {
        this.bar.setTitle(R.string.useTitle);
        List<String> all = new ArrayList<String>();
        String appName = super.getResources().getString(R.string.app_name);

        all.add(super.getString(R.string.use_1,appName));
        all.add(super.getString(R.string.use_2,appName));
        all.add(super.getString(R.string.use_3,appName));
        all.add(super.getString(R.string.use_4,appName));

        this.main.setAdapter(new Adapter_Use(super.getApplicationContext(),all));
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Activity_Use.class);
        activity.startActivity(intent);
    }
}
