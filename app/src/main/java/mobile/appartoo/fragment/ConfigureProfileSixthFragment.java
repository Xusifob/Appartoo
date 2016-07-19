package mobile.appartoo.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

        ((EditText) getActivity().findViewById(R.id.contractConfigureProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(getActivity());
                final String[] items = getResources().getStringArray(R.array.contracts);

                selectContractDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText) getActivity().findViewById(R.id.contractConfigureProfile)).setText(items[which]);
                    }
                });

                selectContractDialog.setNegativeButton("Annuler", null);
                selectContractDialog.show();
            }
        });
    }
}
