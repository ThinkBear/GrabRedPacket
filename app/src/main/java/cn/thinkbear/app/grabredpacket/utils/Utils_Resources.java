package cn.thinkbear.app.grabredpacket.utils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;


/**
 * 资源 工具类
 * @author ThinkBear
 */

public class Utils_Resources {

    public static Drawable getDrawableBySize(Resources res,int drawId, int size) {
        return getDrawableByWidthAndHeight(res, drawId, size, size);
    }

    public static Drawable getDrawableByWidthAndHeight(Resources res,int drawId, int width, int height) {
        Drawable draw = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            draw = res.getDrawable(drawId, null);
        } else {
            draw = res.getDrawable(drawId);
        }
        draw.setBounds(0, 0, width, height);
        return draw;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawableBySizeId(Resources res,int drawId, int sizeId) {
        int size = res.getDimensionPixelSize(sizeId);
        return getDrawableByWidthAndHeight(res, drawId, size, size);
    }
}
