package mobile.appartoo.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.adapter.ResponsiblesAdapter;
import mobile.appartoo.model.ResponsibleModel;

/**
 * Created by alexandre on 16-07-12.
 */
public class ConfigureProfileNinethFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_configure_profile_page9, container, false);
        return rootView;
    }
}
