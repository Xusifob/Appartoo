package com.appartoo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.appartoo.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexandre on 16-08-10.
 */
public class ImageManager {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PICK_IMAGE = 2;

    public static final String TRANFORM_SQUARE = "square";
    public static final String TRANFORM_CIRCLE = "circle";

    public static Bitmap getPictureFromCamera(Activity activity, Uri uri) {

        activity.getContentResolver().notifyChange(uri, null);
        Bitmap imageBitmap;

        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            imageBitmap = rotateImageIfRequired(imageBitmap, uri, null);
            imageBitmap = transformResize(imageBitmap, 1280);
        } catch (IOException e) {
            imageBitmap = null;
            Toast.makeText(activity.getApplicationContext(), R.string.unable_to_load_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return imageBitmap;
    }

    public static Bitmap getPictureFromGallery(Activity activity, Uri uri) {

        activity.getContentResolver().notifyChange(uri, null);
        Bitmap imageBitmap;

        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            imageBitmap = rotateImageIfRequired(imageBitmap, uri, activity);
            imageBitmap = transformResize(imageBitmap, 1280);
        } catch (IOException e) {
            imageBitmap = null;
            Toast.makeText(activity.getApplicationContext(), R.string.unable_to_load_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return imageBitmap;
    }



    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage, Activity activity) throws IOException {

        ExifInterface ei;
        System.out.println("SO !");
        if(activity != null) ei = new ExifInterface(getRealPathFromURI(activity, selectedImage));
        else ei = new ExifInterface(selectedImage.getPath());
        System.out.println("FUCK YOU ! FUCK YOU ! FUCK YOU !!!!!");

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return transformRotate(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return transformRotate(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return transformRotate(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap transformResize(Bitmap bitmap, int scaleSize) {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if(originalHeight > originalWidth) {
            newHeight = scaleSize ;
            multFactor = (float) originalWidth/(float) originalHeight;
            newWidth = (int) (newHeight*multFactor);
        } else if(originalWidth > originalHeight) {
            newWidth = scaleSize ;
            multFactor = (float) originalHeight/ (float)originalWidth;
            newHeight = (int) (newWidth*multFactor);
        } else if(originalHeight == originalWidth) {
            newHeight = scaleSize ;
            newWidth = scaleSize ;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }

    public static Bitmap transformSquare(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {

            bitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        } else {

            bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        return bitmap;
    }

    public static Bitmap transformRotate(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static File bitmapToFile(Bitmap bitmap, Activity activity){
        File filesDir = activity.getFilesDir();
        File imageFile = new File(filesDir, String.valueOf(bitmap.hashCode()) + ".jpg");
        OutputStream outputStream;

        try {
            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return imageFile;
    }

    public static void downloadPictureIntoView(@NonNull final Context context, @NonNull final ImageView imageView, @NonNull String url, final String transformation){
        final String imageUrl = Appartoo.SERVER_URL + RestService.REST_URL + "/upload/" + url;

        RequestCreator requestCreator = Picasso.with(context)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE);

        if(transformation == TRANFORM_SQUARE) requestCreator.transform(new CropSquareTransformation());
        else if (transformation == TRANFORM_CIRCLE) requestCreator.transform(new CircleTransformation());

        requestCreator.into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Log.v("Picasso","Could not fetch image, trying again.");
                        //Try again online if cache failed
                        RequestCreator requestCreatorOnline = Picasso.with(context).load(imageUrl);

                        if(transformation == TRANFORM_SQUARE) requestCreatorOnline.transform(new CropSquareTransformation());
                        else if (transformation == TRANFORM_CIRCLE) requestCreatorOnline.transform(new CircleTransformation());

                        requestCreatorOnline.into(imageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sBmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sBmp = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / factor), (int) (bmp.getHeight() / factor), false);
        } else {
            sBmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius + 5, radius + 5);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2,
                radius / 2, radius / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sBmp, rect, rect, paint);

        return output;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square";
        }
    }

    public static class CircleTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
