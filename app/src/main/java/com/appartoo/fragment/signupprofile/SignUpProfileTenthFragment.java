package com.appartoo.fragment.signupprofile;


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

import com.appartoo.R;
import com.appartoo.adapter.GarantorsAdapter;
import com.appartoo.model.GarantorModel;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileTenthFragment extends Fragment {

    private ArrayList<GarantorModel> garantorModels;
    private GarantorsAdapter garantorsAdapter;
    private ListView garantorsListView;
    private ImageView addgarantorButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_page10, container, false);

        garantorModels = new ArrayList<>();
        garantorsListView = (ListView) rootView.findViewById(R.id.garantorList);
        addgarantorButton = (ImageView) rootView.findViewById(R.id.addGarantorButton);
        garantorsAdapter = new GarantorsAdapter(getActivity(), garantorModels);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        garantorsListView.setAdapter(garantorsAdapter);

        addgarantorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View dialogLayout = inflater.inflate(R.layout.alert_dialog_garantor, null);
            android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(getActivity());
            selectContractDialog.setTitle("Ajouter un garant");
            selectContractDialog.setView(dialogLayout);
            selectContractDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GarantorModel newgarantor = new GarantorModel();
                    newgarantor.setGivenName(((EditText) dialogLayout.findViewById(R.id.garantorRecordFirstName)).getText().toString());
                    newgarantor.setFamilyName(((EditText) dialogLayout.findViewById(R.id.garantorRecordLastName)).getText().toString());
                    newgarantor.setEmail(((EditText) dialogLayout.findViewById(R.id.garantorRecordMail)).getText().toString());
                    newgarantor.setIncome(Float.valueOf(((EditText) dialogLayout.findViewById(R.id.garantorRecordIncome)).getText().toString()));
                    garantorModels.add(newgarantor);
                    garantorsAdapter.notifyDataSetChanged();
                }
            });
            selectContractDialog.setNegativeButton(R.string.cancel, null);
            selectContractDialog.show();
            }
        });
    }
}
