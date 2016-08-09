package mobile.appartoo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import mobile.appartoo.R;
import mobile.appartoo.model.UserWithProfileModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import mobile.appartoo.view.NavigationDrawerView;
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

    private RestService restService;
    private SharedPreferences sharedPreferences;
    private NavigationDrawerView navigationDrawerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_modify, container, false);

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

        sharedPreferences = getActivity().getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        if(Appartoo.LOGGED_USER_PROFILE != null) {
            populateView();
        }
    }

    private void populateView(){
        System.out.println(Appartoo.LOGGED_USER_PROFILE.getFamilyName());
        System.out.println(Appartoo.LOGGED_USER_PROFILE.getGivenName());
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
        if(Appartoo.LOGGED_USER_PROFILE.getMessy() != null) isOrdinate.setChecked(Appartoo.LOGGED_USER_PROFILE.getMessy());
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
        final UserWithProfileModel profileUpdateModel = getProfileUpdateModel();

        if(profileUpdateModel != null && Appartoo.LOGGED_USER_PROFILE != null) {

            Call<UserWithProfileModel> callback = restService.updateUserProfile(Appartoo.LOGGED_USER_PROFILE.getId(),"Bearer (" + Appartoo.TOKEN + ")", profileUpdateModel);

            callback.enqueue(new Callback<UserWithProfileModel>() {
                @Override
                public void onResponse(Call<UserWithProfileModel> call, Response<UserWithProfileModel> response) {
                    saveSettings.setEnabled(true);

                    if(response.isSuccessful()) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.success_update_user_profile, Toast.LENGTH_SHORT).show();

                        if(!profileUpdateModel.getGivenName().equals(Appartoo.LOGGED_USER_PROFILE.getGivenName()) || !profileUpdateModel.getFamilyName().equals(Appartoo.LOGGED_USER_PROFILE.getFamilyName())) {
                            NavigationDrawerView.setHeaderInformations(profileUpdateModel.getGivenName() + " " + profileUpdateModel.getFamilyName(), userMail.getText().toString());
                            navigationDrawerView.updateHeader();
                        }

                        updateUserLoggedModel();
                    } else {
                        System.out.println(response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserWithProfileModel> call, Throwable t) {
                    t.printStackTrace();
                    saveSettings.setEnabled(true);
                }
            });
        }
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
        Appartoo.LOGGED_USER_PROFILE.setMessy(isOrdinate.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setManiac(isManiac.isChecked());
        Appartoo.LOGGED_USER_PROFILE.setGivenName(userFirstName.getText().toString().trim());
        Appartoo.LOGGED_USER_PROFILE.setFamilyName(userLastName.getText().toString().trim());
        Appartoo.LOGGED_USER_PROFILE.setTelephone(userPhone.getText().toString().trim());
    }


    private UserWithProfileModel getProfileUpdateModel(){
        UserWithProfileModel updateModel = new UserWithProfileModel();
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

}