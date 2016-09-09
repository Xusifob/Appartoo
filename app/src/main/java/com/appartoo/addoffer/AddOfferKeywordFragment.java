package com.appartoo.addoffer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferKeywordFragment extends ValidationFragment {

    private EditText keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_keyword, container, false);
        keyword = (EditText) rootView.findViewById(R.id.addOfferKeyword);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder selectKeywordDialog = new android.app.AlertDialog.Builder(getActivity());

                final String[] items = getResources().getStringArray(R.array.keywords);

                selectKeywordDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keyword.setText(items[which]);
                    }
                });
                selectKeywordDialog.show();
            }
        });
    }

    @Override
    public boolean validateFragment(Context context) {
        if(!TextValidator.haveText(keyword.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_keyword, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public String getKeyword() {
        return keyword.getText().toString().trim();
    }
}
