package mobile.appartoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import mobile.appartoo.R;

/**
 * Created by alexandre on 16-07-20.
 */
public class SearchOfferFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_offer, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        final RangeSeekBar<Integer> rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.searchOfferPrice);

        // Set the range
        rangeSeekBar.setRangeValues(0, 2000);
        rangeSeekBar.setTextAboveThumbsColorResource(R.color.colorDarkGray);
        rangeSeekBar.setSelectedMinValue(300);
        rangeSeekBar.setSelectedMaxValue(1700);

        return view;
    }

}
