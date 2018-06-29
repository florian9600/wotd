package com.example.mateuszzaporowski.wotd.support;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mateuszzaporowski on 17.05.18.
 */

public class Margin {
    static public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
