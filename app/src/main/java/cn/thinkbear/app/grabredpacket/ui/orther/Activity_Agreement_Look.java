package cn.thinkbear.app.grabredpacket.ui.orther;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.view.Widget_ActivityBar;

/**
 * 免责声明
 * @author ThinkBear
 */

public class Activity_Agreement_Look extends BaseActivity {
    private Widget_ActivityBar bar = null;
    private TextView content = null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_agreement_look;
    }

    @Override
    protected void doInitView() {
        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);
        this.content = (TextView) super.findViewById(R.id.content);
    }

    @Override
    protected void doSetView() {
        this.bar.setTitle(R.string.app_name);
        this.content.setText(super.getString(R.string.agreementContent,getString(R.string.app_name)));

    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Activity_Agreement_Look.class);
        activity.startActivity(intent);
    }
}
