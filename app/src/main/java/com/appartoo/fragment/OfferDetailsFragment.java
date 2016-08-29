package com.appartoo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.activity.UserDetailActivity;
import com.appartoo.adapter.ResidentsAdapter;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferModelWithDate;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.model.UserModel;
import com.appartoo.utils.TextValidator;
import com.appartoo.view.NonScrollableListView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-13.
 */
public class OfferDetailsFragment extends Fragment {

    private SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
    private NonScrollableListView residentList;
    private View rootView;
    private TextView offerCity;
    private TextView offerDescription;
    private TextView offerTitle;
    private TextView offerKeyword;
    private TextView offerRooms;
    private TextView offerStart;
    private TextView offerEnd;

    private View offerDetailsResidentsSeparator;
    private OfferModel offer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        residentList = (NonScrollableListView) rootView.findViewById(R.id.offerResidentList);

        offerCity = (TextView) rootView.findViewById(R.id.offerCity);
        offerDescription = (TextView) rootView.findViewById(R.id.offerDescription);
        offerTitle = (TextView) rootView.findViewById(R.id.offerTitle);
        offerKeyword = (TextView) rootView.findViewById(R.id.offerKeyword);
        offerRooms = (TextView) rootView.findViewById(R.id.offerRooms);
        offerStart = (TextView) rootView.findViewById(R.id.offerStart);
        offerEnd = (TextView) rootView.findViewById(R.id.offerEnd);
        offerDetailsResidentsSeparator = rootView.findViewById(R.id.offerDetailsResidentsSeparator);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        offer = getActivity().getIntent().getParcelableExtra("offer");
        populateView();
    }

    private void populateView() {
        if(offer.getAddress() != null) offerCity.setText(offer.getAddress().getCity());
        else offerCity.setText(R.string.unknown_city);

        if (offer.getDescription() == null || !TextValidator.haveText(offer.getDescription())) offerDescription.setText(R.string.no_description);
        else offerDescription.setText(offer.getDescription());
        offerTitle.setText(offer.getName());
        offerKeyword.setText(offer.getKeyword() + " " + NumberFormat.getInstance().format(offer.getPrice()) + " " + getString(R.string.euro));
        offerRooms.setText(String.valueOf(offer.getRooms()) + " " + getString(R.string.room_or_rooms));

        String startNotInformed = getString(R.string.start) + " : " + getString(R.string.not_informed);
        String endNotInformed = getString(R.string.end) + " : " + getString(R.string.not_informed);

        if(offer instanceof OfferModelWithDate) {
            if(((OfferModelWithDate) offer).getAvailabilityStarts() != null) offerStart.setText(getString(R.string.start) + " : " + dateParser.format(((OfferModelWithDate) offer).getAvailabilityStarts()));
            else offerStart.setText(startNotInformed);
            if(((OfferModelWithDate) offer).getAvailabilityEnds() != null) offerEnd.setText(getString(R.string.end) + " : " + dateParser.format(((OfferModelWithDate) offer).getAvailabilityEnds()));
            else offerEnd.setText(endNotInformed);
        } else if (offer instanceof OfferModelWithDetailledDate) {
            if(((OfferModelWithDetailledDate) offer).getAvailabilityStarts() != null) offerStart.setText(getString(R.string.start) + " : " + dateParser.format(((OfferModelWithDetailledDate) offer).getAvailabilityStarts().getDate()));
            else offerStart.setText(startNotInformed);
            if(((OfferModelWithDetailledDate) offer).getAvailabilityEnds() != null) offerEnd.setText(getString(R.string.end) + " : " + dateParser.format(((OfferModelWithDetailledDate) offer).getAvailabilityEnds().getDate()));
            else offerEnd.setText(endNotInformed);
        }

        ArrayList<UserModel> residents = new ArrayList<>();
        residents.addAll(offer.getResident());

        if(residents.size() > 0) {

            ResidentsAdapter residentsAdapter = new ResidentsAdapter(getActivity(), residents);
            residentsAdapter.setAcceptAnimals(offer.getAcceptAnimal());

            residentList.setAdapter(residentsAdapter);
            residentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("user", offer.getResident().get(position));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
            });
        } else {
            residentList.setVisibility(View.GONE);
            offerDetailsResidentsSeparator.setVisibility(View.GONE);
        }
    }
}
