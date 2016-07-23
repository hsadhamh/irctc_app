package irctc.factor.app.irctcmadeeasy.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.PassengerCursorAdapter;
import irctc.factor.app.irctcmadeeasy.AddPassengerActivity;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.DeletePassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;
import irctc.factor.app.irctcmadeeasy.Events.PassengerListUpdated;
import irctc.factor.app.irctcmadeeasy.Events.SelectPassenger;
import irctc.factor.app.irctcmadeeasy.Events.UnselectPassenger;
import irctc.factor.app.irctcmadeeasy.Json.ChildJson;
import irctc.factor.app.irctcmadeeasy.Json.PassengerJson;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class PassengerListFragment extends Fragment{

    @BindView(R.id.id_list_passengers)
    public RecyclerView mListPassengers;
    @BindView(R.id.fab_add_passenger)
    public FloatingActionButton mListAddPassenger;

    private Unbinder unbinder;

    PassengerCursorAdapter mAdapter = null;


    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private PassengerInfoDao mPassengerInfo;

    List<Long> mSelectedPassengerList = new ArrayList();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy(){ super.onDestroy(); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_passengers, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListPassengers.setLayoutManager(linearLayoutManager);

        /*  Passenger List Initialize   */
        mDaoMaster = new DaoMaster(TicketConstants.getReadableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mPassengerInfo = mDaoSession.getPassengerInfoDao();

        setCursorAdapterToList();
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setCursorAdapterToList(){
        Cursor localCursor;
        localCursor = TicketConstants
                    .getReadableDatabase()
                    .query(mPassengerInfo.getTablename(), mPassengerInfo.getAllColumns(), null, null, null, null, "");
        mAdapter = new PassengerCursorAdapter(getActivity().getApplicationContext(), localCursor);
        mListPassengers.setAdapter(mAdapter);
    }

    @OnClick(R.id.fab_add_passenger)
    public void onAddPassengerClick(){ EventBus.getDefault().post(new AddPassengerEvent("")); }

    public void onListUpdatedEvent(){
        Cursor localCursor;
        localCursor = TicketConstants
                .getReadableDatabase()
                .query(mPassengerInfo.getTablename(), mPassengerInfo.getAllColumns(), null, null, null, null, "");
        mAdapter.swapCursor(localCursor);
    }

    @Subscribe
    public void onEventHandle(DeletePassengerEvent e){
        PassengerInfo pass = mPassengerInfo.load((long)e.getPassengerID());
        mPassengerInfo.delete(pass);
        onListUpdatedEvent();
    }

    @Subscribe
    public void onEventHandle(SelectPassenger e){ mSelectedPassengerList.add((long)e.getPassengerID()); }

    @Subscribe
    public void onEventHandle(UnselectPassenger e){ mSelectedPassengerList.remove((long)e.getPassengerID()); }

    public TicketJson GetJsonObjectFilled() {
        TicketJson oJsonTicket = new TicketJson();
        if(!mSelectedPassengerList.isEmpty()){
            for (Long i : mSelectedPassengerList)
            {
                PassengerInfo pass = mPassengerInfo.load(i);
                if(pass.getChild().equals("CHILD"))
                {
                    ChildJson child = new ChildJson();
                    child.setName(pass.getName());
                    child.setAge(""+pass.getAge());
                    child.setGender(pass.getGender());
                    oJsonTicket.getChildInfo().add(child);
                }
                else
                {
                    PassengerJson passJson = new PassengerJson();
                    passJson.setGender(pass.getGender());
                    passJson.setAge(""+pass.getAge());
                    passJson.setBerth(pass.getBerth());
                    passJson.setName(pass.getName());
                    passJson.setIDCard("" + pass.getAadharCardNo());
                    passJson.setIDCardNumber("" + pass.getAadharCardNo());
                    passJson.setNationality(pass.getNationality());
                    passJson.setType(pass.getChild());
                    oJsonTicket.getPassengerInfo().add(passJson);
                }

            }
        }
        return oJsonTicket;
    }

}
