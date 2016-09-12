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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.adapter.ImageBitmapViewPagerAdapter;
import com.appartoo.utils.adapter.ImageModelViewPagerAdapter;
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
public class AddOfferPicturesFragment extends ValidationFragment {

    private View pictureFromCamera;
    private View pictureFromGallery;
    private ArrayList<Bitmap> images;
    private ArrayList<File> files;
    private ViewPager imagePager;
    private CircleIndicator circleIndicator;
    private ImageBitmapViewPagerAdapter pagerAdapter;
    private Uri cameraUri;
    private ArrayList<ImageModel> imagesModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_pictures, container, false);
        imagePager = (ViewPager) rootView.findViewById(R.id.addOfferPicturesPager);
        pictureFromCamera = rootView.findViewById(R.id.pictureFromCamera);
        pictureFromGallery = rootView.findViewById(R.id.pictureFromGallery);
        circleIndicator = (CircleIndicator) rootView.findViewById(R.id.offerFlatImagesPagerIndicator);

        images = new ArrayList<>();
        files = new ArrayList<>();
        pagerAdapter = new ImageBitmapViewPagerAdapter(getActivity(), images, files);

        imagePager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(imagePager);
        pagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(imagesModels != null) {
            for(final ImageModel image : imagesModels) {
                final ImageView pic = new ImageView(getActivity());
                pic.setAdjustViewBounds(true);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                pic.setTag(image.getContentUrl());

                final String imageUrl = Appartoo.SERVER_URL + RestService.REST_URL + "/upload/" + image.getContentUrl();

                RequestCreator requestCreator = Picasso.with(getActivity().getApplicationContext())
                        .load(imageUrl)
                        .networkPolicy(NetworkPolicy.OFFLINE);

                requestCreator.into(pic, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        images.add(0, ((BitmapDrawable) pic.getDrawable()).getBitmap());
                        files.add(ImageManager.bitmapToFile(((BitmapDrawable)pic.getDrawable()).getBitmap(), getActivity()));
                        pagerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError() {
                        Log.v("Picasso","Could not fetch image, trying again.");
                        //Try again online if cache failed
                        Picasso.with(getActivity().getApplicationContext())
                                .load(imageUrl)
                                .into(pic, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        images.add(0, ((BitmapDrawable) pic.getDrawable()).getBitmap());
                                        files.add(ImageManager.bitmapToFile(((BitmapDrawable)pic.getDrawable()).getBitmap(), getActivity()));
                                        pagerAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
            }

            imagesModels.clear();
        }

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
                images.add(0, ImageManager.transformSquare(imageBitmap));
                files.add(ImageManager.bitmapToFile(imageBitmap, getActivity()));
                pagerAdapter.notifyDataSetChanged();
            }
        }
    }

    public ArrayList<File> getFiles(){
        return files;
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);
        this.imagesModels = offerModel.getImages();
    }


}