package irctc.factor.app.irctcmadeeasy.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.PassengerCursorAdapter;
import irctc.factor.app.irctcmadeeasy.AddPassengerActivity;
import irctc.factor.app.irctcmadeeasy.Events.AddPassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;
import irctc.factor.app.irctcmadeeasy.Events.PassengerListUpdated;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class PassengerListFragment extends Fragment {

    @BindView(R.id.id_list_passengers)
    public RecyclerView mListPassengers;
    @BindView(R.id.fab_add_passenger)
    public FloatingActionButton mListAddPassenger;
    @BindView(R.id.fab_more_menu)
    public FloatingActionMenu mMenuMoreOptions;

    private Unbinder unbinder;

    PassengerCursorAdapter mAdapter = null;

    Cursor mCursor;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private PassengerInfoDao mPassengerInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().register(this);
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
        mMenuMoreOptions.setVisibility(view.GONE);

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
        mCursor = TicketConstants
                    .getReadableDatabase()
                    .query(mPassengerInfo.getTablename(), mPassengerInfo.getAllColumns(), null, null, null, null, "");
        mAdapter = new PassengerCursorAdapter(getActivity().getApplicationContext(), mCursor);
        mListPassengers.setAdapter(mAdapter);
    }

    @OnClick(R.id.fab_add_passenger)
    public void onAddPassengerClick(){ EventBus.getDefault().post(new AddPassengerEvent("")); }

    public void onListUpdatedEvent(){ mAdapter.swapCursor(mCursor); }

}
