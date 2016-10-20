package cn.thinkbear.app.grabredpacket.share.activity;


import android.app.Activity;
import android.os.Bundle;

import cn.thinkbear.app.grabredpacket.utils.Utils_Activity;

/**
 *
 * @author ThinkBear
 */

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.hasSavedInstanceState(savedInstanceState);
		int layoutId = this.getContentViewId();
		if(layoutId>0){
			super.setContentView(this.getContentViewId());
		}
		this.doInitView();
		this.doSetView();
		Utils_Activity.addActivity(this);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utils_Activity.removeActivity(this);
	}

	protected abstract int getContentViewId();
	protected abstract void doInitView();
	protected abstract void doSetView();
	protected void hasSavedInstanceState(Bundle savedInstanceState){

	}
}
