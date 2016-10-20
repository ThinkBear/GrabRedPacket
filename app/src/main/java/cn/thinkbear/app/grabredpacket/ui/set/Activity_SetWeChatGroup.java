package cn.thinkbear.app.grabredpacket.ui.set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.adapter.Adapter_SetGroup;
import cn.thinkbear.app.grabredpacket.ui.App;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.dialog.DialogFactory;
import cn.thinkbear.app.grabredpacket.share.view.Widget_ActivityBar;
import cn.thinkbear.app.grabredpacket.utils.Utils_Static;
import cn.thinkbear.app.grabredpacket.vo.WeChatConfig;

/**
 * 微信端设置 -- 高级设置 -- 群过滤设置
 * @author ThinkBear
 */

public class Activity_SetWeChatGroup extends BaseActivity{

    private Widget_ActivityBar bar = null;
    private EditText groupName = null;
    private TextView addGroup = null;
    private RadioGroup optionGroup = null;
    private ListView main = null;
    private MyClickEvent myClickEvent = null;

    private App app = null;
    private WeChatConfig config = null;

    private Adapter_SetGroup adapter = null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_group;
    }

    @Override
    protected void doInitView() {
        this.app = (App) super.getApplication();
        this.config = this.app.getWeChatConfig();

        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);
        this.groupName = (EditText) super.findViewById(R.id.groupName);
        this.addGroup = (TextView) super.findViewById(R.id.addGroup);
        this.optionGroup = (RadioGroup) super.findViewById(R.id.optionGroup);

        this.main = (ListView) super.findViewById(R.id.main);
        this.myClickEvent = new MyClickEvent();

        this.adapter = new Adapter_SetGroup(super.getApplicationContext(),this.config.getAllGroup());
    }

    @Override
    protected void doSetView() {
        this.bar.setTitle(R.string.setGroupTitle);
        this.addGroup.setOnClickListener(this.myClickEvent);

        this.main.setAdapter(this.adapter);
        this.adapter.setOnCallback(new MyCallback());
        this.optionGroup.setOnCheckedChangeListener(new MyCheckedChangeEvent());
        this.optionGroup.check(this.config.isOptionGroup()?R.id.optionA:R.id.optionB);
    }

    private class MyCheckedChangeEvent implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            config.setOptionGroup(checkedId == R.id.optionA);
        }
    }

    private class MyCallback implements Adapter_SetGroup.OnCallback{

        @Override
        public void deleteEvent(final String groupNameStr) {
            DialogFactory dialog = DialogFactory.getInstance(Activity_SetWeChatGroup.this);
            dialog.setMessage("确定要删除群名["+groupNameStr+"]？");
            dialog.setYesAndNoMode("删除", "取消", new DialogFactory.OnYesCallback() {
                @Override
                public void doYesClick(Dialog dialog) {
                    dialog.dismiss();
                    config.getAllGroup().remove(groupNameStr);
                    app.setWeChatConfig(config);
                    adapter.notifyDataSetChanged();
                }
            },null);
        }
    }

    private class MyClickEvent implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.addGroup:
                    String groupNameStr = groupName.getText().toString().trim();
                    if(TextUtils.isEmpty(groupNameStr)){
                        Toast.makeText(getApplicationContext(),"群名不能为空，请输入",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(config.getAllGroup().contains(groupNameStr)){
                        Toast.makeText(getApplicationContext(),"群名不能为空，请输入",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    groupName.setText("");
                    Utils_Static.closeInputMethod(Activity_SetWeChatGroup.this);
                    config.getAllGroup().add(groupNameStr);
                    app.setWeChatConfig(config);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Activity_SetWeChatGroup.class);
        activity.startActivity(intent);
    }
}
