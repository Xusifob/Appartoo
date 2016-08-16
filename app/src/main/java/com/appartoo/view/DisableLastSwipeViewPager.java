package com.appartoo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by alexandre on 16-07-19.
 */
public class DisableLastSwipeViewPager extends ViewPager {

    private float initialXValue;

    public DisableLastSwipeViewPager(Context context) {
        super(context);
    }

    public DisableLastSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if(getCurrentItem() < getChildCount() - 2) return true;

        if(getCurrentItem() == getChildCount() - 1) return false;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(getCurrentItem() == getChildCount() - 2) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                try {
                    float diffX = event.getX() - initialXValue;
                    if (diffX < 0) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}