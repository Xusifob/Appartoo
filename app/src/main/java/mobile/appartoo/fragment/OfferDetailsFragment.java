package mobile.appartoo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.activity.UserDetailActivity;
import mobile.appartoo.adapter.ImageViewPagerAdapter;
import mobile.appartoo.adapter.ResidentsAdapter;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.model.OfferModelWithDate;
import mobile.appartoo.model.OfferModelWithDetailledDate;
import mobile.appartoo.model.UserModel;
import mobile.appartoo.view.NonScrollableListView;

/**
 * Created by alexandre on 16-07-13.
 */
public class OfferDetailsFragment extends Fragment {

    private SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
    private NonScrollableListView residentList;
    private ViewPager imagesPager;
    private OfferModel offer;
    private int[] resources = {R.drawable.flat, R.drawable.flat2};

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
        ((TextView) getActivity().findViewById(R.id.offerKeyword)).setText(offer.getKeyword() + " " + Integer.toString(offer.getPrice()) + "€");
        ((TextView) getActivity().findViewById(R.id.offerRooms)).setText(Integer.toString(offer.getRooms()) + " chambre(s)");

        if(offer instanceof OfferModelWithDate) {
            ((TextView) getActivity().findViewById(R.id.offerStart)).setText("Début : " + dateParser.format(((OfferModelWithDate) offer).getAvailabilityStarts()));
            ((TextView) getActivity().findViewById(R.id.offerEnd)).setText("Fin : " + dateParser.format(((OfferModelWithDate) offer).getAvailabilityEnds()));
        } else if (offer instanceof OfferModelWithDetailledDate) {
            ((TextView) getActivity().findViewById(R.id.offerStart)).setText("Début : " + dateParser.format(((OfferModelWithDetailledDate) offer).getAvailabilityStarts().getDate()));
            ((TextView) getActivity().findViewById(R.id.offerEnd)).setText("Fin : " + dateParser.format(((OfferModelWithDetailledDate) offer).getAvailabilityEnds().getDate()));
        }

        ArrayList<UserModel> residents = new ArrayList<>();
        residents.addAll(offer.getResident());

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
    }
}
