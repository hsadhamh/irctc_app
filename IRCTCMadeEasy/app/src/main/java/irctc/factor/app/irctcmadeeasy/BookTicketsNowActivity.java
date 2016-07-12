package irctc.factor.app.irctcmadeeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.rey.material.widget.Button;
import com.rey.material.widget.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import irctc.factor.app.irctcmadeeasy.Adapters.BookTicketAdapter;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;

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

    List<Fragment> listFrags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_tickets);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        listFrags.add(new TrainDetailsFragment());
        listFrags.add(new PassengerListFragment());
        listFrags.add(new BookingPaymentFragment());

        BookTicketAdapter adpater = new BookTicketAdapter(getSupportFragmentManager(), listFrags, mPagerTitles);
        mPager.setAdapter(adpater);

        assert mPagerIndicator != null;
        mPagerIndicator.setViewPager(mPager);

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(AddPassengerEvent e){
        Intent i = new Intent(BookTicketsNowActivity.this, AddPassengerActivity.class);
        startActivityForResult(i, EventConstants.EVENT_REQUEST_ADD_PASSENGER);
    }

    @Subscribe
    public void onEvent(EditPassengerInfo e){
        Intent i = new Intent(BookTicketsNowActivity.this, AddPassengerActivity.class);
        i.putExtra("passenger", e.getPassengerID());
        startActivityForResult(i, EventConstants.EVENT_REQUEST_ADD_PASSENGER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EventConstants.EVENT_REQUEST_ADD_PASSENGER) {
            if(resultCode == EventConstants.EVENT_RESP_ADD_PASSENGER_OK){
                ((PassengerListFragment)listFrags.get(1)).onListUpdatedEvent();
            }
        }
    }//onActivityResult
}
