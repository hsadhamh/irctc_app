package irctc.factor.app.irctcmadeeasy.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.AutoCompleteStringAdapter;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.ShowHidePasswordEditText;
import irctc.factor.app.irctcmadeeasy.TicketConstants;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class TrainDetailsFragment extends Fragment {

    @BindView(R.id.txt_username)
    public EditText mvUserName;
    @BindView(R.id.txt_password)
    public ShowHidePasswordEditText mvPassword;
    @BindView(R.id.check_box_save_login)
    public CheckBox mvCbSaveLogin;

    @BindView(R.id.auto_complete_source)
    public AutoCompleteTextView mvStationSource;
    @BindView(R.id.auto_complete_destination)
    public AutoCompleteTextView mvStationDestination;
    @BindView(R.id.auto_complete_boarding)
    public AutoCompleteTextView mvStationBoarding;
    @BindView(R.id.txt_journey_date)
    public EditText mvDateJourney;
    @BindView(R.id.btn_calendar_click)
    public ImageButton mvDateCalButton;

    @BindView(R.id.auto_complete_trains)
    public AutoCompleteTextView mvTrainNumber;
    @BindView(R.id.spinner_class_berth)
    public Spinner mSpClassTrain;

    @BindView(R.id.id_radio_gn)
    public RadioButton mvGeneralButton;
    @BindView(R.id.id_radio_hp)
    public RadioButton mvHandicapped;
    @BindView(R.id.id_radio_ck)
    public RadioButton mvTatkal;
    @BindView(R.id.id_radio_pt)
    public RadioButton mvTatkalPremium;
    @BindView(R.id.id_radio_ld)
    public RadioButton mvLadies;

    @BindView(R.id.txt_mobile_no)
    public EditText mvMobileNumber;
    @BindView(R.id.id_check_preferred_coach)
    public CheckBox mCbPreferredCoach;
    @BindView(R.id.txt_prefer_coach)
    public EditText mvPreferredCoachTxt;
    @BindView(R.id.id_check_auto_upgrade)
    public CheckBox mCbAutoUpgrade;

    @BindView(R.id.id_check_book_confirm)
    public CheckBox mCbBookCondition;
    @BindView(R.id.id_radio_none)
    public RadioButton mRbNone;
    @BindView(R.id.id_radio_book_all)
    public RadioButton mRbBookOnSameCoach;
    @BindView(R.id.id_radio_book_at_one)
    public RadioButton mRbBookOneLower;
    @BindView(R.id.id_radio_book_at_two)
    public RadioButton mRbBookTwoLower;

    private Unbinder unbinder;
    AutoCompleteStringAdapter stationAdapter,trainAdapter;

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_irctc, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Loading Soucre and Destination,Boarding
        stationAdapter=new AutoCompleteStringAdapter(container.getContext(),R.layout.activity_ticket_irctc,R.id.lbl_name, TicketConstants.STATION_CONST_LIST);
        mvStationSource.setThreshold(3);
        mvStationDestination.setThreshold(3);
        mvStationSource.setAdapter(stationAdapter);
        mvStationDestination.setAdapter(stationAdapter);
        mvStationBoarding.setAdapter(stationAdapter);

        //Loading Train Details
        trainAdapter=new AutoCompleteStringAdapter(container.getContext(),R.layout.activity_ticket_irctc,R.id.lbl_name,TicketConstants.TRAIN_CONST_LIST);
        mvTrainNumber.setThreshold(3);
        mvTrainNumber.setAdapter(trainAdapter);

        //Loading Class Spinner items
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(container.getContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.train_class));
        mSpClassTrain.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        quotaRadioButtonHandler();
        miscRadioButtonHandler();

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void quotaRadioButtonHandler() {
        mvGeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvTatkal.setChecked(false);
                mvTatkalPremium.setChecked(false);
                mvHandicapped.setChecked(false);
                mvLadies.setChecked(false);
            }
        });
        mvTatkal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvGeneralButton.setChecked(false);
                mvTatkalPremium.setChecked(false);
                mvHandicapped.setChecked(false);
                mvLadies.setChecked(false);
            }
        });

        mvTatkalPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvGeneralButton.setChecked(false);
                mvTatkal.setChecked(false);
                mvHandicapped.setChecked(false);
                mvLadies.setChecked(false);
            }
        });

        mvHandicapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvGeneralButton.setChecked(false);
                mvTatkal.setChecked(false);
                mvTatkalPremium.setChecked(false);
                mvLadies.setChecked(false);
            }
        });

        mvLadies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvGeneralButton.setChecked(false);
                mvTatkal.setChecked(false);
                mvTatkalPremium.setChecked(false);
                mvHandicapped.setChecked(false);
            }
        });

    }

    public void miscRadioButtonHandler(){

        mRbNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbBookOnSameCoach.setChecked(false);
                mRbBookOneLower.setChecked(false);
                mRbBookTwoLower.setChecked(false);
            }
        });
        mRbBookOnSameCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbNone.setChecked(false);
                mRbBookOneLower.setChecked(false);
                mRbBookTwoLower.setChecked(false);
            }
        });
        mRbBookOneLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbNone.setChecked(false);
                mRbBookOnSameCoach.setChecked(false);
                mRbBookTwoLower.setChecked(false);
            }
        });
        mRbBookTwoLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbNone.setChecked(false);
                mRbBookOnSameCoach.setChecked(false);
                mRbBookOneLower.setChecked(false);
            }
        });

    }

}
