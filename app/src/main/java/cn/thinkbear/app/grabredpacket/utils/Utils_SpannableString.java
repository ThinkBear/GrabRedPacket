package cn.thinkbear.app.grabredpacket.utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

/**
 * 字体样式 工具类
 * @author ThinkBear
 */

public class Utils_SpannableString {
	private Resources res = null;
	public Utils_SpannableString(Resources res) {
		this.res = res;
	}


	public SpannableString getColorSizeString(String content, int colorId,
			int sizeId) {
		if(content==null){
			content = "";
		}
		SpannableString spanString = this.getColorString(content, colorId);
		spanString.setSpan(
				new AbsoluteSizeSpan(this.res.getDimensionPixelOffset(sizeId)),
				0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}

	public SpannableString getColorString(String content, int colorId) {
		if(content==null){
			content = "";
		}
		SpannableString spanString = new SpannableString(content);
		spanString.setSpan(new ForegroundColorSpan(this.res.getColor(colorId)),
				0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}

	public SpannableString getColorSizeBoldString(String content, int colorId,
			int sizeId) {
		if(content==null){
			content = "";
		}
		SpannableString spanString = this.getColorSizeString(content, colorId,
				sizeId);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;

	}

	public SpannableString getColorSizeStrikeString(String content,int colorId,int sizeId){
		if(content==null){
			content = "";
		}
		SpannableString spanString = this.getColorSizeString(content, colorId,
				sizeId);
		spanString.setSpan(new StrikethroughSpan(), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}



}
