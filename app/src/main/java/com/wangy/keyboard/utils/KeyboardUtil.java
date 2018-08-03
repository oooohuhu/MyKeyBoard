package com.wangy.keyboard.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.wangy.keyboard.R;

import java.util.List;

public class KeyboardUtil {
    private Context ctx;
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 字符键盘
    //    public boolean isnun = false;// 是否数据键盘
    public boolean isupper = false;// 是否大写
    private KeyboardListener keyboardListener;
    private EditText ed;

    public interface KeyboardListener {
        void onOK();
    }

    public void setKeyboardListener(KeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public KeyboardUtil(Activity act, Context ctx, EditText edit) {
        this.act = act;
        this.ctx = ctx;
        this.ed = edit;
        k1 = new Keyboard(ctx, R.xml.letter);
        k2 = new Keyboard(ctx, R.xml.symbols_number);
        k3 = new Keyboard(ctx, R.xml.symbols_character);
        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        try {
            keyboardView.setKeyboard(k1);
            keyboardView.setEnabled(true);
            keyboardView.setPreviewEnabled(true);
            keyboardView.setOnKeyboardActionListener(listener);
        } catch (Exception ex) {
            Log.e("keyboardexception", ex.toString());
        }

    }
    public KeyboardUtil(Context ctx, View parent, EditText edit) {
        this.ctx = ctx;
        this.ed = edit;
        k1 = new Keyboard(ctx, R.xml.letter);
        k2 = new Keyboard(ctx, R.xml.symbols_number);
        k3 = new Keyboard(ctx, R.xml.symbols_character);
        keyboardView = (KeyboardView) parent.findViewById(R.id.keyboard_view);
        try {
            keyboardView.setKeyboard(k1);
            keyboardView.setEnabled(true);
            keyboardView.setPreviewEnabled(true);
            keyboardView.setOnKeyboardActionListener(listener);
        } catch (Exception ex) {
            Log.e("keyboardexception", ex.toString());
        }
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
            Log.e("keyboard", text.toString());
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
                keyboardListener.onOK();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
                keyboardView.setKeyboard(k1);

            } else if (primaryCode == 123456) {// 数字键盘切换

                keyboardView.setKeyboard(k2);
            } else if (primaryCode == 456456) {// 字母键盘切换
                keyboardView.setKeyboard(k1);
            } else if (primaryCode == 789789) {// 字符键盘切换
                keyboardView.setKeyboard(k3);
            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keylist = k1.getKeys();
        if (isupper) {//大写切换小写
            isupper = false;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {//小写切换大写
            isupper = true;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    public void showKeyboard() {
        try {
            int visibility = keyboardView.getVisibility();
            if (visibility == View.GONE || visibility == View.INVISIBLE) {
                keyboardView.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
            Log.e("keyboard",ex.toString());
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }

}
