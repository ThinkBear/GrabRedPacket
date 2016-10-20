package cn.thinkbear.app.grabredpacket.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;


import cn.thinkbear.app.grabredpacket.R;

import java.util.Random;

/**
 * 动画 工具类
 * @author ThinkBear
 */

public class Utils_Anim {
    private static final int[] ANIMS = {R.anim.alpha_short, R.anim.alpha_translate_short, R.anim.rotate_short, R.anim.rotate_translate_short, R.anim.translate_short, R.anim.scale_short};

    public static void startRandomAnimation(Context context, View view) {
        view.startAnimation(AnimationUtils.loadAnimation(context, ANIMS[new Random().nextInt(ANIMS.length)]));
    }
    public static void startAnimation(Context context, View view,int animId) {
        view.startAnimation(AnimationUtils.loadAnimation(context, animId));
    }

}
