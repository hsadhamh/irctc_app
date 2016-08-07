package irctc.factor.app.irctcmadeeasy;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Button;

import com.rey.material.widget.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Adapters.BookTicketAdapter;
import irctc.factor.app.irctcmadeeasy.Events.AddFormsEvent;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.BookingPaymentEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;

import irctc.factor.app.irctcmadeeasy.Fragments.BookingPaymentFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.IGetPaymentValue;
import irctc.factor.app.irctcmadeeasy.Fragments.IGetValue;
import irctc.factor.app.irctcmadeeasy.Fragments.PassengerListFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.TrainDetailsFragment;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;
import irctc.factor.app.irctcmadeeasy.database.TicketDetails;
import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class BookTicketsNowActivity extends AppCompatActivity implements IGetValue,IGetPaymentValue {

    @BindView(R.id.id_pager_indicator)
    TabPageIndicator mPagerIndicator;
    @BindView(R.id.id_pager_verses)
    ViewPager mPager;
    @BindView(R.id.btn_save_info)
    Button mSaveForLaterButton;


    String[] mPagerTitles = {"Train Details","Passengers","Payment"};

    List<Fragment> mListUiFragments = new ArrayList<>();

    MaterialDialog mWarnDialog = null;
    /* 0 -> Nothing; 1 -> Save; 2 -> Book */
    int mAction = 0; /**/
    TicketJson mFinalJson = null;
    BookTicketAdapter bookTicketAdapter;
    TicketJson finalJson = new TicketJson();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_tickets);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        mListUiFragments.add(new TrainDetailsFragment());
        mListUiFragments.add(new PassengerListFragment());
        mListUiFragments.add(new BookingPaymentFragment());



        bookTicketAdapter = new BookTicketAdapter(getSupportFragmentManager(), mListUiFragments, mPagerTitles);
        mPager.setAdapter(bookTicketAdapter);

        assert mPagerIndicator != null;
        mPagerIndicator.setViewPager(mPager);





        mWarnDialog = new MaterialDialog
                        .Builder(BookTicketsNowActivity.this)
                        .title("Input Review")
                        .positiveText("Continue")
                        .negativeText("Review")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                switch (mAction){
                                    case 1:
                                        SaveInformationToDB();
                                        break;
                                    case 2:
                                        startBooking();
                                        break;
                                }

                            }
                        })
                        .cancelable(true)
                        .build();

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
                ((PassengerListFragment)mListUiFragments.get(1)).onListUpdatedEvent();
            }
        }
    }//onActivityResult


    @OnClick(R.id.btn_save_info)
    public void saveAsForm(){
        EventBus.getDefault().post(new AddFormsEvent(""));
        EventBus.getDefault().post(new BookingPaymentEvent(""));
        EventBus.getDefault().post(new BookingPaymentEvent(""));




        /* Validate all values */
       String msg = GetWarningsIfAny(finalJson);

        if(!msg.isEmpty()){
            mWarnDialog.setContent(msg);
            mWarnDialog.show();
        }


      // mFinalJson = GetJsonPopulated();
        /* Validate all values */
       /* String msg = GetWarningsIfAny(mFinalJson);

        if(!msg.isEmpty()){
            mWarnDialog.setContent(msg);
            mWarnDialog.show();
        }*/
    }

    String GetWarningsIfAny(TicketJson finalJson){
        /* Validate all values */
        String msg = "";
        int nSize = msg.length();
        if(finalJson.getUserName().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Username</b>";
        if(finalJson.getPassword().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Password</b>";
        if(finalJson.getSrcStation().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Source Station</b>";
        if(finalJson.getDestStation().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Destination</b>";
        if(finalJson.getDateOfJourney().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Date of Journey</b>";
        if(finalJson.getTrainNumber().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Train Number</b>";
        if(finalJson.getClassSelection().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Class</b>";
        if(finalJson.getQuota().isEmpty())
            msg += (msg.isEmpty() ? "" : ", ") + "<b>Quota</b>";

        msg += "\n";
        if(finalJson.getPassengerInfo().size() == 0)
            msg += (msg.isEmpty() ? "" : ", ") + "No passengers selected.";
        if(finalJson.getPassengerInfo().size() > 4 && finalJson.getQuota().equals("TATKAL"))
            msg += (msg.isEmpty() ? "" : ", ")  + "More than 4 passengers selected. Only first 4 will be applied in tatkal booking.";
        if(finalJson.getPassengerInfo().size() > 6)
            msg += (msg.isEmpty() ? "" : ", ") + "More than 6 passengers selected. Only first 4 will be applied in booking.";

        if(msg.length() > 2)
            msg = "Following information are provided \n" + msg;
        else
            msg="";
        return msg;
    }

    TicketJson GetJsonPopulated(){
        TicketJson finalJson = new TicketJson();

        FragmentManager frg=getSupportFragmentManager();

       /* String trainTag=bookTicketAdapter.getItemTag(0);
        String pasTag=bookTicketAdapter.getItemTag(1);
        String bookTag=bookTicketAdapter.getItemTag(2);*/

       TrainDetailsFragment trainFrag = (TrainDetailsFragment)bookTicketAdapter.getItem(0);
       PassengerListFragment pasFrag = (PassengerListFragment)bookTicketAdapter.getItem(1);
       BookingPaymentFragment bookFrag = (BookingPaymentFragment)bookTicketAdapter.getItem(2);




        TicketJson jsonTicket = trainFrag.GetJsonObjectFilled();
        TicketJson jsonPassenger = pasFrag.GetJsonObjectFilled();
       TicketJson jsonPay = bookFrag.GetJsonObjectFilled();

/*
        finalJson.setUserName(jsonTicket.getUserName());
        finalJson.setPassword(jsonTicket.getPassword());
        finalJson.setSrcStation(jsonTicket.getSrcStation());
        finalJson.setDestStation(jsonTicket.getDestStation());
        finalJson.setBoardingStation(jsonTicket.getBoardingStation());
        finalJson.setDateOfJourney(jsonTicket.getDateOfJourney());
        finalJson.setTrainNumber(jsonTicket.getTrainNumber());
        finalJson.setClassSelection(jsonTicket.getClassSelection());
        finalJson.setQuota(jsonTicket.getQuota());
        finalJson.setPreferredCoachSelection(jsonTicket.getPreferredCoachSelection());
        finalJson.setPreferredCoachID(jsonTicket.getPreferredCoachID());
        finalJson.setAutoUpgrade(jsonTicket.getAutoUpgrade());
        finalJson.setBookConfirmTickets(jsonTicket.getBookConfirmTickets());
        finalJson.setBookConfirmTicketOption(jsonTicket.getBookConfirmTicketOption());
        finalJson.setMobileNumber(jsonTicket.getMobileNumber());
*/

        /* PASSENGERS */
        finalJson.getPassengerInfo().addAll(jsonPassenger.getPassengerInfo());
        finalJson.getChildInfo().addAll(jsonPassenger.getChildInfo());

        /* PAYMENT */
        finalJson.setPaymentMode(jsonPay.getPaymentMode());
        finalJson.setPaymentModeOptionID(jsonPay.getPaymentModeOptionID());
        finalJson.setCardNumberValue(jsonPay.getCardNumberValue());
        finalJson.setCardType(jsonPay.getCardType());
        finalJson.setExpiryMonth(jsonPay.getExpiryMonth());
        finalJson.setExpiryYear(jsonPay.getExpiryYear());
        finalJson.setCardCvvNumber(jsonPay.getCardCvvNumber());
        finalJson.setNameOnCard(jsonPay.getNameOnCard());

        return finalJson;
    }

    void SaveInformationToDB(){
        /* CODE to insert */
    }

    void startBooking(){
        /* Code for next activity after JSON string */
    }

    @Override
    public void getEditTextValue(TicketJson jsonTicket) {



        finalJson.setUserName(jsonTicket.getUserName());

        finalJson.setPassword(jsonTicket.getPassword());
        finalJson.setSrcStation(jsonTicket.getSrcStation());
        finalJson.setDestStation(jsonTicket.getDestStation());
        finalJson.setBoardingStation(jsonTicket.getBoardingStation());
        finalJson.setDateOfJourney(jsonTicket.getDateOfJourney());
        finalJson.setTrainNumber(jsonTicket.getTrainNumber());
        finalJson.setClassSelection(jsonTicket.getClassSelection());
        finalJson.setQuota(jsonTicket.getQuota());
        finalJson.setPreferredCoachSelection(jsonTicket.getPreferredCoachSelection());
        finalJson.setPreferredCoachID(jsonTicket.getPreferredCoachID());
        finalJson.setAutoUpgrade(jsonTicket.getAutoUpgrade());
        finalJson.setBookConfirmTickets(jsonTicket.getBookConfirmTickets());
        finalJson.setBookConfirmTicketOption(jsonTicket.getBookConfirmTicketOption());
        finalJson.setMobileNumber(jsonTicket.getMobileNumber());

        /* PASSENGERS */
        finalJson.getPassengerInfo().addAll(jsonTicket.getPassengerInfo());
        finalJson.getChildInfo().addAll(jsonTicket.getChildInfo());

    }

    @Override
    public void getPaymentValue(TicketJson jsonPay)
    {
        finalJson.setPaymentMode(jsonPay.getPaymentMode());
        finalJson.setPaymentModeOptionID(jsonPay.getPaymentModeOptionID());
        finalJson.setCardNumberValue(jsonPay.getCardNumberValue());
        finalJson.setCardType(jsonPay.getCardType());
        finalJson.setExpiryMonth(jsonPay.getExpiryMonth());
        finalJson.setExpiryYear(jsonPay.getExpiryYear());
        finalJson.setCardCvvNumber(jsonPay.getCardCvvNumber());
        finalJson.setNameOnCard(jsonPay.getNameOnCard());
    }
}
