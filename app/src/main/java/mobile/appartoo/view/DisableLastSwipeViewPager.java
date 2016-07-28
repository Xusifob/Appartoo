package mobile.appartoo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by alexandre on 16-07-19.
 */
public class DisableLastSwipeViewPager extends ViewPager {

    public DisableLastSwipeViewPager(Context context) {
        super(context);
    }

    public DisableLastSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(getCurrentItem() == getChildCount()-1) {
            return false;
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(getCurrentItem() == getChildCount()-1) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }
}