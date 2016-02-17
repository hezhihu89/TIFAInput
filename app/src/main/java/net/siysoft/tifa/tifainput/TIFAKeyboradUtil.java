package net.siysoft.tifa.tifainput;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 贺志虎 on 2016/2/16 0016.
 */
public class TIFAKeyboradUtil {

    private static Window mWindow;
    private static Dialog mDialog;
    private static Context mContext;
    private static TIFAKeyboardView keyboardView;
    private TIFAEditText mEditText;
    private static TIFAKeyboradUtil keyboradUtil;
    public static boolean isnun = false;// 是否数据键盘
    public static boolean isupper = false;// 是否大写
    private static Keyboard key_english;// 字母键盘
    private static Keyboard key_random_num; //随机数字键盘
    private static Keyboard symbols, symbols_shift;// 数字键盘及第二个菜单
    private static String TAG = TIFAKeyboradUtil.getInstance().getClass().getSimpleName();
    private static InputType type = InputType.TYPE_KEY_EN;
    private static HashMap<EditText, StringBuffer> stringMap;
    private static Editable myEditable;

    //=====================================================================================
    // 输入法类型
    static enum InputType {
        TYPE_KEY_EN,
        TYPE_KEY_NUM
    }

    //======================================================================================
    public static TIFAKeyboradUtil getInstance() {
        if (keyboradUtil == null) {
            synchronized (TIFAKeyboradUtil.class) {
                if (keyboradUtil == null) {
                    stringMap = new HashMap<>();
                    return keyboradUtil = new TIFAKeyboradUtil();
                }
            }
        }
        return keyboradUtil;
    }

    /**
     * 设置  绑定 editText
     *
     * @param context
     * @param editText
     */
    TIFAKeyboradUtil setEdit(Context context, TIFAEditText editText) {
        this.mEditText = editText;
        this.mContext = context;
        //如果不含有这个edit 则进行edit和 stringbuff 的绑定
        if (!stringMap.containsKey(editText)) {
            Log.d(TAG, "不含有 再保存");
            stringMap.put(editText, new StringBuffer());
        }
        if (mDialog == null) {
            create(context);
        }
        return keyboradUtil;
    }

    /**
     * 创建键盘
     *
     * @param context
     */
    private void create(Context context) {

        mDialog = new Dialog(context);
        //设置没有提示框
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mWindow = mDialog.getWindow();
        mDialog.setContentView(getContentView(context));
        mDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.width = params.MATCH_PARENT;
        params.height = params.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        //设置背景透明不模糊
        params.dimAmount = 0f;

        mWindow.setWindowAnimations(android.R.style.Animation_InputMethod);
        //设置背景  不设置背景的话会变成小窗口
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        mWindow.setAttributes(params);
        //设置dialog的监听
        setInputListener();
    }

    public View getContentView(Context context) {
        //------键盘表----------------------------------------------
        key_english = new Keyboard(context, R.xml.english);//字母
        symbols = new Keyboard(context, R.xml.symbols); //符号
        symbols_shift = new Keyboard(context, R.xml.symbols_shift); //符号2
        key_random_num = new Keyboard(context, R.xml.randomnum); //数字
        //-----------------------------------------------------------
        View view = View.inflate(context, R.layout.input, null);
        keyboardView = (TIFAKeyboardView) view.findViewById(R.id.keyboard_views);
        keyboardView.setBackgroundColor(Color.parseColor("#FFFF00"));
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);

        return view;
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
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
            Log.d("TAG", "swipeDown()");
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {


            Editable editable = mEditText.getText();
            myEditable = editable;
            //根据edit 找到对应的StringBuffer
            StringBuffer stringBuffer = stringMap.get(mEditText);

            int start = mEditText.getSelectionStart();

            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                // 完成按钮所做的动作
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                // 删除按钮所做的动作
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                        stringBuffer.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                // 大小写切换
                changeKey();
                keyboardView.setKeyboard(key_english);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                // 数字键盘切换,默认是英文键盘
                if (isnun) {
                    isnun = false;
                    keyboardView.setKeyboard(key_english);
                } else {
                    isnun = true;
                    keyboardView.setKeyboard(symbols);
                }
            } else if (primaryCode == -102) {

                keyboardView.setKeyboard(symbols_shift);

            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    //光标左移
                    mEditText.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < mEditText.length()) {
                    //光标右移
                    mEditText.setSelection(start + 1);
                }
            } else {
                if (mEditText.getHideString()) {
                    //将输入到的字符插入到edit中
                    editable.insert(start, Character.toString((char) primaryCode));
                } else {
                    editable.insert(start, "*");

                }
                Log.d(TAG, "开始位置" + start);
                //如果小于lenth 则说明 需要从中间插入-----------------------------------------
                stringBuffer.insert(start, Character.toString((char) primaryCode));
                //===============插入==================================================

            }

        }

    };


    public void show() {
        if (!mDialog.isShowing()) {
            Log.d(TAG, "show()");
            mDialog.show();
        }
    }

    public static boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Keyboard.Key> keylist = key_english.getKeys();
        if (isupper) {// 大写切换小写
            isupper = false;
            for (Keyboard.Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {// 小写切换大写
            isupper = true;
            for (Keyboard.Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }


    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 是否是英文
     *
     * @param str
     * @return
     */
    private boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 获取键盘的高度
     */
    public int getInutMothedHight() {
        //如果键盘是显示的
        if (mDialog.isShowing()) {
            return keyboardView.getHeight();
        }
        return 0;
    }

    /**
     * 设置Dialog的监听
     */
    private void setInputListener() {

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(TAG, "消失了");
            }
        });
        //dialog 显示监听
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (mEditText.getMyInputType() == InputType.TYPE_KEY_NUM) {
                    randomNumber();
                    keyboardView.setKeyboard(key_random_num);

                } else {
                    isnun = false;
                    keyboardView.setKeyboard(key_english);
                }
            }

        });
    }

    /**
     * 设置输入发类型
     *
     * @param type InputType.TYPE_KEY_EN, 普通英文输入法
     *             InputType.TYPE_KEY_NUM， 数字输入法
     */
    public void setInputType(InputType type) {
        this.type = type;
    }

    /**
     * 随机换位数字
     */
    private void randomNumber() {
        List<Keyboard.Key> keyList = key_random_num.getKeys();

        int size = keyList.size();
        for (int i = 0; i < size; i++) {
            int random_a = (int) (Math.random() * (size));
            int random_b = (int) (Math.random() * (size));

            int code = keyList.get(random_a).codes[0];
            CharSequence label = keyList.get(random_a).label;

            keyList.get(random_a).codes[0] = keyList.get(random_b).codes[0];
            keyList.get(random_a).label = keyList.get(random_b).label;

            keyList.get(random_b).codes[0] = code;
            keyList.get(random_b).label = label;
        }
    }

    public StringBuffer getText(TIFAEditText editText) {
        if (stringMap.containsKey(editText)) {
            return stringMap.get(editText);
        }
        return null;
    }

}
