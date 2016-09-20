package com.appartoo.signup.configuration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.appartoo.addoffer.AddModifyOfferActivity;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.DetailledDateModel;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDate;
import com.appartoo.utils.model.OfferModelWithDetailledDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileBirthdateFragment extends Fragment {

    private SimpleDateFormat dateFormat;
    private EditText birthdateEdit;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_birthdate, container, false);

        birthdateEdit = (EditText) rootView.findViewById(R.id.signUpConfigureBirthdate);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();

        birthdateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        return rootView;
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

    public boolean validateFragment(Context context) {
        if(birthdateEdit.getTag() != null) {

            Calendar now = Calendar.getInstance();
            Calendar birthdate = Calendar.getInstance();
            birthdate.setTime((Date) birthdateEdit.getTag());

            int age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
            if (birthdate.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR)) age -= 1;

            if (age < 15) {
                Toast.makeText(context, R.string.error_age_under_15, Toast.LENGTH_SHORT).show();
                return false;
            } else if (age > 130) {
                Toast.makeText(context, R.string.error_age_over_130, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public Date getBirthdate() {
        return (Date) birthdateEdit.getTag();
    }
}
