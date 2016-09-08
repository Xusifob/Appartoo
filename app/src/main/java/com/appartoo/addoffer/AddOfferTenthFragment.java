package com.appartoo.addoffer;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.appartoo.R;
import com.appartoo.utils.adapter.AddResidentAdapter;
import com.appartoo.utils.model.UserModel;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferTenthFragment extends Fragment {

    //TODO add residents to offer;

    private ArrayList<UserModel> residentModels;
    private AddResidentAdapter residentsAdapter;
    private ListView residentsListView;
    private ImageView addresidentButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page10, container, false);

        residentModels = new ArrayList<>();
        residentsListView = (ListView) rootView.findViewById(R.id.addOfferResidentList);
        addresidentButton = (ImageView) rootView.findViewById(R.id.addResidentButton);
        residentsAdapter = new AddResidentAdapter(getActivity(), residentModels);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        residentsListView.setAdapter(residentsAdapter);
        /*addresidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogLayout = inflater.inflate(R.layout.alert_dialog_resident, null);
                android.app.AlertDialog.Builder addGarantorDialog = new android.app.AlertDialog.Builder(getActivity());
                addGarantorDialog.setTitle("Ajouter un résident");
                addGarantorDialog.setView(dialogLayout);
                addGarantorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserModel newresident = new UserModel();
                        newresident.setGivenName(((EditText) dialogLayout.findViewById(R.id.residentRecordFirstName)).getText().toString());
                        newresident.setFamilyName(((EditText) dialogLayout.findViewById(R.id.residentRecordLastName)).getText().toString());
                        newresident.setEmail(((EditText) dialogLayout.findViewById(R.id.residentRecordMail)).getText().toString());
                        residentModels.add(newresident);
                        if(getActivity() instanceof AddOfferActivity){
                            ((AddOfferActivity) getActivity()).setResidents(residentModels);
                        }
                        residentsAdapter.notifyDataSetChanged();
                    }
                });
                addGarantorDialog.setNegativeButton(R.string.cancel, null);
                addGarantorDialog.show();
            }
        });*/
    }
}
