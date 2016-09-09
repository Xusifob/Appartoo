package com.appartoo.addoffer;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferPriceFragment extends ValidationFragment {

    private EditText price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_price, container, false);

        price = (EditText) rootView.findViewById(R.id.addOfferPrice);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        price.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddOfferActivity) ((AddOfferActivity) getActivity()).nextView(textView);
                return true;
            }
        });
    }

    @Override
    public boolean validateFragment(Context context) {
        if(!TextValidator.haveText(price.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_price, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Integer getPrice() {
        return Integer.valueOf(price.getText().toString());
    }

}
