package mobile.appartoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobile.appartoo.R;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpThirdFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_page3, container, false);
        return rootView;
    }

    public boolean isFormValid() {
        View man = getActivity().findViewById(R.id.signUpMan);
        View woman = getActivity().findViewById(R.id.signUpMan);

        System.out.println(man.isSelected());
        System.out.println(woman.isSelected());
        return true;
    }
}
