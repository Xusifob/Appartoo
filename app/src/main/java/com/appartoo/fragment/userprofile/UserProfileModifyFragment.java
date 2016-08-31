package com.appartoo.fragment.userprofile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.model.CompleteUserModel;
import com.appartoo.model.ImageModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;
import com.appartoo.view.NavigationDrawerView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileModifyFragment extends Fragment {

    private EditText userLastName;
    private EditText userFirstName;
    private EditText userPhone;
    private EditText userMail;
    private Switch isSmoker;
    private Switch isCook;
    private Switch isMusician;
    private Switch isSpendthrift;
    private Switch isPartyGoer;
    private Switch isLayabout;
    private Switch inRelationship;
    private Switch isGeek;
    private Switch isTraveller;
    private Switch isGenerous;
    private Switch isOrdinate;
    private Switch isManiac;
    private Switch isWorker;
    private Button saveSettings;
    private ImageView userProfilePic;
    private RestService restService;
    private SharedPreferences sharedPreferences;
    private NavigationDrawerView navigationDrawerView;
    private File profilePictureFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_modify, container, false);

        //TODO Plus d'options à modifier

        defineInteractionsVariables(view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);
        navigationDrawerView = (NavigationDrawerView) getActivity().findViewById(R.id.navigationDrawer);
        sharedPreferences = getActivity().getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(Appartoo.LOGGED_USER_PROFILE != null) {
            bindData();
        }

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = getResources().getStringArray(R.array.picture_actions);

                android.app.AlertDialog.Builder choosePictureDialog = new android.app.AlertDialog.Builder(getActivity());
                choosePictureDialog.setNegativeButton(R.string.cancel, null);

                choosePictureDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which){
                            case 0:
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(ImageManager.getTempFile(getActivity())) );
                                startActivityForResult(intent, ImageManager.REQUEST_IMAGE_CAPTURE);
                                break;
                            case 1:
                                intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ImageManager.REQUEST_PICK_IMAGE);
                                break;
                            default:
                                break;
                        }
                    }
                });

                choosePictureDialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap;
            if (requestCode == ImageManager.REQUEST_IMAGE_CAPTURE) {
                imageBitmap = ImageManager.getPictureFromCamera(getActivity());
            } else if (requestCode == ImageManager.REQUEST_PICK_IMAGE) {
                imageBitmap = ImageManager.getPictureFromGallery(data, getActivity());
            } else {
                imageBitmap = null;
            }

            if(imageBitmap != null) {
                profilePictureFile = ImageManager.transformFile(imageBitmap, getActivity());
                userProfilePic.setImageBitmap(ImageManager.transformSquare(imageBitmap));
            }
        }
    }

    private void bindData(){

        if (userProfilePic.getDrawable() == null) {
            ImageManager.downloadPictureIntoView(getActivity().getApplicationContext(), userProfilePic, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
        }

        userFirstName.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getGivenName()));
        userLastName.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getFamilyName()));
        userMail.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getUser().getEmail()));

        if(Appartoo.LOGGED_USER_PROFILE.getTelephone() != null) userPhone.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getTelephone()));
        if(Appartoo.LOGGED_USER_PROFILE.getContract() != null && (Appartoo.LOGGED_USER_PROFILE.getContract().equals("salary") || Appartoo.LOGGED_USER_PROFILE.getContract().equals("freelance"))) isWorker.setChecked(true);
        if(Appartoo.LOGGED_USER_PROFILE.getSmoker() != null) isSmoker.setChecked(Appartoo.LOGGED_USER_PROFILE.getSmoker());
        if(Appartoo.LOGGED_USER_PROFILE.getCook() != null) isCook.setChecked(Appartoo.LOGGED_USER_PROFILE.getCook());
        if(Appartoo.LOGGED_USER_PROFILE.getMusician() != null) isMusician.setChecked(Appartoo.LOGGED_USER_PROFILE.getMusician());
        if(Appartoo.LOGGED_USER_PROFILE.getSpendthrift() != null) isSpendthrift.setChecked(Appartoo.LOGGED_USER_PROFILE.getSpendthrift());
        if(Appartoo.LOGGED_USER_PROFILE.getPartyGoer() != null) isPartyGoer.setChecked(Appartoo.LOGGED_USER_PROFILE.getPartyGoer());
        if(Appartoo.LOGGED_USER_PROFILE.getLayabout() != null) isLayabout.setChecked(Appartoo.LOGGED_USER_PROFILE.getLayabout());
        if(Appartoo.LOGGED_USER_PROFILE.getInRelationship() != null) inRelationship.setChecked(Appartoo.LOGGED_USER_PROFILE.getInRelationship());
        if(Appartoo.LOGGED_USER_PROFILE.getGeek() != null) isGeek.setChecked(Appartoo.LOGGED_USER_PROFILE.getGeek());
        if(Appartoo.LOGGED_USER_PROFILE.getTraveller() != null) isTraveller.setChecked(Appartoo.LOGGED_USER_PROFILE.getTraveller());
        if(Appartoo.LOGGED_USER_PROFILE.getGenerous() != null) isGenerous.setChecked(Appartoo.LOGGED_USER_PROFILE.getGenerous());
        if(Appartoo.LOGGED_USER_PROFILE.getMessy() != null) isOrdinate.setChecked(!Appartoo.LOGGED_USER_PROFILE.getMessy());
        if(Appartoo.LOGGED_USER_PROFILE.getManiac() != null) isManiac.setChecked(Appartoo.LOGGED_USER_PROFILE.getManiac());

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings.setEnabled(false);
                updateUserProfile();
            }
        });
    }

    private void updateUserProfile(){
        System.out.println("Updating user profile");
        final CompleteUserModel profileUpdateModel = getProfileUpdateModel();

        if(profileUpdateModel != null && Appartoo.LOGGED_USER_PROFILE != null) {

            Call<CompleteUserModel> callback = restService.updateUserProfile(RestService.REST_URL + Appartoo.LOGGED_USER_PROFILE.getId(),"Bearer " + Appartoo.TOKEN, profileUpdateModel);

            callback.enqueue(new Callback<CompleteUserModel>() {
                @Override
                public void onResponse(Call<CompleteUserModel> call, Response<CompleteUserModel> response) {

                    if(response.isSuccessful()) {

                        if(!profileUpdateModel.getGivenName().equals(Appartoo.LOGGED_USER_PROFILE.getGivenName()) || !profileUpdateModel.getFamilyName().equals(Appartoo.LOGGED_USER_PROFILE.getFamilyName())) {
                            NavigationDrawerView.setHeaderInformations(profileUpdateModel.getGivenName() + " " + profileUpdateModel.getFamilyName(), userMail.getText().toString());
                            navigationDrawerView.updateHeader();
                        }

                        updateUserLoggedModel();

                        if(profilePictureFile != null){
                            updateUserProfilePicture();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.success_update_user_profile, Toast.LENGTH_SHORT).show();
                            saveSettings.setEnabled(true);
                        }
                    } else {
                        System.out.println(response.code());
                        saveSettings.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<CompleteUserModel> call, Throwable t) {
                    t.printStackTrace();
                    saveSettings.setEnabled(true);
                }
            });
        }
    }

    private void updateUserProfilePicture(){

        RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data"), profilePictureFile);
        MultipartBody.Part body =  MultipartBody.Part.createFormData("file", profilePictureFile.getName(), file);
        Call<ImageModel> callback = restService.addImageToServer(RestService.REST_URL + Appartoo.LOGGED_USER_PROFILE.getId() + "/images","Bearer " + Appartoo.TOKEN, body);

        callback.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                saveSettings.setEnabled(true);

                if(response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.success_update_user_profile, Toast.LENGTH_SHORT).show();
                    Appartoo.LOGGED_USER_PROFILE.setImage(response.body());
                    navigationDrawerView.updateHeader();
                } else {
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
                t.printStackTrace();
                saveSettings.setEnabled(true);
            }
        });
    }

    private void updateUserLoggedModel(){
        Appartoo.LOGGED_USER_PROFILE.setSmoker(isSmoker.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setCook(isCook.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setMusician(isMusician.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setSpendthrift(isSpendthrift.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setPartyGoer(isPartyGoer.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setLayabout(isLayabout.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setInRelationship(inRelationship.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setGeek(isGeek.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setTraveller(isTraveller.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setGenerous(isGenerous.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setMessy(!isOrdinate.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setManiac(isManiac.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setGivenName(userFirstName.getText().toString().trim());
        Appartoo.LOGGED_USER_PROFILE.setFamilyName(userLastName.getText().toString().trim());
        Appartoo.LOGGED_USER_PROFILE.setTelephone(userPhone.getText().toString().trim());
    }


    private CompleteUserModel getProfileUpdateModel(){
        CompleteUserModel updateModel = new CompleteUserModel();
        updateModel.setSmoker(isSmoker.isChecked());
        updateModel.setCook(isCook.isChecked());
        updateModel.setMusician(isMusician.isChecked());
        updateModel.setSpendthrift(isSpendthrift.isChecked());
        updateModel.setPartyGoer(isPartyGoer.isChecked());
        updateModel.setLayabout(isLayabout.isChecked());
        updateModel.setInRelationship(inRelationship.isChecked());
        updateModel.setGeek(isGeek.isChecked());
        updateModel.setTraveller(isTraveller.isChecked());
        updateModel.setGenerous(isGenerous.isChecked());
        updateModel.setMessy(!isOrdinate.isChecked());
        updateModel.setManiac(isManiac.isChecked());
        updateModel.setGivenName(userFirstName.getText().toString().trim());
        updateModel.setFamilyName(userLastName.getText().toString().trim());
        updateModel.setTelephone(userPhone.getText().toString().trim());
        return updateModel;
    }

    private void defineInteractionsVariables(View view){
        userProfilePic = (ImageView) view.findViewById(R.id.userProfileModifyProfilePic);
        userLastName = (EditText) view.findViewById(R.id.userProfileModifyLastName);
        userFirstName = (EditText) view.findViewById(R.id.userProfileModifyFirstName);
        userPhone = (EditText) view.findViewById(R.id.userProfileModifyPhone);
        userMail = (EditText) view.findViewById(R.id.userProfileModifyMail);
        isSmoker = (Switch) view.findViewById(R.id.userProfileModifyIsSmoker);
        isSmoker = (Switch) view.findViewById(R.id.userProfileModifyIsSmoker);
        isCook = (Switch) view.findViewById(R.id.userProfileModifyIsCook);
        isMusician = (Switch) view.findViewById(R.id.userProfileModifyIsMusician);
        isSpendthrift = (Switch) view.findViewById(R.id.userProfileModifyIsSpendthrift);
        isPartyGoer = (Switch) view.findViewById(R.id.userProfileModifyIsPartyGoer);
        isLayabout = (Switch) view.findViewById(R.id.userProfileModifyIsLayabout);
        inRelationship = (Switch) view.findViewById(R.id.userProfileModifyInRelationship);
        isGeek = (Switch) view.findViewById(R.id.userProfileModifyIsGeek);
        isTraveller = (Switch) view.findViewById(R.id.userProfileModifyIsTraveller);
        isGenerous = (Switch) view.findViewById(R.id.userProfileModifyIsGenerous);
        isOrdinate = (Switch) view.findViewById(R.id.userProfileModifyIsOrdinate);
        isManiac = (Switch) view.findViewById(R.id.userProfileModifyIsManiac);
        isWorker = (Switch) view.findViewById(R.id.userProfileModifyIsWorker);
        saveSettings = (Button) view.findViewById(R.id.userProfileModifySaveSettings);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivity().getSupportFragmentManager().putFragment(outState, "currentFragment", this);
    }
}