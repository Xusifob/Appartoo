package com.appartoo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.appartoo.R;
import com.appartoo.activity.UserDetailActivity;
import com.appartoo.adapter.ResidentsAdapter;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferModelWithDate;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.model.UserModel;
import com.appartoo.view.NonScrollableListView;

/**
 * Created by alexandre on 16-07-13.
 */
public class OfferDetailsFragment extends Fragment {

    private SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
    private NonScrollableListView residentList;
    private OfferModel offer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        residentList = (NonScrollableListView) view.findViewById(R.id.offerResidentList);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        offer = getActivity().getIntent().getParcelableExtra("offer");
        populateView();
    }

    private void populateView() {
        ((TextView) getActivity().findViewById(R.id.offerCity)).setText(offer.getAddress().getCity());
        ((TextView) getActivity().findViewById(R.id.offerDescription)).setText(offer.getDescription());
        ((TextView) getActivity().findViewById(R.id.offerTitle)).setText(offer.getName());
        ((TextView) getActivity().findViewById(R.id.offerKeyword)).setText(offer.getKeyword() + " " + NumberFormat.getInstance().format(offer.getPrice()) + " " + getString(R.string.euro));
        ((TextView) getActivity().findViewById(R.id.offerRooms)).setText(String.valueOf(offer.getRooms()) + " " + getString(R.string.room_or_rooms));

        if(offer instanceof OfferModelWithDate) {
            ((TextView) getActivity().findViewById(R.id.offerStart)).setText(getString(R.string.start) + " : " + dateParser.format(((OfferModelWithDate) offer).getAvailabilityStarts()));
            ((TextView) getActivity().findViewById(R.id.offerEnd)).setText(getString(R.string.end) + " : " + dateParser.format(((OfferModelWithDate) offer).getAvailabilityEnds()));
        } else if (offer instanceof OfferModelWithDetailledDate) {
            ((TextView) getActivity().findViewById(R.id.offerStart)).setText(getString(R.string.start) + " : " + dateParser.format(((OfferModelWithDetailledDate) offer).getAvailabilityStarts().getDate()));
            ((TextView) getActivity().findViewById(R.id.offerEnd)).setText(getString(R.string.start) + " : " + dateParser.format(((OfferModelWithDetailledDate) offer).getAvailabilityEnds().getDate()));
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
            getActivity().findViewById(R.id.offerDetailsResidentsSeparator).setVisibility(View.GONE);
        }
    }
}
