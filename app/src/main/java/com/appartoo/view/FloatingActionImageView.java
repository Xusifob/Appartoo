package com.appartoo.view;

/**
 * Created by alexandre on 16-08-22.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.appartoo.R;
import com.appartoo.utils.ImageManager;

/**
 * Sked Series, All rights Reserved
 * Created by Sanjeet on 06-Jan-16.
 */
public class FloatingActionImageView extends FloatingActionButton {
    private Context context;

    public FloatingActionImageView(Context context) {
        super(context);
        this.context = context;
    }

    public FloatingActionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FloatingActionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = null;
        if (b != null) {
            bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            BitmapDrawable bitmapDrawable = null;
            bitmapDrawable = ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.default_profile_picture));

            if (bitmapDrawable != null) {
                bitmap = bitmapDrawable.getBitmap();
            }
        }

        int w = getWidth();

        int border = (int) ImageManager.convertDpToPixel(4, context);
        Bitmap roundBitmap = ImageManager.getCroppedBitmap(bitmap, w - border*2);
        canvas.drawBitmap(roundBitmap, border, border, null);


    }
}
