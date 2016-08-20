package irctc.factor.app.irctcmadeeasy.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.AutoCompleteStringAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.TicketDetailsCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Events.GetMeJsonValues;
import irctc.factor.app.irctcmadeeasy.Events.UpdateJsonValues;
import irctc.factor.app.irctcmadeeasy.Interfaces.IGetValue;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.View.ShowHidePasswordEditText;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.TicketDetails;
import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;

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

    private Unbinder unbinder;
    AutoCompleteStringAdapter stationAdapter, trainAdapter;

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
        stationAdapter = new AutoCompleteStringAdapter(container.getContext(), R.layout.activity_ticket_irctc, R.id.lbl_name, TicketConstants.STATION_CONST_LIST);
        mvStationSource.setThreshold(3);
        mvStationDestination.setThreshold(3);
        mvStationSource.setAdapter(stationAdapter);
        mvStationDestination.setAdapter(stationAdapter);
        mvStationBoarding.setAdapter(stationAdapter);

        //Loading Train Details
        trainAdapter = new AutoCompleteStringAdapter(container.getContext(), R.layout.activity_ticket_irctc, R.id.lbl_name, TicketConstants.TRAIN_CONST_LIST);
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
                                    mvDateJourney.setText(dayOfMonth + "-" + (monthOfYear +1 < 10 ? ("0" + (monthOfYear+1)) : (monthOfYear)) + "-" + year);
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

        oJsonTicket.setUserName(mvUserName.getText() != null ? mvUserName.getText().toString() : "");
        oJsonTicket.setPassword(mvPassword.getText() != null ? mvPassword.getText().toString() : "");
        oJsonTicket.setSrcStation(mvStationSource.getText() != null ? mvStationSource.getText().toString() : "");
        oJsonTicket.setDestStation(mvStationDestination.getText().toString());
        oJsonTicket.setBoardingStation(mvStationBoarding.getText().toString());
        oJsonTicket.setDateOfJourney(mvDateJourney.getText().toString());

        String sTrain  = mvTrainNumber.getText().toString();
        int nIndex = sTrain.indexOf(':');
        if(nIndex > 0)
            sTrain = sTrain.substring(0, nIndex).trim();
        oJsonTicket.setTrainNumber(sTrain);

        String[] ClassXml = getContext().getResources().getStringArray(R.array.train_class);
        int pos = mSpClassTrain.getSelectedItemPosition();
        oJsonTicket.setClassSelection(ClassXml[pos]);

        String sQuota = mvTatkalPremium.isChecked() ? "PREMIUM TATKAL" : mvTatkal.isChecked() ? "TATKAL" :
                mvHandicapped.isChecked() ? "PHYSICALLY HANDICAPPED" : mvLadies.isChecked() ? "LADIES" : "GENERAL";
        oJsonTicket.setQuota(sQuota);

        if(mCbPreferredCoach.isChecked()) {
            oJsonTicket.setPreferredCoachSelection(true);
            oJsonTicket.setPreferredCoachID(mvPreferredCoachTxt.getText().toString());
        }
        else {
            oJsonTicket.setPreferredCoachSelection(false);
            oJsonTicket.setPreferredCoachID("");
        }

        if(mCbAutoUpgrade.isChecked())
            oJsonTicket.setAutoUpgrade(true);
        else
            oJsonTicket.setAutoUpgrade(false);

        if(mCbBookCondition.isChecked()){
            oJsonTicket.setBookConfirmTickets(true);
            int nCondition = mRbNone.isChecked()? 0 : mRbBookOneLower.isChecked()? 1 : mRbBookOnSameCoach.isChecked()? 2 : mRbBookTwoLower.isChecked()? 3 : 0;
            oJsonTicket.setBookConfirmTicketOption("" + nCondition);
        }
        else{
            oJsonTicket.setBookConfirmTickets(false);
            oJsonTicket.setBookConfirmTicketOption("0");
        }
        oJsonTicket.setMobileNumber(mvMobileNumber.getText().toString());
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

        mvUserName.setText(train.getUserName());
        mvPassword.setText(train.getPassword());

        mvStationSource.setText(train.getSrcStation());
        mvStationDestination.setText(train.getDestStation());
        mvStationBoarding.setText(train.getBoardingStation());
        mvDateJourney.setText(train.getDateOfJourney());
        mvTrainNumber.setText(train.getTrainNumber());
        ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) mSpClassTrain.getAdapter();
        mSpClassTrain.setSelection(array_spinner.getPosition(train.getClassSelection()));

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
        mvMobileNumber.setText(train.getMobileNumber());
        mCbPreferredCoach.setChecked(train.getPreferredCoachSelection());
        if (train.getPreferredCoachSelection() && !(train.getPreferredCoachID() == null
                || train.getPreferredCoachID().equals(" "))) {
            mvPreferredCoachTxt.setText(train.getPreferredCoachID());
        }

        if (train.getAutoUpgrade())
            mCbAutoUpgrade.setChecked(true);

        if (train.getBookConfirmTickets())
            mCbBookCondition.setChecked(true);

        switch(train.getBookConfirmTicketOption()) {
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
