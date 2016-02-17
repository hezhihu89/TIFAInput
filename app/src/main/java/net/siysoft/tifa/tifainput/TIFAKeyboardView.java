package net.siysoft.tifa.tifainput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 贺志虎 on 2016/2/16 0016.
 */
public class TIFAKeyboardView extends KeyboardView {

    private Keyboard currentKeyboard;
    private List<Keyboard.Key> keys = new ArrayList<Keyboard.Key>();
    private int lastKeyIndex = 0;
    private Keyboard.Key focusedKey;

    public TIFAKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        setPreviewEnabled(false);
        currentKeyboard = this.getKeyboard();

        keys = currentKeyboard.getKeys();

        focusedKey = keys.get(lastKeyIndex);

        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        String label = focusedKey.label == null ? null : adjustCase(
                focusedKey.label).toString();
/*
        //获取得到焦点时的.9格式背景图片
        NinePatchDrawable keyBackground = (NinePatchDrawable) getResources()
                .getDrawable(R.drawable.keyboard_letter_focus);


        final Rect bounds = keyBackground.getBounds();
        if (focusedKey.width != bounds.right
                || focusedKey.height != bounds.bottom) {
            keyBackground.setBounds(0, 0, focusedKey.width, focusedKey.height);
        }

        canvas.translate(focusedKey.x, focusedKey.y);
*/
        //    keyBackground.draw(canvas);

        Paint paint = new Paint();

        if (label != null) {

            //    paint.setTextSize((float) 40); // 设置文字大小

            if (label.toString().length() > 1) {
                paint.setTextSize((float) 21); // 设置文字大小
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            }

            paint.setTextAlign(Paint.Align.CENTER); // 设置文字水平居中

            paint.setColor(Color.WHITE); // 设置文字颜色

            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            // 计算文字高度
            float fontHeight = fontMetrics.bottom - fontMetrics.top;

            // 计算文字baseline
            float textBaseY = focusedKey.height
                    - (focusedKey.height - fontHeight) / 2 - fontMetrics.bottom;

            // 重绘文字
            //  canvas.drawText(label, (focusedKey.width) / 2, textBaseY, paint);
        }
    }

    private CharSequence adjustCase(CharSequence label) {
        if (currentKeyboard.isShifted() && label != null && label.length() < 3
                && Character.isLowerCase(label.charAt(0))) {
            label = label.toString().toUpperCase();
        }
        return label;
    }

    public int getLastKeyIndex() {
        return lastKeyIndex;
    }

    public void setLastKeyIndex(int index) {
        this.lastKeyIndex = index;
    }
}
