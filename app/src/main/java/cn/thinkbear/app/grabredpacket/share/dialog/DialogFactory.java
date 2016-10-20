package cn.thinkbear.app.grabredpacket.share.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.utils.Utils_Static;

/**
 *
 * @author ThinkBear
 */

public class DialogFactory {

    private Activity activity = null;
    private AlertDialog dialog = null;
    private TextView messageView = null;
    private TextView yes = null;
    private TextView no = null;
    private OnYesCallback onYesCallback = null;
    private OnNoCallback onNoCallback = null;
    private MyClickEvent myClickEvent = null;

    private DialogFactory(Activity activity) {
        this.activity = activity;
        this.doInitView();
        this.doSetView();
    }

    public static DialogFactory getInstance(Activity activity) {
        return new DialogFactory(activity);
    }

    private void doInitView() {
        this.dialog = new AlertDialog.Builder(activity).create();
        this.dialog.show();
        // 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
        window.setWindowAnimations(R.style.popup_anim_style_a);
        window.setContentView(R.layout.popup_dialog);
        this.messageView = (TextView) window.findViewById(R.id.message);
        this.yes = (TextView) window.findViewById(R.id.yes);
        this.no = (TextView) window.findViewById(R.id.no);
        this.myClickEvent = new MyClickEvent();
    }

    private void doSetView() {
        int width = Utils_Static.getFillWidth(activity);
        this.messageView.setLayoutParams(new LinearLayout.LayoutParams(
                width * 4 / 5,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        this.yes.setOnClickListener(this.myClickEvent);
        this.no.setOnClickListener(this.myClickEvent);
    }




    private class MyClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    if (onYesCallback != null) {
                        onYesCallback.doYesClick(dialog);
                    } else {
                        dialog.dismiss();
                    }
                    break;
                case R.id.no:
                    if (onNoCallback != null) {
                        onNoCallback.doNoClick(dialog);
                    } else {
                        dialog.dismiss();
                    }
                    break;
            }
        }

    }

    public void setYesMode(String text, OnYesCallback onYesCallback) {
        this.onYesCallback = onYesCallback;
        this.setYesMode(text);
    }

    public void setYesMode(String text) {
        this.yes.setText(text);
        this.setYesMode();
    }

    private void setYesMode() {
        this.yes.setBackgroundResource(R.drawable.pressed_dialog_yes2);
        this.no.setVisibility(View.GONE);
    }


    public void setYesAndNoMode(String yesText, String noText, OnYesCallback onYesCallback, OnNoCallback onNoCallback) {
        this.onYesCallback = onYesCallback;
        this.onNoCallback = onNoCallback;
        this.setYesAndNoMode(yesText, noText);
    }

    public void setYesAndNoMode(String yesText, String noText) {
        this.yes.setText(yesText);
        this.no.setText(noText);
        this.setYesAndNoMode();
    }

    private void setYesAndNoMode() {
        this.yes.setBackgroundResource(R.drawable.pressed_dialog_yes);
        this.no.setBackgroundResource(R.drawable.pressed_dialog_no);
    }

    public static void showConfirmDialog(Activity activity, String msg) {
        showConfirmDialog(activity, msg, Gravity.CENTER);
    }

    public static void showConfirmDialog(Activity activity, int stringId) {
        showConfirmDialog(activity, stringId, Gravity.CENTER);
    }

    public static void showConfirmDialog(Activity activity, int stringId, int gravity) {
        final DialogFactory dialog = DialogFactory.getInstance(activity);
        dialog.setMessage(stringId, gravity);
        dialog.setYesMode("确定");
    }

    public static void showConfirmDialog(Activity activity, String msg, int gravity) {
        final DialogFactory dialog = DialogFactory.getInstance(activity);
        dialog.setMessage(msg, gravity);
        dialog.setYesMode("确定");
    }



    public void setMessage(int stringId) {
        setMessage(stringId, Gravity.CENTER);
    }

    public void setMessage(String message) {
        setMessage(message, Gravity.CENTER);
    }

    public void setMessage(int stringId, int gravity) {
        messageView.setText(stringId);
        messageView.setGravity(gravity);
    }

    public void setMessage(String message, int gravity) {
        messageView.setText(message);
        messageView.setGravity(gravity);
    }

    public void setCancel(boolean flag) {
        this.dialog.setCancelable(flag);
        this.dialog.setCanceledOnTouchOutside(flag);
    }

    public interface OnYesCallback {
        public void doYesClick(Dialog dialog);
    }

    public interface OnNoCallback {
        public void doNoClick(Dialog dialog);
    }
}
