package irctc.factor.app.irctcmadeeasy.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;


import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import irctc.factor.app.irctcmadeeasy.Fragments.BookingPaymentFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.PassengerListFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.TrainDetailsFragment;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class BookTicketAdapter extends FragmentStatePagerAdapter {
    List<Fragment> moTriViews = new ArrayList<>();
    String[] mTitles;

    public BookTicketAdapter(FragmentManager fm, List<Fragment> listViews, String[] names) {
        super(fm);
        mTitles = names;
        moTriViews.addAll(listViews);
    }

    @Override
    public int getItemPosition(Object object) { return POSITION_NONE; }

    @Override
    public Fragment getItem(int position) {
        return moTriViews.get(position);
        /*switch (position) {
            case 0:
                return TrainDetailsFragment.newInstance();
            case 1:
                return PassengerListFragment.newInstance();
            case 2:
                return BookingPaymentFragment.newInstance();
            default:
                return null;
        }*/
    }

    @Override
    public int getCount() {
        return moTriViews.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}


