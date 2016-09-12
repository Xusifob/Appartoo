package com.appartoo.addoffer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDate;
import com.appartoo.utils.model.OfferModelWithDetailledDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferDatesFragment extends ValidationFragment {

    private SimpleDateFormat dateFormat;
    private EditText availabilityStart;
    private EditText availabilityEnd;
    private Calendar calendar;

    private Date startDate;
    private Date endDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_dates, container, false);

        availabilityStart = (EditText) rootView.findViewById(R.id.addOfferAvailabilityStarts);
        availabilityEnd = (EditText) rootView.findViewById(R.id.addOfferAvailabilityEnds);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(startDate != null) availabilityStart.setText(dateFormat.format(startDate));
        if(endDate != null) availabilityEnd.setText(dateFormat.format(endDate));

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

        availabilityEnd.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        availabilityEnd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddOfferActivity) ((AddOfferActivity) getActivity()).nextView(textView);
                return true;
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
                dateEditText.setTag(calendar.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean validateFragment(Context context) {
        if(!TextValidator.haveText(availabilityStart.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_start_date, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(availabilityEnd.getTag() != null && availabilityStart.getTag() != null) {
            if (((Date) availabilityStart.getTag()).compareTo(((Date) availabilityEnd.getTag())) >= 0) {
                Toast.makeText(context, R.string.error_dates_add_offer, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public Date getStartDate() {
        return (Date) availabilityStart.getTag();
    }

    public Date getEndDate() {
        return (Date) availabilityEnd.getTag();
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);
        if(offerModel instanceof OfferModelWithDate) {
            OfferModelWithDate offer = (OfferModelWithDate) offerModel;
            if (offer.getAvailabilityStarts() != null) startDate = offer.getAvailabilityStarts();
            if (offer.getAvailabilityEnds() != null) endDate = offer.getAvailabilityEnds();
        } else if(offerModel instanceof OfferModelWithDetailledDate) {
            OfferModelWithDetailledDate offer = (OfferModelWithDetailledDate) offerModel;
            if (offer.getAvailabilityStarts() != null) startDate = offer.getAvailabilityStarts().getDate();
            if (offer.getAvailabilityEnds() != null) endDate = offer.getAvailabilityEnds().getDate();
        }
    }
}
