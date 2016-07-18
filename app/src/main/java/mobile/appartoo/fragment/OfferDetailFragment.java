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
import mobile.appartoo.model.UserModel;
import mobile.appartoo.view.NonScrollableListView;

/**
 * Created by alexandre on 16-07-13.
 */
public class OfferDetailFragment extends Fragment {

    private SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
    private NonScrollableListView residentList;
    private ViewPager imagesPager;
    private OfferModel offer;
    private int[] resources = {R.drawable.flat, R.drawable.flat2};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        residentList = (NonScrollableListView) view.findViewById(R.id.offerResidentList);
        imagesPager = (ViewPager) view.findViewById(R.id.offerFlatImagesPager);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ImageViewPagerAdapter imagesAdapter = new ImageViewPagerAdapter(getActivity(), resources, true);
        imagesPager.setAdapter(imagesAdapter);
        offer = (OfferModel) getActivity().getIntent().getSerializableExtra("offer");

        populateView();
    }

    private void populateView(){
        ((TextView) getActivity().findViewById(R.id.offerCity)).setText(offer.getAddress().getCity());
        ((TextView) getActivity().findViewById(R.id.offerDescription)).setText(offer.getDescription());
        ((TextView) getActivity().findViewById(R.id.offerTitle)).setText(offer.getName());
        ((TextView) getActivity().findViewById(R.id.offerKeyword)).setText(offer.getKeyword() + " " + Integer.toString(offer.getPrice()) + "€");
        ((TextView) getActivity().findViewById(R.id.offerRooms)).setText(Integer.toString(offer.getRooms()) + " chambre(s)");
        ((TextView) getActivity().findViewById(R.id.offerStart)).setText("Début : " + dateParser.format(offer.getAvailabilityStarts()));
        ((TextView) getActivity().findViewById(R.id.offerEnd)).setText("Fin : " + dateParser.format(offer.getAvailabilityEnds()));

        ArrayList<UserModel> residents = new ArrayList<>();
        residents.add(offer.getOwner());
        residents.addAll(offer.getResident());

        ResidentsAdapter residentsAdapter = new ResidentsAdapter(getActivity(), residents);
        residentsAdapter.setAcceptAnimals(offer.getAcceptAnimal());

        residentList.setAdapter(residentsAdapter);
        residentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                if (position == 0) {
                    intent.putExtra("user", offer.getOwner());
                } else {
                    intent.putExtra("user", offer.getResident().get(position - 1));
                }
                startActivity(intent);
            }
        });
    }
}
