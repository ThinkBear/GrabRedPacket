package cn.thinkbear.app.grabredpacket.ui;

import android.view.View;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.vo.BaseConfig;

/**
 * 免责声明 （确认页面）
 * @author ThinkBear
 */

public class Activity_Agreement extends BaseActivity {
    private TextView content = null;
    private TextView no = null;
    private TextView yes = null;
    private MyClickEvent myClickEvent = null;
    private App app = null;
    private BaseConfig config = null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void doInitView() {
        this.app = (App) super.getApplication();
        this.config = this.app.getBaseConfig();
        this.content = (TextView) super.findViewById(R.id.content);
        this.no = (TextView) super.findViewById(R.id.no);
        this.yes = (TextView) super.findViewById(R.id.yes);
        this.myClickEvent = new MyClickEvent();
    }

    @Override
    protected void doSetView() {
        if(this.config.isAgreement()){
            Activity_Main.startActivity(Activity_Agreement.this);
            finish();
            return;
        }
        this.no.setOnClickListener(this.myClickEvent);
        this.yes.setOnClickListener(this.myClickEvent);
        this.content.setText(super.getString(R.string.agreementContent,getString(R.string.app_name)));

    }

    private class MyClickEvent implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.no:
                    finish();
                    break;
                case R.id.yes:
                    config.setAgreement(true);
                    app.setBaseConfig(config);
                    Activity_Main.startActivity(Activity_Agreement.this);
                    break;
            }
        }
    }


}
