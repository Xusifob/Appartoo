package mobile.appartoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import mobile.appartoo.R;
import mobile.appartoo.utils.Appartoo;

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
    private Switch isMessy;
    private Switch isManiac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_modify, container, false);

        userLastName = (EditText) view.findViewById(R.id.userProfileModifyLastName);
        userFirstName = (EditText) view.findViewById(R.id.userProfileModifyFirstName);
        userPhone = (EditText) view.findViewById(R.id.userProfileModifyPhone);
        userMail = (EditText) view.findViewById(R.id.userProfileModifyMail);
        isSmoker = ((Switch) view.findViewById(R.id.userProfileModifyIsSmoker));
        isCook = ((Switch) view.findViewById(R.id.userProfileModifyIsCook));
        isMusician = ((Switch) view.findViewById(R.id.userProfileModifyIsMusician));
        isSpendthrift = ((Switch) view.findViewById(R.id.userProfileModifyIsSpendthrift));
        isPartyGoer = ((Switch) view.findViewById(R.id.userProfileModifyIsPartyGoer));
        isLayabout = ((Switch) view.findViewById(R.id.userProfileModifyIsLayabout));
        inRelationship = ((Switch) view.findViewById(R.id.userProfileModifyInRelationship));
        isGeek = ((Switch) view.findViewById(R.id.userProfileModifyIsGeek));
        isTraveller = ((Switch) view.findViewById(R.id.userProfileModifyIsTraveller));
        isGenerous = ((Switch) view.findViewById(R.id.userProfileModifyIsGenerous));
        isMessy = ((Switch) view.findViewById(R.id.userProfileModifyIsMessy));
        isManiac = ((Switch) view.findViewById(R.id.userProfileModifyIsManiac));

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
        if(Appartoo.LOGGED_USER_PROFILE.getMessy() != null) isMessy.setChecked(Appartoo.LOGGED_USER_PROFILE.getMessy());
        if(Appartoo.LOGGED_USER_PROFILE.getManiac() != null) isManiac.setChecked(Appartoo.LOGGED_USER_PROFILE.getManiac());
    }
}