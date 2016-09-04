package irctc.factor.app.irctcmadeeasy.Activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Adapters.TicketDetailsCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Events.DeleteFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.EditFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.StartBookingEvent;
import irctc.factor.app.irctcmadeeasy.Fragments.TrainDetailsFragment;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.Json.flJsonParser;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.TicketDetails;
import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;


/**
 * Created by hassanhussain on 7/9/2016.
 */
public class TicketsListActivity extends AppCompatActivity {
    @BindView(R.id.id_list_forms)
    public RecyclerView mListForm;
    @BindView(R.id.fab_add_forms)
    public FloatingActionButton mListAddForm;
    @BindView(R.id.center_button)
    public android.widget.ImageView mCenterButton;
    @BindView(R.id.id_discover_btn)
    public Button mDiscoverButton;
    @BindView(R.id.entry_view)
    public LinearLayout mLyEntrytext;
    TicketDetailsCursorAdapter mAdapter = null;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    List<Fragment> mListUiFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_forms);
        ButterKnife.setDebug(true);
        InitializeView();

        if(getTicketsDao() == null || getTicketsDao().count() < 1) {
            mListAddForm.setVisibility(View.GONE);
            mListForm.setVisibility(View.GONE);
            //getSupportActionBar().hide();
        }
        else{
            mLyEntrytext.setVisibility(LinearLayout.GONE);
        }
    }

    public void setCursorAdapterToList(){
        Cursor localCursor = TicketConstants
                .getWritableDatabase(getApplicationContext())
                .query(getTicketsDao().getTablename(), getTicketsDao().getAllColumns(), null, null, null, null, "");
        mAdapter = new TicketDetailsCursorAdapter(this, localCursor);
        mListForm.setAdapter(mAdapter);
        if(localCursor!=null&&localCursor.getCount()>2){
            mLyEntrytext.setVisibility(LinearLayout.GONE);
        }
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

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @OnClick(R.id.fab_add_forms)
    public void addFormsOnclick1(){
        Intent i = new Intent(TicketsListActivity.this,BookTicketsNowActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.center_button)
    public void addFormsOnclick(){
        //Intent i = new Intent(TicketsListActivity.this,BookTicketsNowActivity.class);
        //startActivity(i);
    }

    @OnClick(R.id.id_discover_btn)
    public void discoverClick(){
        //Intent i = new Intent(TicketsListActivity.this,IntroSlideActivity.class);
        Intent i = new Intent(TicketsListActivity.this,BookTicketsNowActivity.class);
        startActivity(i);
    }

    public void InitializeView(){
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListForm.setLayoutManager(linearLayoutManager);
        mListUiFragments.add(new TrainDetailsFragment());
        /*  Forms List Initialize   */

        setCursorAdapterToList();
    }

    protected void onStart() { super.onStart(); }

    @Subscribe
    public void onEvent(final EditFormInfo e){
        Intent i = new Intent(TicketsListActivity.this, BookTicketsNowActivity.class);
        i.putExtra("FORM_EDIT_ID", e.getTrainID());
        i.putExtra("CreateNotExists", true);
        startActivity(i);
    }

    public void onListUpdatedEvent(){
        Cursor localCursor = TicketConstants
                .getWritableDatabase(getApplicationContext())
                .query(getTicketsDao().getTablename(), getTicketsDao().getAllColumns(), null, null, null, null, "");
        mAdapter.swapCursor(localCursor);
    }

    @Subscribe
    public void onEventHandle(DeleteFormInfo e){
        TicketDetails train = getTicketsDao().load((long)e.getTrainID());
        getTicketsDao().delete(train);
        onListUpdatedEvent();
    }

    @Subscribe
    public void onEventHandle(StartBookingEvent o){
        final long ticketId = (long)o.id;
        TicketDetails train = getTicketsDao().load(ticketId);
        if(!TicketConstants.CheckIfDatePast(train.getJourneyDate()))
        {
            new MaterialDialog
                .Builder(TicketsListActivity.this)
                .title("Update Date")
                .content("Date is past. Update & Continue booking.")
                .positiveText("Update & Book")
                .negativeText("Close")
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
                        showDatePickerDialog(ticketId);
                    }
                })
                .cancelable(true)
                .show();
        }
        else {
            doBookingNow(ticketId);
        }
    }

    public void doBookingNow(long tId){
        TicketDetails train = getTicketsDao().load(tId);
        Intent i = new Intent(TicketsListActivity.this, IrctcMainActivity.class);
        i.putExtra("JSON_String", train.getJson());
        startActivity(i);
    }

    public TicketDetailsDao getTicketsDao(){
        if(mDaoMaster == null)
            mDaoMaster = new DaoMaster(TicketConstants.getWritableDatabase(getApplicationContext()));
        if(mDaoSession == null)
            mDaoSession = mDaoMaster.newSession();
        return mDaoSession.getTicketDetailsDao();
    }

    public void showDatePickerDialog(final long ticketID){
        final Calendar c = Calendar.getInstance();
        int yr = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(TicketsListActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
                        String newDate = day + "-" + (month +1 < 10 ? ("0" + (month+1)) : (month)) + "-" + year;
                        TicketDetails train = getTicketsDao().load(ticketID);
                        train.setJourneyDate(newDate);
                        try {
                            TicketJson json = flJsonParser.getTicketDetailObject(train.getJson());
                            json.setDatejourney(newDate);
                            String newJson = flJsonParser.getTicketDetailString(json);
                            train.setJson(newJson);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        getTicketsDao().update(train);
                        onListUpdatedEvent();
                        doBookingNow(ticketID);
                    }
                }, yr, mm, dd);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }
}