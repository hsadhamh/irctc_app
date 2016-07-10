package irctc.factor.app.irctcmadeeasy.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.R;

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

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_passengers, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
