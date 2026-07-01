package com.vypeensoft.diceroller.utils;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class AnimationUtils {

    public static void rotateView(View view, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", 0f, 1440f);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
