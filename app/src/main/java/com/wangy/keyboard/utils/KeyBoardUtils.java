package com.wangy.keyboard.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.wangy.keyboard.R;

import java.lang.reflect.Method;


/**
 * author wangy
 * Created on 2018\5\4 0004.
 * description
 */

public class KeyBoardUtils {

    //    private static Dialog mDialog;
    private static EditText et_amount;
    private static KeyboardUtil keyboardUtil;
    private static LinearLayout rl_keyboard;
    private static BottomSheetDialog mDialog;

    public static void initkeyboard(Context context, int ispassword) {
//ispassword 1为密文 其余的为明文
        mDialog = new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);
        mDialog.clearBg();
        View root = LayoutInflater.from(context).inflate(R.layout.keyboard, null);

//        mDialog = new Dialog(context, R.style.keyboard_dialog);
//        RelativeLayout root = (RelativeLayout) LayoutInflater.from(context).inflate(
//                R.layout.keyboard, null);
        et_amount = (EditText) root.findViewById(R.id.et_amount);
        if (ispassword == 1) {
            et_amount.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            et_amount.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et_amount.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(et_amount, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final KeyboardUtil keyboardUtil = new KeyboardUtil(context, root, et_amount);
        keyboardUtil.showKeyboard();
        rl_keyboard = (LinearLayout) root.findViewById(R.id.rl__keyboard);
        keyboardUtil.setKeyboardListener(new KeyboardUtil.KeyboardListener() {
            @Override
            public void onOK() {
                String result = et_amount.getText().toString();
                String msg = "";
                if (!TextUtils.isEmpty(result)) {
                    msg += result;
                    keyboardback.onkeyboardOK(msg);
//                    JSONObject object = new JSONObject();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("keyboardstr", msg);
//                    object.put("data", jsonObject);
//                    callBackFunction.onCallBack(object.toJSONString());
                }
                mDialog.dismiss();
                keyboardUtil.hideKeyboard();
            }
        });
        mDialog.contentView(root);
//        mDialog.setContentView(root);
//        Window dialogWindow = mDialog.getWindow();
//        dialogWindow.setGravity(Gravity.BOTTOM);
////        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = 0; // 新位置Y坐标
//        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
//        root.measure(0, 0);
//        lp.height = root.getMeasuredHeight();
////        lp.alpha = 9f; // 透明度
//        dialogWindow.setAttributes(lp);
    }

    private static KeyBoardResultbackInterface keyboardback;

    public static void setKeyboardBack(KeyBoardResultbackInterface keyboardBack) {
        keyboardback = keyboardBack;
    }

    public static void showKeyboard() {
        if (mDialog != null) {
            mDialog.show();
//            keyboardUtil.showKeyboard();
            et_amount.setText("");
        }
    }

    public static void hideKeyboard() {
        if (mDialog != null) {
            mDialog.dismiss();
            keyboardUtil.hideKeyboard();
        }
    }
}
