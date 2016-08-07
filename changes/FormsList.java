package irctc.factor.app.irctcmadeeasy;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;


import com.rey.material.widget.ImageView;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.PassengerCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.TicketDetailsCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Events.AddFormsEvent;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.DeleteFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.DeletePassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;
import irctc.factor.app.irctcmadeeasy.Fragments.PassengerListFragment;
import irctc.factor.app.irctcmadeeasy.Fragments.TrainDetailsFragment;
import irctc.factor.app.irctcmadeeasy.View.ShowHidePasswordEditText;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;
import irctc.factor.app.irctcmadeeasy.database.TicketDetails;
import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;


/**
 * Created by hassanhussain on 7/9/2016.
 */
public class FormsList extends AppCompatActivity {

    @BindView(R.id.id_list_forms)
    public RecyclerView mListForm;
    @BindView(R.id.fab_add_forms)
    public FloatingActionButton mListAddForm;
    @BindView(R.id.center_button)
    public ImageView centerButton;
    @BindView(R.id.id_discover_btn)
    public Button discoverButton;


    public LinearLayout lvEntrytext;

    private Unbinder unbinder;

    TicketDetailsCursorAdapter mAdapter = null;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private TicketDetailsDao mTicketDetails;
    public Button newButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_forms);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        //LinearLayout mainLayout=(LinearLayout)this.findViewById(R.id.form_entry_text);
        LinearLayout entryLayout=(LinearLayout)this.findViewById(R.id.entry_view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListForm.setLayoutManager(linearLayoutManager);

       /*  Forms List Initialize   */
        mDaoMaster = new DaoMaster(TicketConstants.getReadableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mTicketDetails = mDaoSession.getTicketDetailsDao();

        Toast.makeText(getApplicationContext(),mTicketDetails.count()+"",Toast.LENGTH_LONG);
        if(mTicketDetails==null ||mTicketDetails.count()<1)
        {
           //mainLayout.setVisibility(LinearLayout.GONE);
           mListAddForm.setVisibility(View.GONE);
           getSupportActionBar().hide();
        }
        else
        {
            entryLayout.setVisibility(LinearLayout.GONE);
        }


        setCursorAdapterToList();
    }
    public void setCursorAdapterToList(){
        Cursor localCursor;
        localCursor = TicketConstants
                .getReadableDatabase()
                .query(mTicketDetails.getTablename(), mTicketDetails.getAllColumns(), null, null, null, null, "");
        mAdapter = new TicketDetailsCursorAdapter(this, localCursor);
        mListForm.setAdapter(mAdapter);

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
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @OnClick(R.id.fab_add_forms)
    public void addFormsOnclick1(){
        Intent i = new Intent(FormsList.this,BookTicketsNowActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.center_button)
    public void addFormsOnclick(){
        Intent i = new Intent(FormsList.this,BookTicketsNowActivity.class);
        startActivity(i);
    }
@OnClick(R.id.id_discover_btn)
public void discoverClick(){

    Intent i = new Intent(FormsList.this,IntroSlideActivity.class);
    startActivity(i);

}


    @Subscribe
    public void onEvent(EditFormInfo e){
        Intent i = new Intent(FormsList.this, BookTicketsNowActivity.class);
        i.putExtra("form", e.getTrainID());
        startActivity(i);
    }


    public void onListUpdatedEvent(){
        Cursor localCursor;
        localCursor = TicketConstants
                .getReadableDatabase()
                .query(mTicketDetails.getTablename(), mTicketDetails.getAllColumns(), null, null, null, null, "");
        mAdapter.swapCursor(localCursor);
    }

    @Subscribe
    public void onEventHandle(DeleteFormInfo e){
        TicketDetails train = mTicketDetails.load((long)e.getTrainID());
        mTicketDetails.delete(train);
        onListUpdatedEvent();
    }


}