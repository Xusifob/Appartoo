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
import com.appartoo.utils.model.OfferModel;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferRoomsFragment extends ValidationFragment {

    private EditText rooms;
    private String roomsStr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_rooms, container, false);

        rooms = (EditText) rootView.findViewById(R.id.addOfferRooms);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(roomsStr != null) rooms.setText(roomsStr);

        rooms.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        rooms.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddOfferActivity) ((AddOfferActivity) getActivity()).nextView(textView);
                return true;
            }
        });
    }

    @Override
    public boolean validateFragment(Context context) {
        if(!TextValidator.haveText(rooms.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_rooms, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Integer.valueOf(rooms.getText().toString()) > 10) {
            Toast.makeText(context, R.string.error_max_rooms, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Integer getRooms() {
        return Integer.valueOf(rooms.getText().toString());
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);
        this.roomsStr = offerModel.getRooms().toString();
    }
}
