package irctc.factor.app.irctcmadeeasy.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Adapters.BookTicketAdapter;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;
import irctc.factor.app.irctcmadeeasy.Events.GetMeJsonValues;
import irctc.factor.app.irctcmadeeasy.Fragments.BookingPaymentFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.PassengerListFragmentV2;
import irctc.factor.app.irctcmadeeasy.Fragments.TrainDetailsFragment;
import irctc.factor.app.irctcmadeeasy.Interfaces.IGetValue;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.Json.flJsonParser;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.TicketDetails;
import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class BookTicketsNowActivity extends AppCompatActivity implements IGetValue {

    @BindView(R.id.id_pager_indicator)
    TabPageIndicator mPagerIndicator;
    @BindView(R.id.id_pager_verses)
    ViewPager mPager;
    @BindView(R.id.btn_save_info)
    Button mSaveForLaterButton;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    String[] mPagerTitles = {"Train Details","Passengers","Payment"};
    List<Fragment> mListUiFragments = new ArrayList<>();
    MaterialDialog mWarnDialog = null, mCreateWarnDialog = null;
    /* 0 -> Nothing; 1 -> Save; 2 -> Book */
    int mAction = 0; /**/
    TicketJson mFinalJson = null;
    String msFinalJson = "";
    BookTicketAdapter mBookTicketAdapter;

    long mnDetailsID = 0;
    boolean mCreateNotExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_tickets);
        ButterKnife.setDebug(true);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            mnDetailsID = (int)b.get("FORM_EDIT_ID");
            mCreateNotExists = (boolean) b.get("CreateNotExists");
        }

        mListUiFragments.add(new TrainDetailsFragment());
        mListUiFragments.add(new PassengerListFragmentV2());
        mListUiFragments.add(new BookingPaymentFragment());

        InitializeView();
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
        if(mnDetailsID > 0) { LoadInfo(); }
    }

    public void InitializeView(){
        ButterKnife.bind(this);
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
                        switch (mAction) {
                            case 0:
                                SaveInformationToDB();
                                break;
                            case 1:
                                startBooking();
                                break;
                        }
                    }
                })
                .cancelable(true)
                .build();
        mBookTicketAdapter = new BookTicketAdapter(getSupportFragmentManager(), mListUiFragments, mPagerTitles);
        mPager.setAdapter(mBookTicketAdapter);
        mPager.setOffscreenPageLimit(2);
        assert mPagerIndicator != null;
        mPagerIndicator.setViewPager(mPager);
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
    }//onActivityResult

    @OnClick({ R.id.btn_save_info, R.id.btn_book_now_info })
    public void onButtonClick(View btn){
        mAction = btn.getId() == R.id.btn_book_now_info ? 1 : 0;
        mFinalJson = null;
        mFinalJson = new TicketJson();
        EventBus.getDefault().post(new GetMeJsonValues());
        /* Validate all values */
        String msg = GetWarningsIfAny();
        if(!msg.isEmpty()){
            mWarnDialog.setContent(Html.fromHtml(msg));
            mWarnDialog.show();
        }
        else{
            switch (mAction) {
                case 0:
                    SaveInformationToDB();
                    break;
                case 1:
                    startBooking();
                    break;
            }
        }

    }

    String GetWarningsIfAny(){
        /* Validate all values */
        String msg = "";
        if(mFinalJson.getUsername().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Username</b>";
        if(mFinalJson.getPassword().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Password</b>";
        if(mFinalJson.getSource().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Source Station</b>";
        if(mFinalJson.getDestination().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Destination</b>";
        if(mFinalJson.getDatejourney().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Date of Journey</b>";
        if(mFinalJson.getTrainno().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Train Number</b>";
        if(mFinalJson.getBclass().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Class</b>";
        if(mFinalJson.getQuota().isEmpty())
            msg += (msg.isEmpty() ? "" : ",\n ") + "<b>Quota</b>";

        msg += "\n";
        if(mFinalJson.getPassengerinfo().size() == 0)
            msg += (msg.isEmpty() ? "" : ",\n ") + "No passengers selected.";
        if(mFinalJson.getPassengerinfo().size() > 4 && mFinalJson.getQuota().equals("TATKAL"))
            msg += (msg.isEmpty() ? "" : ",\n ")  + "More than 4 passengers selected. Only first 4 will be applied in tatkal booking.";
        if(mFinalJson.getPassengerinfo().size() > 6)
            msg += (msg.isEmpty() ? "" : ",\n ") + "More than 6 passengers selected. Only first 4 will be applied in booking.";

        if(msg.length() > 2)
            msg = "Following information are not provided \n" + msg;
        else
            msg="";
        return msg;
    }

    void SaveInformationToDB(){
        getJsonString();
        Log.d("Json ", "Json : " + msFinalJson);
        if(msFinalJson.length() > 0){
            TicketDetails oTicketSave = new TicketDetails();
            oTicketSave.setSource(mFinalJson.getSource());
            oTicketSave.setDestination(mFinalJson.getDestination());
            oTicketSave.setBoarding(mFinalJson.getBoarding());
            oTicketSave.setJourneyDate(mFinalJson.getDatejourney());
            oTicketSave.setTrainno(mFinalJson.getTrainno());
            oTicketSave.setIrctcClass(mFinalJson.getBclass());
            oTicketSave.setQuota(mFinalJson.getQuota());
            oTicketSave.setMobileNumber(mFinalJson.getMobile());
            oTicketSave.setCoach(mFinalJson.getCoachid());
            oTicketSave.setAutoUpgrade(mFinalJson.isAutoupgrade() ? "true" : "false");
            oTicketSave.setBookOnConfirm(mFinalJson.isBookconfirm() ? "true" : "false");
            oTicketSave.setConditionsOnBook(mFinalJson.getBookidcond());
            oTicketSave.setTrainno(mFinalJson.getTrainno());
            oTicketSave.setJson(msFinalJson);

            if (mnDetailsID > 0) {
                oTicketSave.setId(mnDetailsID);
                getTicketInfo().update(oTicketSave);
            }
            else
                getTicketInfo().insert(oTicketSave);
        }
        finish();
    }

    void startBooking(){
        goForBooking();
    }

    void goForBooking(){
        /* Code for next activity after JSON string */
        getJsonString();
        Intent i = new Intent(BookTicketsNowActivity.this, IrctcMainActivity.class);
        i.putExtra("JSON_String", msFinalJson);
        startActivity(i);
        finish();
    }

    public void getJsonString(){
        /* CODE to insert */
        try {
            msFinalJson = "";
            msFinalJson = flJsonParser.getTicketDetailString(mFinalJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(BookTicketsNowActivity.this,
                    "Failed to create JSON on exception("+ e.getMessage() +").", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(BookTicketsNowActivity.this,
                    "Failed to create JSON on exception("+ e.getMessage() +").", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getTicketJsonValue(TicketJson jsonTicket) {
        mFinalJson.setUsername(jsonTicket.getUsername());
        mFinalJson.setPassword(jsonTicket.getPassword());
        mFinalJson.setSource(jsonTicket.getSource());
        mFinalJson.setDestination(jsonTicket.getDestination());
        mFinalJson.setBoarding(jsonTicket.getBoarding());
        mFinalJson.setDatejourney(jsonTicket.getDatejourney());
        mFinalJson.setTrainno(jsonTicket.getTrainno());
        mFinalJson.setBclass(jsonTicket.getBclass());
        mFinalJson.setQuota(jsonTicket.getQuota());
        mFinalJson.setPrefercoach(jsonTicket.isPrefercoach());
        mFinalJson.setCoachid(jsonTicket.getCoachid());
        mFinalJson.setAutoupgrade(jsonTicket.isAutoupgrade());
        mFinalJson.setBookconfirm(jsonTicket.isBookconfirm());
        mFinalJson.setBookidcond(jsonTicket.getBookidcond());
        mFinalJson.setMobile(jsonTicket.getMobile());
    }

    @Override
    public void getPaymentJsonValue(TicketJson jsonPay) {
        mFinalJson.setPaymentmode(jsonPay.getPaymentmode());
        mFinalJson.setPaymentmodeid(jsonPay.getPaymentmodeid());
        mFinalJson.setCardnovalue(jsonPay.getCardnovalue());
        mFinalJson.setCardtype(jsonPay.getCardtype());
        mFinalJson.setExpirymon(jsonPay.getExpirymon());
        mFinalJson.setExpiryyear(jsonPay.getExpiryyear());
        mFinalJson.setCardcvv(jsonPay.getCardcvv());
        mFinalJson.setNamecard(jsonPay.getNamecard());
    }

    @Override
    public void getPassengerJsonValue(TicketJson jsonTicket) {
        /* PASSENGERS */
        mFinalJson.getPassengerinfo().clear();
        mFinalJson.getPassengerinfo().addAll(jsonTicket.getPassengerinfo());
        mFinalJson.getChildinfo().clear();
        mFinalJson.getChildinfo().addAll(jsonTicket.getChildinfo());
    }

    public TicketDetailsDao getTicketInfo(){
        if(mDaoMaster == null)
            mDaoMaster = new DaoMaster(TicketConstants.getWritableDatabase(this.getApplicationContext()));
        if(mDaoSession == null)
            mDaoSession = mDaoMaster.newSession();
        return mDaoSession.getTicketDetailsDao();
    }

    public void LoadInfo(){
        TicketDetails pass = getTicketInfo().load(mnDetailsID);
        String sJson = pass.getJson();
        try {
            if(sJson.length() > 0) {
                mFinalJson = flJsonParser.getTicketDetailObject(sJson);
                ((TrainDetailsFragment)mListUiFragments.get(0)).setPassedJson(mFinalJson);
                ((PassengerListFragmentV2)mListUiFragments.get(1)).setPassedJson(mFinalJson);
                ((BookingPaymentFragment)mListUiFragments.get(2)).setPassedJson(mFinalJson);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
