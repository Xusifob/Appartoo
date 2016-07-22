package mobile.appartoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mobile.appartoo.R;
import mobile.appartoo.utils.Appartoo;

public class UserProfileModifyFragment extends Fragment {

    private EditText userLastName;
    private EditText userFirstName;
    private EditText userPhone;
    private EditText userMail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_modify, container, false);

        userLastName = (EditText) view.findViewById(R.id.userProfileModifyLastName);
        userFirstName = (EditText) view.findViewById(R.id.userProfileModifyFirstName);
        userPhone = (EditText) view.findViewById(R.id.userProfileModifyPhone);
        userMail = (EditText) view.findViewById(R.id.userProfileModifyMail);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Appartoo.LOGGED_USER_PROFILE != null) {
            populateView();
        }
    }

    private void populateView(){
        userLastName.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getGivenName()));
        userFirstName.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getFamilyName()));
        userPhone.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getTelephone()));
        userMail.setText(String.valueOf(Appartoo.LOGGED_USER_PROFILE.getUser().getEmail()));
    }
}