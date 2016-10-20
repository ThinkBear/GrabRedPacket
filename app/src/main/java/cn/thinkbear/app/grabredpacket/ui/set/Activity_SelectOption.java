package cn.thinkbear.app.grabredpacket.ui.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.adapter.Adapter_SelectOption;
import cn.thinkbear.app.grabredpacket.ui.App;
import cn.thinkbear.app.grabredpacket.share.activity.BaseActivity;
import cn.thinkbear.app.grabredpacket.share.view.Widget_ActivityBar;

/**
 * QQ和微信设置 -- 普通设置 -- 子项选择
 * @author ThinkBear
 */

public class Activity_SelectOption extends BaseActivity {
    private Widget_ActivityBar bar = null;
    private ListView main = null;
    private int titleId = 0;
    private int items[] = null;
    private int selectIndex = 0;

    @Override
    protected void hasSavedInstanceState(Bundle savedInstanceState) {
        super.hasSavedInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            this.titleId = savedInstanceState.getInt(App.TITLE);
            this.items = savedInstanceState.getIntArray(App.ITEMS);
            this.selectIndex = savedInstanceState.getInt(App.SELECTINDEX);
        }else{
            this.titleId = super.getIntent().getIntExtra(App.TITLE,0);
            this.items = super.getIntent().getIntArrayExtra(App.ITEMS);
            this.selectIndex = super.getIntent().getIntExtra(App.SELECTINDEX,0);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_select_option;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(App.TITLE,this.titleId);
        outState.putIntArray(App.ITEMS,this.items);
        outState.putInt(App.SELECTINDEX,this.selectIndex);
        super.onSaveInstanceState(outState);
    }



    @Override
    protected void doInitView() {
        this.bar = (Widget_ActivityBar) super.findViewById(R.id.bar);
        this.main = (ListView) super.findViewById(R.id.main);
    }

    @Override
    protected void doSetView() {
        this.bar.setTitle(this.titleId);
        this.main.setAdapter(new Adapter_SelectOption(super.getApplicationContext(),this.items,this.selectIndex));
        this.main.setOnItemClickListener(new MyItemClickEvent());
    }

    private class MyItemClickEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent data = new Intent();
            data.putExtra(App.SELECTINDEX,i);
            setResult(App.SELECT_OPTION_RESULT,data);
            finish();
        }
    }

    public static void startActivity(Activity activity,int titleId,int items[],int selectIndex,int requestCode) {
        Intent intent = new Intent(activity, Activity_SelectOption.class);
        intent.putExtra(App.TITLE,titleId);
        intent.putExtra(App.ITEMS,items);
        intent.putExtra(App.SELECTINDEX,selectIndex);
        activity.startActivityForResult(intent,requestCode);
    }

}
