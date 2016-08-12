package mobile.appartoo.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mobile.appartoo.R;
import mobile.appartoo.model.UserWithProfileModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.ImageManager;
import mobile.appartoo.utils.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-15.
 */
public class UserProfileMainFragment extends Fragment {

    private ImageView userProfilePic;
    private TextView userName;
    private TextView userInfos;
    private SharedPreferences sharedPreferences;
    private RestService restService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_main, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        userName = (TextView) view.findViewById(R.id.profileName);
        userInfos = (TextView) view.findViewById(R.id.profileInfos);
        userProfilePic = (ImageView) view.findViewById(R.id.userProfileMainProfilePic);
        sharedPreferences = getActivity().getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);

        if(Appartoo.LOGGED_USER_PROFILE == null) {
            populateWithLocalInfos();
            getUserProfile();
            userProfilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.default_profile_picture, null));
        } else {
            populateView();
        }
    }

    public void populateWithLocalInfos() {
        String givenName = sharedPreferences.getString("givenName", "");
        String familyName = sharedPreferences.getString("familyName", "");
        String mail = sharedPreferences.getString("email", "");
        String age = sharedPreferences.getString("age", "");

        if(!givenName.equals("") && !familyName.equals("") && !mail.equals("")) {
            userName.setText(givenName + " " + familyName);
        }

        if(!age.equals("")) {
            userInfos.setText(age + " " + getString(R.string.year_age));
        }
    }

    public void populateView() {
        if(!Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl().equals("images/profile.png")) {
            ImageManager.downloadPictureIntoView(getActivity().getApplicationContext(), userProfilePic, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl(), true);
        } else {
            userProfilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.default_profile_picture, null));
        }
        userName.setText(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName());
        userInfos.setText(Integer.toString(Appartoo.LOGGED_USER_PROFILE.getAge()) + " " + getString(R.string.year_age));
    }

    private void getUserProfile(){

        Call<UserWithProfileModel> callback = restService.getLoggedUserProfile("Bearer " + Appartoo.TOKEN);

        //Handle the server response
        callback.enqueue(new Callback<UserWithProfileModel>() {

            @Override
            public void onResponse(Call<UserWithProfileModel> call, Response<UserWithProfileModel> response) {
                //If the login is successful
                if(response.isSuccessful()) {
                    Appartoo.LOGGED_USER_PROFILE = response.body();

                    sharedPreferences.edit().putString("givenName", Appartoo.LOGGED_USER_PROFILE.getGivenName())
                            .putString("familyName", Appartoo.LOGGED_USER_PROFILE.getFamilyName())
                            .putString("email", Appartoo.LOGGED_USER_PROFILE.getUser().getEmail())
                            .putString("age", Integer.toString(Appartoo.LOGGED_USER_PROFILE.getAge())).apply();

                    populateView();

                } else {
                    response.code();
                    Toast.makeText(getActivity(), R.string.error_retrieve_user_profile, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserWithProfileModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_retrieve_user_profile, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
