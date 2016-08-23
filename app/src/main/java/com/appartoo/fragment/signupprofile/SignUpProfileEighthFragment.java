package com.appartoo.fragment.signupprofile;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.appartoo.R;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileEighthFragment extends Fragment {

    private EditText contract;
    private final String[] values = {"student", "salary", "freelance", "retired", "unemployed"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_page08, container, false);
        contract = (EditText) rootView.findViewById(R.id.signUpConfigureContract);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(getActivity());

                final String[] items = getResources().getStringArray(R.array.contracts);

                selectContractDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contract.setText(items[which]);
                        contract.setTag(values[which]);
                    }
                });

                selectContractDialog.setNegativeButton(R.string.cancel, null);
                selectContractDialog.show();
            }
        });
    }
}
