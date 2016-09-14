package com.appartoo.addoffer;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.adapter.ImageModelViewPagerAdapter;
import com.appartoo.utils.adapter.ImageViewViewPagerAdapter;
import com.appartoo.utils.model.ImageModel;
import com.appartoo.utils.model.OfferModel;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddModifyOfferPicturesFragment extends ValidationFragment {

    private View pictureFromCamera;
    private View pictureFromGallery;
    private ArrayList<ImageView> imageViews;
    private ArrayList<File> filesToAdd;
    private ArrayList<String> imageIdsToDelete;
    private ArrayList<ImageModel> imagesModels;
    private ViewPager imagePager;
    private CircleIndicator circleIndicator;
    private ImageViewViewPagerAdapter pagerAdapter;
    private Uri cameraUri;
    private TextView fragmentTitle;
    private Integer textReference;
    private Button picturesButton;
    private Integer textButtonReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_pictures, container, false);
        imagePager = (ViewPager) rootView.findViewById(R.id.addOfferPicturesPager);
        pictureFromCamera = rootView.findViewById(R.id.pictureFromCamera);
        pictureFromGallery = rootView.findViewById(R.id.pictureFromGallery);
        circleIndicator = (CircleIndicator) rootView.findViewById(R.id.offerFlatImagesPagerIndicator);
        picturesButton = (Button) rootView.findViewById(R.id.finishaddOfferButton);

        imageIdsToDelete = new ArrayList<>();
        filesToAdd = new ArrayList<>();
        imageViews = new ArrayList<>();

        pagerAdapter = new ImageViewViewPagerAdapter(imageViews);

        imagePager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(imagePager);
        pagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        fragmentTitle = (TextView) rootView.findViewById(R.id.addOfferPicturesTitle);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(textReference != null) fragmentTitle.setText(textReference);
        if(textButtonReference != null) picturesButton.setText(textButtonReference);

        if(imagesModels != null && imageViews.size() == 0) {

            for(final ImageModel image : imagesModels) {
                if(!image.getContentUrl().equals("images/no-image.png")) {
                    final ImageView imageView = imageViewFromModel(image);
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppThemeDialog))
                                    .setTitle("Retirer l'image ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            imageViews.remove(imageView);
                                            imageIdsToDelete.add(image.getIdNumber());
                                            pagerAdapter.notifyDataSetChanged();
                                        }
                                    }).setNegativeButton("Non", null)
                                    .show();
                            return true;
                        }
                    });
                    imageViews.add(imageView);
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        }

        System.out.println(pagerAdapter.getCount());

        pictureFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    File cameraFile = ImageManager.createImageFile(getActivity().getApplicationContext());
                    cameraUri = Uri.fromFile(cameraFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                    startActivityForResult(intent, ImageManager.REQUEST_IMAGE_CAPTURE);
                } catch (IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.unable_to_load_camera, Toast.LENGTH_SHORT).show();
                }
            }
        });

        pictureFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ImageManager.REQUEST_PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = null;
            if (requestCode == ImageManager.REQUEST_IMAGE_CAPTURE) {
                try {
                    imageBitmap = ImageManager.getPictureFromCamera(getActivity(), cameraUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), R.string.unable_to_load_camera, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == ImageManager.REQUEST_PICK_IMAGE) {
                imageBitmap = ImageManager.getPictureFromGallery(data, getActivity());
            }

            if(imageBitmap != null) {
                final ImageView imageView = new ImageView(getActivity());
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(ImageManager.transformSquare(imageBitmap));

                final File file = ImageManager.bitmapToFile(imageBitmap, getActivity());
                filesToAdd.add(file);

                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppThemeDialog))
                                .setTitle("Retirer l'image ?")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        imageViews.remove(imageView);
                                        filesToAdd.remove(file);
                                        pagerAdapter.notifyDataSetChanged();
                                    }
                                }).setNegativeButton("Non", null)
                                .show();
                        return true;
                    }
                });

                imageViews.add(imageView);

                pagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private ImageView imageViewFromModel(ImageModel imageModel) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        ImageManager.downloadPictureIntoView(getActivity().getApplicationContext(), imageView, imageModel.getContentUrl(), ImageManager.TRANFORM_SQUARE);

        return imageView;
    }


    public ArrayList<File> getFilesToAdd() {
        return filesToAdd;
    }

    public ArrayList<String> getImageIdsToDelete() {
        return imageIdsToDelete;
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);
        this.imagesModels = offerModel.getImages();
    }

    @Override
    public void modifyViews() {
        super.modifyViews();
        textReference = R.string.add_photos_modify;
        textButtonReference = R.string.add_offer_pictures_button;
    }
}