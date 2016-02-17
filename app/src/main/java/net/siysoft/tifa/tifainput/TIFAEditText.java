package net.siysoft.tifa.tifainput;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by 贺志虎 on 2016/2/16 0016.
 */
public class TIFAEditText extends EditText {
    private Context mContext;
    private boolean mHideString = false;
    private TIFAKeyboradUtil.InputType type = TIFAKeyboradUtil.InputType.TYPE_KEY_EN;


    public TIFAEditText(Context context) {
        this(context, null);
    }

    public TIFAEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        onCreate();
    }

    /**
     * 创建配置参数
     */
    private void onCreate() {
        // 处理键盘弹出光标问题
        maskingInputMothed();
        //设置点击事件
        setOnClickListener(clicklistener);
    }

    /**
     * 点击edit之后 禁止弹出 系统键盘
     * 并且  处理光标
     */
    private void maskingInputMothed() {

        if (android.os.Build.VERSION.SDK_INT <= 10) {
            setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(this, false);
            } catch (Exception e) {
                // TODO: handle exception
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(this, false);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private OnClickListener clicklistener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TIFAKeyboradUtil.getInstance().setEdit(mContext, TIFAEditText.this).show();
            //如果键盘显示状态
            if (TIFAKeyboradUtil.isShowing()) {
                //获取键盘高度，获取Edit的高度
                Log.d("TAG", TIFAKeyboradUtil.getInstance().getInutMothedHight() + "");
            }
        }
    };

    /**
     * 设置键盘输入类型
     *
     * @param type
     */
    public void setInputType(TIFAKeyboradUtil.InputType type) {
        this.type = type;
    }

    public TIFAKeyboradUtil.InputType getMyInputType() {
        return type;
    }

    /**
     * 获取字符
     */
    public String getDefaultText() {
        if (TIFAKeyboradUtil.getInstance().getText(this).toString().length() != 0) {
            return TIFAKeyboradUtil.getInstance().getText(this).toString();
        }
        return "";

    }

    /**
     * 隐藏输入的字符
     * 默认为显示
     *
     * @param visi
     */
    public void setHideString(boolean visi) {
        this.mHideString = visi;
    }

    public boolean getHideString() {
        return mHideString;
    }

    public String getMD5() {

        return MD5Utile.encodeByMD5(TIFAKeyboradUtil.getInstance().getText(this).toString());

    }
}
