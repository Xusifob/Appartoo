package mobile.appartoo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mobile.appartoo.fragment.AddDemandFragment;
import mobile.appartoo.fragment.AddOfferFragment;

/**
 * Created by alexandre on 16-07-26.
 */
public class AddOffersAndDemandsAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;

    private String tabTitles[] = new String[] { "JE PROPOSE", "JE RECHERCHE" };
    private Fragment fragments[] = new Fragment[] {new AddOfferFragment(), new AddDemandFragment()};
    private Context context;

    public AddOffersAndDemandsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}