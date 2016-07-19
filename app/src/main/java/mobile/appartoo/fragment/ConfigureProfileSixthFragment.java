package mobile.appartoo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import mobile.appartoo.R;

/**
 * Created by alexandre on 16-07-12.
 */
public class ConfigureProfileSixthFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_configure_profile_page6, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

//        ((TextView) getActivity().findViewById(R.id.contractConfigureProfile)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getActivity()).show();
//            }
//        });
    }
}
