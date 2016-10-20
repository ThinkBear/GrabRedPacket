package cn.thinkbear.app.grabredpacket.share.view;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;

/**
 * 自定义标题栏
 * @author ThinkBear
 */

public class Widget_ActivityBar extends LinearLayout {
	private TextView back = null;
	private TextView title = null;
	private OnBackCallback callback = null;

	public Widget_ActivityBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.back = (TextView) super.findViewById(R.id.back);
		this.title = (TextView) super.findViewById(R.id.title);
		this.back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.doBackEvent();
				} else {
					((Activity) getContext()).finish();
				}
			}
		});
	}

	public void setTitle(String titleStr) {
		this.title.setText(titleStr);
	}

	public void setTitle(int titleId) {
		this.title.setText(titleId);
	}

	public void setOnBackCallback(OnBackCallback callback) {
		this.callback = callback;
	}

	public interface OnBackCallback {
		public void doBackEvent();
	}
}
