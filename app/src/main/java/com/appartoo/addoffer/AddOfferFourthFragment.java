package com.appartoo.addoffer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.appartoo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferFourthFragment extends Fragment {

    private SimpleDateFormat dateFormat;
    private EditText availabilityStart;
    private EditText availabilityEnd;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page04, container, false);

        availabilityStart = (EditText) rootView.findViewById(R.id.addOfferAvailabilityStarts);
        availabilityEnd = (EditText) rootView.findViewById(R.id.addOfferAvailabilityEnds);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        availabilityStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        availabilityEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });
    }

    private void openDatePicker(View v) {

        final EditText dateEditText = (EditText) v;
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEditText.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
