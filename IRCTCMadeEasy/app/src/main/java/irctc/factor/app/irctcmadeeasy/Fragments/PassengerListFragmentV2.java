package irctc.factor.app.irctcmadeeasy.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.flexbox.FlexboxLayout;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.TextView;

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
public final class PassengerListFragmentV2 extends Fragment{

    @BindView(R.id.id_passenger_container)
    android.widget.LinearLayout mLayoutPassenger;
    @BindView(R.id.id_child_container)
    android.widget.LinearLayout mLayoutChild;

    @BindView(R.id.id_add_btn_passenger)
    Button mAddPassenger;
    @BindView(R.id.id_add_btn_child)
    Button mAddChild;

    private Unbinder unbinder;
    IGetValue mCallback;
    TicketJson mPassedJson = null;
    LayoutInflater mInflater = null;

    int mnPassRef = -1;
    int mnChildRef = -1;
    int mnPassCount = 0;
    int mnChildCount = 0;

    List<View> mListPassViews = new ArrayList<>();
    List<View> mListChildViews = new ArrayList<>();

    public TicketJson getPassedJson() { return mPassedJson; }
    public void setPassedJson(TicketJson mPassedJson) { this.mPassedJson = mPassedJson; }

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

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.d("PassengerListFragment", "onDestroyView: ");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_passenger_view2, container, false);
        unbinder = ButterKnife.bind(this, view);
        mInflater = inflater;

        if(mPassedJson != null)
            LoadValue();
        else {
            addPassengerView(++mnPassRef);
            mnPassCount++;
        }

        return view;
    }

    @OnClick(R.id.id_add_btn_passenger)
    public void onAddPassenger(){
        if(mnPassCount < 6) {
            addPassengerView(++mnPassRef);
            mnPassCount++;
        }
        else{
            Toast.makeText(getContext(), "Cannot add more than 6 passengers for a ticket.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.id_add_btn_child)
    public void onAddChild(){
        if(mnChildCount < 2) {
            addChildPassengerView(++mnChildRef);
            mnChildCount++;
        }
        else{
            Toast.makeText(getContext(), "Cannot add more than 2 child passengers for a ticket.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPassengerView(int nRef){
        android.widget.LinearLayout passInfo = (android.widget.LinearLayout) InflateAndAddView(R.layout.layout_passenger_info, nRef);
        mLayoutPassenger.addView(passInfo, mLayoutPassenger.getChildCount() - 1);
        mListPassViews.add(passInfo);
    }

    public void addChildPassengerView(int nRef){
        android.widget.LinearLayout passInfo = (android.widget.LinearLayout) InflateAndAddView(R.layout.layout_child_info, nRef);
        mLayoutChild.addView(passInfo, mLayoutChild.getChildCount() - 1);
        mListChildViews.add(passInfo);
    }

    public TicketJson GetJsonObjectFilled() {
        TicketJson oJsonTicket = new TicketJson();
        for(View v : mListPassViews){
            if(v.getTag() instanceof PassengerInfoViewHolder) {
                PassengerInfoViewHolder holder = (PassengerInfoViewHolder) v.getTag();

                PassengerJson passJson = new PassengerJson();
                passJson.setGender(holder.radio_male.isChecked() ? "Male" : "Female");
                passJson.setAge(holder.passenger_age.getText().toString());
                passJson.setBerth(holder.spinner_berth.getSelectedItem().toString());
                passJson.setName(holder.passenger_name.getText().toString());
                passJson.setIDCard("" + 1234);
                passJson.setIDCardNumber("" + 1234);
                passJson.setNationality("Indian");
                int nAge = Integer.parseInt(holder.passenger_age.getText().toString());
                passJson.setType((nAge <=5? "CHILD" : nAge >=60? "SENIOR" : "ADULT"));

                oJsonTicket.getPassengerInfo().add(passJson);
            }
        }

        for(View v : mListChildViews){
            if(v.getTag() instanceof ChildPassengerInfoViewHolder) {
                ChildPassengerInfoViewHolder holder = (ChildPassengerInfoViewHolder) v.getTag();

                ChildJson child = new ChildJson();
                child.setGender(holder.radio_male.isChecked() ? "Male" : "Female");
                child.setAge(holder.passenger_age.getText().toString());
                child.setName(holder.passenger_name.getText().toString());
                oJsonTicket.getChildInfo().add(child);
            }
        }
        return oJsonTicket;
    }

    public void LoadValue() {
        TicketJson oJson = mPassedJson;
        for(PassengerJson oPass : oJson.getPassengerInfo()){
            if(mnPassCount < 6) {
                mnPassCount++;
                addPassengerView(++mnPassRef);
                View v = FindViewFromList(mnPassRef, mListPassViews);
                if (v != null) {
                    PassengerInfoViewHolder info = (PassengerInfoViewHolder) v.getTag();
                    info.passenger_name.setText(oPass.getName());
                    info.passenger_age.setText(oPass.getAge());
                    if (oPass.getGender().equalsIgnoreCase("male"))
                        info.radio_male.setChecked(true);
                    else if (oPass.getGender().equalsIgnoreCase("female"))
                        info.radio_female.setChecked(true);

                    if (oPass.getType() != null && oPass.getType().equals("CHILD"))
                        info.check_box_child.setChecked(true);
                    else if (oPass.getType() != null && oPass.getType().equals("SENIOR"))
                        info.check_box_senior.setChecked(true);

                /*if(oPass.getFood().equals("NonVeg"))
                    mFoodOption.setSelection(2);
                else*/
                    info.spinner_food.setSelection(1);
                    info.spinner_berth.setSelection(getBerthIndex(oPass.getBerth()));

                }
            }
        }

        for(ChildJson oPass : oJson.getChildInfo()){
            if(mnChildCount < 2) {
                mnChildCount++;
                addChildPassengerView(++mnChildRef);
                View v = FindViewFromList(mnChildRef, mListChildViews);
                if (v != null) {
                    ChildPassengerInfoViewHolder info = (ChildPassengerInfoViewHolder) v.getTag();
                    info.passenger_name.setText(oPass.getName());
                    info.passenger_age.setText(oPass.getAge());
                    if (oPass.getGender().equalsIgnoreCase("male"))
                        info.radio_male.setChecked(true);
                    else if (oPass.getGender().equalsIgnoreCase("female"))
                        info.radio_female.setChecked(true);
                }
            }
        }
    }

    public int getBerthIndex(String s){
        if(s.equals("Lower"))
            return 1;
        else if(s.equals("Middle"))
            return 2;
        else if(s.equals("Upper"))
            return 3;
        else if(s.equals("Side Upper"))
            return 4;
        else if(s.equals("Side Lower"))
            return 5;
        else if(s.equals("Window Seat"))
            return 6;
        return 0;
    }

    public View FindViewFromList(int n, List<View> listViews){
        for(View temp : listViews){
            if(temp.getTag() instanceof ChildPassengerInfoViewHolder){
                if(((ChildPassengerInfoViewHolder)temp.getTag()).nRef == n)
                    return temp;
            }
            else if(temp.getTag() instanceof PassengerInfoViewHolder){
                if(((PassengerInfoViewHolder)temp.getTag()).nRef == n)
                    return temp;
            }
        }
        return null;
    }

    public View InflateAndAddView(@LayoutRes int nRes, int nRef) {
        View v = mInflater.inflate(nRes, null, false);
        if(nRes == R.layout.layout_child_info)
            v.setTag(new ChildPassengerInfoViewHolder(v, nRef));
        else if(nRes == R.layout.layout_passenger_info)
            v.setTag(new PassengerInfoViewHolder(v, nRef));
        return v;
    }

    @Subscribe
    public void onEvent(GetMeJsonValues e) { mCallback.getPassengerJsonValue(GetJsonObjectFilled()); }

    public void OnDeletePassengerView(int type, int ref) {
        int nFoundIndex = -1;
        boolean found = false;
        if(type == 1){
            for(View v : mListPassViews){
                ++nFoundIndex;
                if(v.getTag() instanceof PassengerInfoViewHolder){
                    PassengerInfoViewHolder holder = (PassengerInfoViewHolder)v.getTag();
                    if(holder.nRef == ref){
                        found = true;
                        break;
                    }
                }
            }
            if(nFoundIndex > -1 && nFoundIndex < mListPassViews.size() && found) {
                View v1 = mListPassViews.get(nFoundIndex);
                mLayoutPassenger.removeView(v1);
                mListPassViews.remove(nFoundIndex);
                mnPassCount--;
            }
        }
        else if(type == 2) {
            for(View v : mListChildViews){
                ++nFoundIndex;
                if(v.getTag() instanceof ChildPassengerInfoViewHolder){
                    ChildPassengerInfoViewHolder holder = (ChildPassengerInfoViewHolder)v.getTag();
                    if(holder.nRef == ref){
                        found = true;
                        break;
                    }
                }
            }
            if(nFoundIndex > -1 && nFoundIndex < mListChildViews.size() && found) {
                View v1 = mListChildViews.get(nFoundIndex);
                mLayoutChild.removeView(v1);
                mListChildViews.remove(nFoundIndex);
                mnChildCount--;
            }
        }
    }

    class ChildPassengerInfoViewHolder {
        @BindView(R.id.id_passenger_title)
        TextView passenger_title;
        @BindView(R.id.id_passenger_delete)
        Button delete;
        @BindView(R.id.txt_passenger_name)
        EditText passenger_name;
        @BindView(R.id.txt_passenger_age)
        EditText passenger_age;
        @BindView(R.id.layout_radio_group1)
        FlexboxLayout gender_group;
        @BindView(R.id.id_radio_male)
        RadioButton radio_male;
        @BindView(R.id.id_radio_female)
        RadioButton radio_female;
        int nRef;

        ChildPassengerInfoViewHolder(View v, int ref) {
            ButterKnife.bind(this, v);
            nRef = ref;
        }

        @OnClick(R.id.id_passenger_delete)
        public void onDeleteCall(){
            OnDeletePassengerView(2, nRef);
        }

        @OnCheckedChanged({R.id.id_radio_male, R.id.id_radio_female})
        public void onRadioClick(RadioButton btn, boolean isChecked){
            if(isChecked){
                if(btn.getId() == R.id.id_radio_male){
                    radio_female.setChecked(false);
                }
                else if(btn.getId() == R.id.id_radio_female){
                    radio_male.setChecked(false);
                }
            }
        }
    }

    class PassengerInfoViewHolder {
        @BindView(R.id.id_passenger_title)
        TextView passenger_title;
        @BindView(R.id.id_passenger_delete)
        Button delete;
        @BindView(R.id.txt_passenger_name)
        EditText passenger_name;
        @BindView(R.id.txt_passenger_age)
        EditText passenger_age;
        @BindView(R.id.layout_radio_group1)
        FlexboxLayout gender_group;
        @BindView(R.id.id_radio_male)
        RadioButton radio_male;
        @BindView(R.id.id_radio_female)
        RadioButton radio_female;
        @BindView(R.id.spinner_berth)
        Spinner spinner_berth;
        @BindView(R.id.spinner_food)
        Spinner spinner_food;
        @BindView(R.id.txt_passenger_nationality)
        EditText passenger_nationality;
        @BindView(R.id.layout_misc_options)
        FlexboxLayout layout_misc_options;
        @BindView(R.id.check_box_child)
        CheckBox check_box_child;
        @BindView(R.id.check_box_senior)
        CheckBox check_box_senior;
        @BindView(R.id.check_box_bed_roll)
        CheckBox check_box_bed_roll;

        @BindView(R.id.check_box_opt_berth)
        CheckBox check_box_opt_berth;

        int nRef;

        PassengerInfoViewHolder(View v, int ref) {
            ButterKnife.bind(this, v);
            nRef = ref;
            setAdapters();
            setUiListeners();
        }

        @OnClick(R.id.id_passenger_delete)
        public void onDeleteCall(){
            OnDeletePassengerView(1, nRef);
        }

        public void setAdapters(){
            ArrayAdapter<String> spBerthAdapter = new ArrayAdapter<String>(getContext(),
                    R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_berth));
            spBerthAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
            spinner_berth.setAdapter(spBerthAdapter);

            ArrayAdapter<String> spFoodAdapter = new ArrayAdapter<String>(getContext(),
                    R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_food));
            spFoodAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
            spinner_food.setAdapter(spFoodAdapter);
        }

        public void setUiListeners(){
            passenger_age.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String sAge = s.toString();
                    if(!sAge.isEmpty()) {
                        int age = Integer.parseInt(sAge);
                        check_box_child.setEnabled(age > 0 && age < 6);
                        check_box_child.setChecked(age > 0 && age < 6);
                        check_box_senior.setEnabled(age >= 60);
                        check_box_senior.setChecked(age >= 60);
                    }
                    else {
                        check_box_child.setEnabled(false);
                        check_box_child.setChecked(false);
                        check_box_senior.setEnabled(false);
                        check_box_senior.setChecked(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) { }
            });
        }

        @OnCheckedChanged(R.id.check_box_child)
        public void onChildChecked(){
            spinner_berth.setEnabled(!check_box_child.isChecked());
            spinner_food.setEnabled(!check_box_child.isChecked());
            check_box_child.setEnabled(check_box_child.isChecked());
            check_box_senior.setEnabled(!check_box_child.isChecked());
            check_box_bed_roll.setEnabled(!check_box_child.isChecked());
            check_box_opt_berth.setEnabled(!check_box_child.isChecked());
        }

        @OnCheckedChanged({R.id.id_radio_male, R.id.id_radio_female})
        public void onRadioClick(RadioButton btn, boolean isChecked){
            if(isChecked){
                if(btn.getId() == R.id.id_radio_male){
                    radio_female.setChecked(false);
                }
                else if(btn.getId() == R.id.id_radio_female){
                    radio_male.setChecked(false);
                }
            }
        }
    }
}
