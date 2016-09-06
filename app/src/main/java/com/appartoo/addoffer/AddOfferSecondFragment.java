package com.appartoo.addoffer;

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
public class AddOfferSecondFragment extends Fragment {

    private EditText keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page02, container, false);
        keyword = (EditText) rootView.findViewById(R.id.addOfferKeyword);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(getActivity());

                final String[] items = getResources().getStringArray(R.array.keywords);

                selectContractDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keyword.setText(items[which]);
                    }
                });
                selectContractDialog.show();
            }
        });
    }
}
