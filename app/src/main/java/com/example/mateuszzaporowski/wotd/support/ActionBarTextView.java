package com.example.mateuszzaporowski.wotd.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mateuszzaporowski on 12.05.18.
 */

@SuppressLint("AppCompatCustomView")
public class ActionBarTextView extends TextView {
    public ActionBarTextView(Context context) {
        super(context);
    }

    public ActionBarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ActionBarTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTextFixed(CharSequence text) {
        super.setText(" " + text + " ");
    }
}
