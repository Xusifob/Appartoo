package mobile.appartoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mobile.appartoo.activity.OfferImagesActivity;
import mobile.appartoo.view.ZoomableImageView;

/**
 * Created by alexandre on 16-07-12.
 */
public class ImageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] resources;
    private boolean crop;
    private boolean isZoomable;

    public ImageViewPagerAdapter(Context context, int[] resources) {
        this.resources = resources;
        this.context = context;
    }

    @Override
    public int getCount() {
        return resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resources[position]);

        if(crop) {

        }

        if(context instanceof OfferImagesActivity) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            ZoomableImageView img = new ZoomableImageView(context);
            img.setImageBitmap(bitmap);
            img.setMaxZoom(4f);
            container.addView(img);
            return img;
        } else {
            bitmap = bitmapToSquare(bitmap);
            ImageView imageView = new ImageView(context);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OfferImagesActivity.class);
                    intent.putExtra("resources", resources);
                    intent.putExtra("currentImage", position);
                    context.startActivity(intent);
                }
            });
            imageView.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
            container.addView(imageView);
            return imageView;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    private Bitmap bitmapToSquare(Bitmap bitmap){

        if (bitmap.getWidth() >= bitmap.getHeight()){
            bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/2 - bitmap.getHeight()/2, 0, bitmap.getHeight(), bitmap.getHeight());
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2 - bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth());
        }

        return bitmap;
    }
}