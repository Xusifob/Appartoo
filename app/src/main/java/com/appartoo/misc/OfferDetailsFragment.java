package com.appartoo.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.adapter.ResidentsAdapter;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDate;
import com.appartoo.utils.model.OfferModelWithDetailledDate;
import com.appartoo.utils.model.UserModel;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.view.NonScrollableListView;

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

    public void bindData(final OfferModel offer) {
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
            OfferModelWithDate offerModel = (OfferModelWithDate) offer;
            if (offerModel.getAvailabilityStarts() != null)
                offerStart.setText(getString(R.string.start) + " : " + dateParser.format(offerModel.getAvailabilityStarts()));
            else
                offerStart.setText(startNotInformed);

            if (offerModel.getAvailabilityEnds() != null)
                offerEnd.setText(getString(R.string.end) + " : " + dateParser.format(offerModel.getAvailabilityEnds()));
            else
                offerEnd.setText(endNotInformed);
        } else if(offer instanceof OfferModelWithDetailledDate) {
            OfferModelWithDetailledDate offerModel = (OfferModelWithDetailledDate) offer;
            if (offerModel.getAvailabilityStarts() != null)
                offerStart.setText(getString(R.string.start) + " : " + dateParser.format(offerModel.getAvailabilityStarts().getDate()));
            else
                offerStart.setText(startNotInformed);

            if (offerModel.getAvailabilityEnds() != null)
                offerEnd.setText(getString(R.string.end) + " : " + dateParser.format(offerModel.getAvailabilityEnds().getDate()));
            else
                offerEnd.setText(endNotInformed);
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
                }
            });
        } else {
            residentList.setVisibility(View.GONE);
                offerDetailsResidentsSeparator.setVisibility(View.GONE);
        }
    }
}
