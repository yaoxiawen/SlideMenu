package com.yxw.slidemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.yxw.slidemenu.anim.ScrollAnimation;

public class SlideMenu extends FrameLayout {
    private View mainView;
    private View menuView;
    private int downX;
    private int scroll;
    private int menuWidth;
    private Scroller scroller;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    /**
     * 当1级的子view全部加载完时会调用该方法
     * 因此该方法可以写初始化子view的引用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取1级的子view
        mainView = getChildAt(0);
        menuView = getChildAt(1);
        //获取布局文件中为定值的宽高，通过获取布局参数对象获取指定的宽高
        menuWidth = menuView.getLayoutParams().width;
    }

//    /**
//     * 参数widthMeasureSpec和heightMeasureSpec是系统测量viewgroup时传入的参数
//     * 就是viewgroup的宽高，viewgroup在布局文件中宽高都为match_parent
//     * 所以参数widthMeasureSpec和heightMeasureSpec也就是屏幕的宽高
//     *
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        //需要测量所有子view的宽高
//        //mainView的布局文件中宽高都为match_parent，所以可以直接用测量参数
//        mainView.measure(widthMeasureSpec, heightMeasureSpec);
//        menuView.measure(menuWidth, heightMeasureSpec);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //onLayout方法在onMeasure方法之后调用，所以已经测量完了，
        // 可以用getMeasuredWidth()获取子view的宽高
        mainView.layout(0, 0, r, b);
        //因为布局文件中宽高都为match_parent，也可以直接用参数
        //mainView.layout(0, 0, r, b);
        menuView.layout(-menuWidth, 0, 0, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://DOWN不拦截，把downX值获取了即可
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE://MOVE要拦截
                if (Math.abs(ev.getX() - downX) >10) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int newX = (int) event.getX();
                int dx = newX - downX;
                //反方向
                scroll = getScrollX() - dx;
                //不能直接break，break的话，scroll的值还是在变的，会拉不回来
                if (scroll > 0) {
                    scroll = 0;
                }
                if (scroll < -menuWidth) {
                    scroll = -menuWidth;
                }
                scrollTo(scroll, 0);
                downX = newX;
                break;
            case MotionEvent.ACTION_UP:
                //1、使用自定义动画
//                ScrollAnimation animation;
//                if (scroll<-menuWidth/2){
//                    animation = new ScrollAnimation(this,scroll,-menuWidth);
//                }else{
//                    animation = new ScrollAnimation(this,scroll,0);
//                }
//                startAnimation(animation);
                //2、使用Scroller
                if (scroll < -menuWidth / 2) {
                    //注意第三个参数是dx
                    scroller.startScroll(scroll, 0, -menuWidth - scroll, 0,
                            Math.abs(-menuWidth - scroll) * 2);
                    invalidate();
                } else {
                    scroller.startScroll(scroll, 0, - scroll, 0,
                            Math.abs(- scroll) * 2);
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 类似于自定义动画中的applyTransformation方法
     * 由于scroller不会去主动调用该方法
     * 而invalidate()会调用该方法
     * invalidate->draw->computeScroll
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //scroller.computeScrollOffset()返回true，表示动画没结束
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            //一直迭代调用
            invalidate();
        }
    }

    public void switchMenu() {
        if (getScrollX()==0){
            scroller.startScroll(0, 0, -menuWidth, 0,
                    Math.abs(-menuWidth));
            invalidate();
        }else{
            scroller.startScroll(-menuWidth, 0, menuWidth, 0,
                    Math.abs(-menuWidth));
            invalidate();
        }
    }
}
