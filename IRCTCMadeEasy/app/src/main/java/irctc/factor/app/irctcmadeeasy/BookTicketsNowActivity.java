package irctc.factor.app.irctcmadeeasy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.rey.material.widget.Button;
import com.rey.material.widget.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import irctc.factor.app.irctcmadeeasy.Adapters.BookTicketAdapter;
import irctc.factor.app.irctcmadeeasy.Fragments.BookingPaymentFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.PassengerListFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.TrainDetailsFragment;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class BookTicketsNowActivity extends AppCompatActivity {

    @BindView(R.id.id_pager_indicator)
    TabPageIndicator mPagerIndicator;
    @BindView(R.id.id_pager_verses)
    ViewPager mPager;
    @BindView(R.id.btn_book_now_info)
    Button mBookNowButton;
    @BindView(R.id.btn_save_info)
    Button mSaveForLaterButton;

    String[] mPagerTitles = {"Train Details","Passengers","Payment"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_tickets);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        List<Fragment> listFrags = new ArrayList<>();
        listFrags.add(new TrainDetailsFragment());
        listFrags.add(new PassengerListFragment());
        listFrags.add(new BookingPaymentFragment());

        BookTicketAdapter adpater = new BookTicketAdapter(getSupportFragmentManager(), listFrags, mPagerTitles);
        mPager.setAdapter(adpater);

        assert mPagerIndicator != null;
        mPagerIndicator.setViewPager(mPager);

    }
}
