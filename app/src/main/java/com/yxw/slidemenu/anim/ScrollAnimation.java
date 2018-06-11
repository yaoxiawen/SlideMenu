package com.yxw.slidemenu.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ScrollAnimation extends Animation {
    private View view;
    private int startX;
    private int endX;

    public ScrollAnimation(View view, int startX, int endX) {
        this.view = view;
        this.startX = startX;
        this.endX = endX;
        long time = Math.abs(endX - startX) * 2;//动态设置动画时间
        setDuration(time);
    }

    /**
     * 在指定时间内一直执行该方法，直到动画结束
     *
     * @param interpolatedTime 标识动画执行的进度或百分比
     * @param t
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        int currentX = (int) (startX+(endX-startX)*interpolatedTime);
        view.scrollTo(currentX,0);
    }
}
