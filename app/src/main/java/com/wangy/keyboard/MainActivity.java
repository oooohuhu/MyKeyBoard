package com.wangy.keyboard;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.wangy.keyboard.utils.BottomSheetDialog;
import com.wangy.keyboard.utils.KeyBoardResultbackInterface;
import com.wangy.keyboard.utils.KeyBoardUtils;
import com.wangy.keyboard.utils.KeyboardUtil;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private View rootview;
    private static BottomSheetDialog mDialog;
    private KeyboardUtil keyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edittext);
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        findViewById(R.id.keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //软键盘自带edittext
                KeyBoardUtils.initkeyboard(MainActivity.this, 2);
                KeyBoardUtils.showKeyboard();
                KeyBoardUtils.setKeyboardBack(new KeyBoardResultbackInterface() {
                    @Override
                    public void onkeyboardOK(String result) {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        findViewById(R.id.keyboard2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //软键盘不带edittext
                initDialog();
                mDialog.show();
            }
        });
    }

    private void initDialog() {
        rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.keyboard2, null);
        mDialog = new BottomSheetDialog(MainActivity.this, R.style.Material_App_BottomSheetDialog);
        mDialog.clearBg();
        keyboardUtil = new KeyboardUtil(MainActivity.this, rootview, editText);
        keyboardUtil.showKeyboard();
        keyboardUtil.setKeyboardListener(new KeyboardUtil.KeyboardListener() {
            @Override
            public void onOK() {
                keyboardUtil.hideKeyboard();
                if (mDialog != null) {
                    mDialog.dismiss();
                    mDialog = null;
                }
            }
        });
        mDialog.contentView(rootview);
    }
}
