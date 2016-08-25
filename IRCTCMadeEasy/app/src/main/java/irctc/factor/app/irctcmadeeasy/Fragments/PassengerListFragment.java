package irctc.factor.app.irctcmadeeasy.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.PassengerCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.DeletePassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.GetMeJsonValues;
import irctc.factor.app.irctcmadeeasy.Events.SelectPassenger;
import irctc.factor.app.irctcmadeeasy.Events.UnSelectPassenger;
import irctc.factor.app.irctcmadeeasy.Interfaces.IGetValue;
import irctc.factor.app.irctcmadeeasy.Json.ChildJson;
import irctc.factor.app.irctcmadeeasy.Json.PassengerJson;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public final class PassengerListFragment extends Fragment{

    @BindView(R.id.id_list_passengers)
    public RecyclerView mListPassengers;
    @BindView(R.id.fab_add_passenger)
    public FloatingActionButton mListAddPassenger;

    private Unbinder unbinder;
    PassengerCursorAdapter mAdapter = null;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;


    IGetValue mCallback;

    boolean mbCreateApproved = false;
    TicketJson mPassedJson = null;

    public TicketJson getPassedJson() { return mPassedJson; }
    public void setPassedJson(TicketJson mPassedJson) { this.mPassedJson = mPassedJson; }
    public boolean isCreateApproved() { return mbCreateApproved; }
    public void setCreateApproved(boolean mLoadGivenValue) { this.mbCreateApproved = mLoadGivenValue; }

    public static PassengerListFragment newInstance() { return new PassengerListFragment(); }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        mCallback = (IGetValue) context;
    }

    @Override public void onDetach() {
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

        setCursorAdapterToList();

        if(mPassedJson != null)
            LoadValue();

        return view;
    }

    public PassengerInfoDao getPassengerInfo(){
        if(mDaoMaster == null)
            mDaoMaster = new DaoMaster(TicketConstants.getWritableDatabase(this.getContext()));
        if(mDaoSession == null)
            mDaoSession = mDaoMaster.newSession();
        return mDaoSession.getPassengerInfoDao();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.d("PassengerListFragment", "onDestroyView: ");
    }

    public void setCursorAdapterToList(){ onListUpdatedEvent(); }

    @OnClick(R.id.fab_add_passenger)
    public void onAddPassengerClick(){ EventBus.getDefault().post(new AddPassengerEvent("")); }

    public void onListUpdatedEvent(){
        Cursor localCursor = TicketConstants
                .getReadableDatabase(this.getContext())
                .query(getPassengerInfo().getTablename(), getPassengerInfo().getAllColumns(), null, null, null, null, "");
        getAdapter(localCursor).swapCursor(localCursor);
        mListPassengers.setAdapter(getAdapter(localCursor));
    }

    PassengerCursorAdapter getAdapter(Cursor localCursor){
        if(mAdapter == null)
            mAdapter = new PassengerCursorAdapter(this.getContext(), localCursor);
        return mAdapter;
    }

    @Subscribe
    public void onEventHandle(DeletePassengerEvent e){
        PassengerInfo pass = getPassengerInfo().load((long)e.getPassengerID());
        getPassengerInfo().delete(pass);
        onListUpdatedEvent();
    }

    @Subscribe
    public void onEventHandle(SelectPassenger e){
        if(!mAdapter.getSelectedPassengerList().contains((long)e.getPassengerID()))
            mAdapter.getSelectedPassengerList().add((long)e.getPassengerID());
    }

    @Subscribe
    public void onEventHandle(UnSelectPassenger e){
        mAdapter.getSelectedPassengerList().remove((long)e.getPassengerID());
    }

    public TicketJson GetJsonObjectFilled() {
        TicketJson oJsonTicket = new TicketJson();
        if(!mAdapter.getSelectedPassengerList().isEmpty()){
            for (Long i : mAdapter.getSelectedPassengerList()){
                PassengerInfo pass = getPassengerInfo().load(i);
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

    @Subscribe
    public void onEvent(GetMeJsonValues e) { mCallback.getPassengerJsonValue(GetJsonObjectFilled()); }

    public void LoadValue(){
        TicketJson oJson = mPassedJson;
        List<PassengerInfo> listPass = getPassengerInfo().loadAll();
        for(PassengerJson oPass : oJson.getPassengerInfo()){
            boolean bAdd = CheckIfPassengerInfoFound(listPass, oPass);
            if(!bAdd && isCreateApproved()){
                /* Add to DB */
                PassengerInfo passenger = new PassengerInfo();
                passenger.setAadharCardNo(1234);
                passenger.setAge(Integer.parseInt(oPass.getAge()));
                passenger.setChild((passenger.getAge()<=5? "CHILD" : passenger.getAge()>=60? "SENIOR" : "ADULT"));
                passenger.setBerth(oPass.getBerth());
                passenger.setFood("Veg");
                passenger.setGender(oPass.getGender());
                passenger.setName(oPass.getName());
                passenger.setNationality("Indian");
                passenger.setTransactionId(256);

                long nInsertedPassId = getPassengerInfo().insert(passenger);
                if(!mAdapter.getSelectedPassengerList().contains(nInsertedPassId))
                    mAdapter.getSelectedPassengerList().add(nInsertedPassId);
            }
        }
        onListUpdatedEvent();
    }

    boolean CheckIfPassengerInfoFound(List<PassengerInfo> listPass, PassengerJson info){
        for(PassengerInfo pass : listPass){
            if( info.getName().equalsIgnoreCase(pass.getName())
                    && info.getGender().equalsIgnoreCase(pass.getGender() == null ? "Male" : pass.getGender())
                    && pass.getAge() == Integer.parseInt(info.getAge())){
                if(!mAdapter.getSelectedPassengerList().contains(pass.getId()))
                    mAdapter.getSelectedPassengerList().add(pass.getId());
                return true;
            }
        }
        return false;
    }

}
