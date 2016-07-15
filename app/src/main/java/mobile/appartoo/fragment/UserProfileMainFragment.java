package mobile.appartoo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobile.appartoo.R;

/**
 * Created by alexandre on 16-07-15.
 */
public class UserProfileMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_main, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }
}
