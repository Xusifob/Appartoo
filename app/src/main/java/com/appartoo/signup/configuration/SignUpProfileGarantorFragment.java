package com.appartoo.signup.configuration;


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
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.adapter.GarantorsAdapter;
import com.appartoo.utils.model.GarantorModel;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileGarantorFragment extends Fragment {

    private ArrayList<GarantorModel> garantorModels;
    private GarantorsAdapter garantorsAdapter;
    private ListView garantorsListView;
    private ImageView addgarantorButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_garantors, container, false);

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
                    GarantorModel newgarantor = getGarantorModel(dialogLayout);

                    if(newgarantor != null) {
                        garantorModels.add(newgarantor);
                        garantorsAdapter.notifyDataSetChanged();
                    }
                }
            });
            selectContractDialog.setNegativeButton(R.string.cancel, null);
            selectContractDialog.show();
            }
        });
    }

    private GarantorModel getGarantorModel(View dialogLayout){
        String firstName = ((EditText) dialogLayout.findViewById(R.id.garantorRecordFirstName)).getText().toString();
        String familyName = ((EditText) dialogLayout.findViewById(R.id.garantorRecordLastName)).getText().toString();
        String email = ((EditText) dialogLayout.findViewById(R.id.garantorRecordMail)).getText().toString();
        String income = ((EditText) dialogLayout.findViewById(R.id.garantorRecordIncome)).getText().toString();

        if(!TextValidator.haveText(new String[] {firstName, familyName, email, income})) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.error_missing_info_garantor, Toast.LENGTH_SHORT).show();
            return null;
        }

        if(!TextValidator.isEmail(email)) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
            return null;
        }

        GarantorModel model = new GarantorModel();
        model.setGivenName(firstName);
        model.setFamilyName(familyName);
        model.setEmail(email);
        model.setIncome(Float.valueOf(income));

        return model;

    }

    public ArrayList<GarantorModel> getGarantors() {
        return garantorModels;
    }
}
