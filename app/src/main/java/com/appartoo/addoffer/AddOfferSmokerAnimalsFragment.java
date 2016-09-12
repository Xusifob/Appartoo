package com.appartoo.addoffer;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.OfferModel;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferSmokerAnimalsFragment extends ValidationFragment {

    private View acceptAnimals;
    private View allowSmoker;

    private Boolean acceptAnimalsBool;
    private Boolean allowSmokerBool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_smoker_animals, container, false);

        acceptAnimals = rootView.findViewById(R.id.addOfferAcceptAnimals);
        allowSmoker = rootView.findViewById(R.id.addOfferAllowSmoker);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        acceptAnimals.setTag(false);
        allowSmoker.setTag(false);

        acceptAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAnimal();
            }
        });
        allowSmoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSmoker();
            }
        });

        if(getActivity() instanceof AddOfferActivity) {
            if (acceptAnimalsBool != null && acceptAnimalsBool)
                toggleAnimal();

            if (allowSmokerBool != null && allowSmokerBool)
                toggleSmoker();
        }
    }

    private void toggleAnimal() {
        ImageView animal = (ImageView) acceptAnimals.findViewById(R.id.addOfferImageAnimals);
        Boolean allow = (Boolean) acceptAnimals.getTag();

        if(allow) {
            acceptAnimals.setTag(false);
            animal.setImageDrawable(ResourcesCompat.getDrawable(acceptAnimals.getResources(), R.drawable.dont_accept_animals_big, null));
            ((TextView) acceptAnimals.findViewById(R.id.addOfferTextAnimals)).setText(R.string.i_dont_accept_animals);
        } else {
            acceptAnimals.setTag(true);
            animal.setImageDrawable(ResourcesCompat.getDrawable(acceptAnimals.getResources(), R.drawable.accept_animals_big, null));
            ((TextView) acceptAnimals.findViewById(R.id.addOfferTextAnimals)).setText(R.string.i_accept_animals);
        }
    }

    private void toggleSmoker() {
        ImageView smoker = (ImageView) allowSmoker.findViewById(R.id.addOfferImageSmoker);
        Boolean allow = (Boolean) allowSmoker.getTag();

        if(allow) {
            allowSmoker.setTag(false);
            smoker.setImageDrawable(ResourcesCompat.getDrawable(allowSmoker.getResources(), R.drawable.is_not_smoker_big, null));
            ((TextView) allowSmoker.findViewById(R.id.addOfferTextSmoker)).setText(R.string.i_dont_accept_smokers);
        } else {
            allowSmoker.setTag(true);
            smoker.setImageDrawable(ResourcesCompat.getDrawable(allowSmoker.getResources(), R.drawable.is_smoker_big, null));
            ((TextView) allowSmoker.findViewById(R.id.addOfferTextSmoker)).setText(R.string.i_accept_smokers);
        }
    }

    public Boolean getAcceptAnimals() {
        return (Boolean) acceptAnimals.getTag();
    }

    public Boolean getAllowSmoker() {
        return (Boolean) allowSmoker.getTag();
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);

        this.acceptAnimalsBool = offerModel.getAcceptAnimal();
        this.allowSmokerBool = offerModel.getSmoker();

    }
}
