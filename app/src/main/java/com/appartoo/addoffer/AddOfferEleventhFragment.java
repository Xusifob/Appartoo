package com.appartoo.addoffer;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.adapter.ImageListViewAdapter;
import com.appartoo.utils.ImageManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferEleventhFragment extends Fragment {

    private View pictureFromCamera;
    private View pictureFromGallery;
    private ListView pictureContainer;
    private ArrayList<ImageView> images;
    private ArrayList<File> files;
    private ImageListViewAdapter picturesAdapter;
    private Uri cameraUri;
    private int photoNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page11, container, false);
        pictureContainer = (ListView) rootView.findViewById(R.id.pictureContainer);
        pictureFromCamera = rootView.findViewById(R.id.pictureFromCamera);
        pictureFromGallery = rootView.findViewById(R.id.pictureFromGallery);

        images = new ArrayList<>();
        files = new ArrayList<>();
        picturesAdapter = new ImageListViewAdapter(getActivity(), images);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        pictureContainer.setAdapter(picturesAdapter);
        pictureContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                android.app.AlertDialog.Builder removePictureDialog = new android.app.AlertDialog.Builder(getActivity());

                removePictureDialog.setTitle("Retirer l'image ?");
                removePictureDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        images.remove(position);
                        files.remove(position);
                        picturesAdapter.notifyDataSetChanged();
                    }
                });

                removePictureDialog.setNegativeButton("Non", null);
                removePictureDialog.show();

                return true;
            }
        });

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
                ImageView pic = new ImageView(getActivity());
                pic.setAdjustViewBounds(true);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                pic.setImageBitmap(imageBitmap);
                images.add(0, pic);

                files.add(ImageManager.transformFile(imageBitmap, getActivity()));

                if(getActivity() instanceof AddOfferActivity) {
                    ((AddOfferActivity) getActivity()).setFiles(files);
                }
                picturesAdapter.notifyDataSetChanged();
            }
        }
    }
}