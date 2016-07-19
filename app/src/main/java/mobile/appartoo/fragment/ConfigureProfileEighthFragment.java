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
public class ConfigureProfileEighthFragment extends Fragment {

    ArrayList<ResponsibleModel> responsibleModels;
    ResponsiblesAdapter responsiblesAdapter;
    ListView responsiblesListView;
    ImageView addResponsibleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_configure_profile_page8, container, false);

        responsibleModels = new ArrayList<>();
        responsiblesListView = (ListView) rootView.findViewById(R.id.responsibleList);
        addResponsibleButton = (ImageView) rootView.findViewById(R.id.addResponsibleButton);
        responsiblesAdapter = new ResponsiblesAdapter(getActivity(), responsibleModels);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        responsiblesListView.setAdapter(responsiblesAdapter);

        addResponsibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View dialogLayout = inflater.inflate(R.layout.alert_dialog_responsible, null);
            android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(getActivity());
            selectContractDialog.setTitle("Ajouter un garant");
            selectContractDialog.setView(dialogLayout);
            selectContractDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ResponsibleModel newResponsible = new ResponsibleModel();
                    newResponsible.setFirstname(((EditText) dialogLayout.findViewById(R.id.responsibleRecordFirstName)).getText().toString());
                    newResponsible.setLastname(((EditText) dialogLayout.findViewById(R.id.responsibleRecordLastName)).getText().toString());
                    newResponsible.setMail(((EditText) dialogLayout.findViewById(R.id.responsibleRecordMail)).getText().toString());
                    newResponsible.setIncome(Float.valueOf(((EditText) dialogLayout.findViewById(R.id.responsibleRecordIncome)).getText().toString()));
                    responsibleModels.add(newResponsible);
                    responsiblesAdapter.notifyDataSetChanged();
                }
            });
            selectContractDialog.setNegativeButton("Annuler", null);
            selectContractDialog.show();
            }
        });
    }
}
