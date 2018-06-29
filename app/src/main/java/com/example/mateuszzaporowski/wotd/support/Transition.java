package com.example.mateuszzaporowski.wotd.support;

import android.app.Fragment;
import android.app.FragmentManager;

import com.example.mateuszzaporowski.wotd.R;

/**
 * Created by mateuszzaporowski on 12.05.18.
 */

public class Transition {
    static public void makeATransition(FragmentManager fm, Fragment fg, String nameOfStack) {
        fm.beginTransaction()
                .setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left)
                .replace(R.id.content, fg)
                .addToBackStack(nameOfStack)
                .commit();
    }
}