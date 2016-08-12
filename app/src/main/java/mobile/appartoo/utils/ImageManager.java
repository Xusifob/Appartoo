package mobile.appartoo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mobile.appartoo.R;

/**
 * Created by alexandre on 16-08-10.
 */
public class ImageManager {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PICK_IMAGE = 2;

    public static Bitmap getPictureFromGallery(Intent data, Activity activity){
        Bitmap imageBitmap;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        try {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            imageBitmap = BitmapFactory.decodeFile(filePath);
            imageBitmap = rotateImageIfRequired(imageBitmap, Uri.parse(filePath));
            imageBitmap = ImageManager.transformResize(imageBitmap, 1280);

        } catch (NullPointerException e) {
            imageBitmap = null;
            Toast.makeText(activity.getApplicationContext(), R.string.unable_to_load_picture, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            imageBitmap = null;
            e.printStackTrace();
        }
        return imageBitmap;
    }

    public static Bitmap getPictureFromCamera(Activity activity){
        Bitmap imageBitmap;
        File file = getTempFile(activity);
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(file) );
            imageBitmap = rotateImageIfRequired(imageBitmap, Uri.fromFile(file));
            imageBitmap = ImageManager.transformResize(imageBitmap, 1280);
        } catch (IOException e) {
            imageBitmap = null;
            e.printStackTrace();
        }
        return imageBitmap;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

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

    public static File getTempFile(Context context){
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
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

    public static File transformFile(Bitmap bitmap, Activity activity){
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

    public static void downloadPictureIntoView(@NonNull final Context context, @NonNull final ImageView imageView, @NonNull String url, boolean squarePicture){
        final String imageUrl = Appartoo.SERVER_URL + "/upload/" + url;

        System.out.println(imageUrl);

        RequestCreator requestCreator = Picasso.with(context)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE);

        if(squarePicture) {
            requestCreator.transform(new CropSquareTransformation());
        }

        requestCreator.into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(imageUrl)
                                .transform(new CropSquareTransformation())
                                .into(imageView, new com.squareup.picasso.Callback() {
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

    private static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
