package irctc.factor.app.irctcmadeeasy.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.AutoCompleteStringAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.StationAutoCompleteAdapter;
import irctc.factor.app.irctcmadeeasy.Events.GetMeJsonValues;
import irctc.factor.app.irctcmadeeasy.Interfaces.IGetValue;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;
import irctc.factor.app.irctcmadeeasy.View.ShowHidePasswordEditText;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public final class TrainDetailsFragment extends Fragment  {

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
    public android.widget.EditText mvDateJourney;


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

    @BindView(R.id.id_radio_yes_insurance)
    public RadioButton mRbOptInsurance;
    @BindView(R.id.id_radio_no_insurance)
    public RadioButton mRbOptOutInsurance;

    private Unbinder unbinder;
    AutoCompleteStringAdapter trainAdapter;
    StationAutoCompleteAdapter stationAdapter, stationAdapter1, stationAdapter2;

    private int mYear, mMonth, mDay;
    IGetValue mCallback;

    public static final String LOGINPREFERENCES = "LoginPrefs";
    public static final String USERNAME = "usernameKey";
    public static final String PASSWORD = "passwordKey";
    public static final String CHECKBOXFLAG="loginFlag";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TicketJson mPassedJson = null;
    public TicketJson getPassedJson() { return mPassedJson; }
    public void setPassedJson(TicketJson mPassedJson) { this.mPassedJson = mPassedJson; }

    public static TrainDetailsFragment newInstance() { return new TrainDetailsFragment(); }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (IGetValue) context;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_irctc, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Loading Soucre and Destination,Boarding
        stationAdapter = new StationAutoCompleteAdapter(container.getContext(), TicketConstants.STATION_CONST_LIST);
        stationAdapter1 = new StationAutoCompleteAdapter(container.getContext(), TicketConstants.STATION_CONST_LIST);
        stationAdapter2 = new StationAutoCompleteAdapter(container.getContext(), TicketConstants.STATION_CONST_LIST);

        mvStationSource.setAdapter(stationAdapter);
        mvStationDestination.setAdapter(stationAdapter1);
        mvStationBoarding.setAdapter(stationAdapter2);

        mvStationSource.setThreshold(3);
        mvStationDestination.setThreshold(3);
        mvStationBoarding.setThreshold(3);

        //Loading Train Details
        trainAdapter = new AutoCompleteStringAdapter(container.getContext(),
                R.layout.activity_ticket_irctc, R.id.lbl_name, TicketConstants.TRAIN_CONST_LIST);
        mvTrainNumber.setThreshold(3);
        mvTrainNumber.setAdapter(trainAdapter);

        //Loading Class Spinner items
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.train_class));
        mSpClassTrain.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        quotaRadioButtonHandler();
        miscRadioButtonHandler();
        ShowDatePicker(container);

        //Setting value in Username and Password Field
        pref = getContext().getSharedPreferences(LOGINPREFERENCES,1);
        if(pref!=null) {
            mvUserName.setText(pref.getString(USERNAME, ""));
            mvPassword.setText(pref.getString(PASSWORD, ""));
            if(pref.getString(CHECKBOXFLAG,"").equals("1"))
            {
                mvCbSaveLogin.setChecked(true);
                //Disabling the textboxes
                mvUserName.setEnabled(false);
                mvPassword.setEnabled(false);
                mvUserName.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        view.findViewById(R.id.page_train_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.page_train_details, new BookingPaymentFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        if(mPassedJson != null) { LoadValue(); }

        return view;
    }

    @Override
    public void onDestroyView() {
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

    @OnCheckedChanged({R.id.id_radio_yes_insurance, R.id.id_radio_no_insurance})
    public void OnSelectInsurance(RadioButton btn, boolean isChecked){
        if(isChecked){
            int btnRes = btn.getId();
            mRbOptInsurance.setChecked(btnRes == R.id.id_radio_yes_insurance);
            mRbOptOutInsurance.setChecked(btnRes == R.id.id_radio_no_insurance);
        }
    }

    public void ShowDatePicker(final ViewGroup container) {


        mvDateJourney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH) ;
                    mDay = c.get(Calendar.DAY_OF_MONTH) ;
                    DatePickerDialog datePickerDialog = new DatePickerDialog(container.getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(android.widget.DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    mvDateJourney.setText((dayOfMonth< 10 ? ("0" + (dayOfMonth)) : (dayOfMonth) ) + "-" + (monthOfYear +1 < 10 ? ("0" + (monthOfYear+1)) : (monthOfYear+1)) + "-" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    datePickerDialog.show();
                }

                return true;
            }

        });



    }

    public void miscRadioButtonHandler() {

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

    @Subscribe
    public void saveTrainInfo(GetMeJsonValues e) {
        mCallback.getTicketJsonValue(GetJsonObjectFilled());
    }

    public TicketJson GetJsonObjectFilled() {
        TicketJson oJsonTicket = new TicketJson();

        oJsonTicket.setUsername(mvUserName.getText() != null ? mvUserName.getText().toString() : "");
        oJsonTicket.setPassword(mvPassword.getText() != null ? mvPassword.getText().toString() : "");
        oJsonTicket.setSource(mvStationSource.getText() != null ? mvStationSource.getText().toString() : "");
        oJsonTicket.setDestination(mvStationDestination.getText().toString());
        oJsonTicket.setBoarding(mvStationBoarding.getText().toString());
        oJsonTicket.setDatejourney(mvDateJourney.getText().toString());

        String sTrain  = mvTrainNumber.getText().toString();
        int nIndex = sTrain.indexOf(':');
        if(nIndex > 0)
            sTrain = sTrain.substring(0, nIndex).trim();
        oJsonTicket.setTrainno(sTrain);

        String[] ClassXml = getContext().getResources().getStringArray(R.array.train_class);
        int pos = mSpClassTrain.getSelectedItemPosition();
        oJsonTicket.setBclass(ClassXml[pos]);

        String sQuota = mvTatkalPremium.isChecked() ? "PREMIUM TATKAL" : mvTatkal.isChecked() ? "TATKAL" :
                mvHandicapped.isChecked() ? "PHYSICALLY HANDICAPPED" : mvLadies.isChecked() ? "LADIES" : "GENERAL";
        oJsonTicket.setQuota(sQuota);

        if(mCbPreferredCoach.isChecked()) {
            oJsonTicket.setPrefercoach(true);
            oJsonTicket.setCoachid(mvPreferredCoachTxt.getText().toString());
        }
        else {
            oJsonTicket.setPrefercoach(false);
            oJsonTicket.setCoachid("");
        }

        if(mCbAutoUpgrade.isChecked())
            oJsonTicket.setAutoupgrade(true);
        else
            oJsonTicket.setAutoupgrade(false);

        if(mCbBookCondition.isChecked()){
            oJsonTicket.setBookconfirm(true);
            int nCondition = mRbNone.isChecked()? 0 : mRbBookOneLower.isChecked()? 1 : mRbBookOnSameCoach.isChecked()? 2 : mRbBookTwoLower.isChecked()? 3 : 0;
            oJsonTicket.setBookidcond("" + nCondition);
        }
        else{
            oJsonTicket.setBookconfirm(false);
            oJsonTicket.setBookidcond("0");
        }

        if(mRbOptOutInsurance.isChecked())
            oJsonTicket.setOptinsurance(false);
        else if(mRbOptInsurance.isChecked())
            oJsonTicket.setOptinsurance(true);

        oJsonTicket.setMobile(mvMobileNumber.getText().toString());
        return oJsonTicket;
    }

    @OnClick(R.id.id_check_preferred_coach)
    public void preferredCheck() {
        if (mCbPreferredCoach.isChecked()) {
            mvPreferredCoachTxt.setEnabled(true);
        }
        else {
            mvPreferredCoachTxt.setEnabled(false);
            mvPreferredCoachTxt.setText("");
        }
    }

    @OnClick(R.id.check_box_save_login)
    public void onChecked() {
        if (mvCbSaveLogin.isChecked()) {
            editor = pref.edit();
            editor.putString(USERNAME, mvUserName.getText().toString());
            editor.putString(PASSWORD, mvPassword.getText().toString());
            editor.putString(CHECKBOXFLAG,"1");
            editor.commit();
            //Disabling the textboxes
            mvUserName.setEnabled(false);
            mvPassword.setEnabled(false);
            mvUserName.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            editor = pref.edit();
            editor.clear();
            editor.commit();
            //Enabling the Textboxes
            mvUserName.setEnabled(true);
            mvPassword.setEnabled(true);
            mvUserName.setBackgroundColor(Color.TRANSPARENT);
            mvPassword.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void LoadValue(){
        TicketJson train = mPassedJson;

        mvUserName.setText(train.getUsername());
        mvPassword.setText(train.getPassword());

        mvStationSource.setText(train.getSource());
        mvStationDestination.setText(train.getDestination());
        mvStationBoarding.setText(train.getBoarding());
        mvDateJourney.setText(train.getDatejourney());
        mvTrainNumber.setText(train.getTrainno());
        ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) mSpClassTrain.getAdapter();
        mSpClassTrain.setSelection(array_spinner.getPosition(train.getBclass()));

        if (train.getQuota().equals("GENERAL")) {
            mvGeneralButton.setChecked(true);
            mvTatkal.setChecked(false);
        } else if (train.getQuota().equals("TATKAL"))
            mvTatkal.setChecked(true);
        else if (train.getQuota().equals("PREMIUM TATKAL")) {
            mvTatkalPremium.setChecked(true);
            mvTatkal.setChecked(false);
        } else if (train.getQuota().equals("PHYSICALLY HANDICAPPED")) {
            mvHandicapped.setChecked(true);
            mvTatkal.setChecked(false);
        } else {
            mvLadies.setChecked(true);
            mvTatkal.setChecked(false);
        }
        mvMobileNumber.setText(train.getMobile());
        mCbPreferredCoach.setChecked(train.isPrefercoach());
        if (train.isPrefercoach() && !(train.getCoachid() == null
                || train.getCoachid().equals(" "))) {
            mvPreferredCoachTxt.setText(train.getCoachid());
        }

        if (train.isAutoupgrade())
            mCbAutoUpgrade.setChecked(true);

        if (train.isBookconfirm())
            mCbBookCondition.setChecked(true);

        if(train.getOptinsurance())
            mRbOptInsurance.setChecked(true);
        else
            mRbOptOutInsurance.setChecked(true);

        switch(train.getBookidcond()) {
            case "0":
                mRbNone.setChecked(true);
                break;
            case "1":
                mRbBookOnSameCoach.setChecked(true);
                mRbNone.setChecked(false);
                break;
            case "2":
                mRbBookOneLower.setChecked(true);
                mRbNone.setChecked(false);
                break;
            default:
                mRbBookTwoLower.setChecked(true);
                mRbNone.setChecked(false);
        }
    }
}
