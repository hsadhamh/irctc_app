package irctc.factor.app.irctcmadeeasy.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Adapters.AutoCompleteStringAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.PassengerCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.TicketDetailsCursorAdapter;
import irctc.factor.app.irctcmadeeasy.AddPassengerActivity;
import irctc.factor.app.irctcmadeeasy.Events.AddFormsEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.View.ShowHidePasswordEditText;
import irctc.factor.app.irctcmadeeasy.TicketConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.TicketDetails;
import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;

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
    AutoCompleteStringAdapter stationAdapter, trainAdapter;

    private int mYear, mMonth, mDay;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private TicketDetailsDao ticketDetails;
    private int mnTrainID = 0;

    TicketDetailsCursorAdapter mAdapter = null;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //mListPassengers.setLayoutManager(linearLayoutManager);


        // Toast.makeText(getContext().getApplicationContext(),Integer.toString(mnTrainID),Toast.LENGTH_SHORT).show();


        mDaoMaster = new DaoMaster(TicketConstants.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        ticketDetails = mDaoSession.getTicketDetailsDao();

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mnTrainID = intent.getIntExtra("form", 0);
        }
        if (mnTrainID > 0)
            GetTrainInfo(mnTrainID);
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

        mvDateCalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH) + 1;


                DatePickerDialog datePickerDialog = new DatePickerDialog(container.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mvDateJourney.setText(dayOfMonth + "-" + (monthOfYear < 10 ? ("0" + monthOfYear) : (monthOfYear)) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
    public void onEvent(AddFormsEvent e) {

        saveTrainInfo();

    }

    public void saveTrainInfo() {
        TicketDetails ticket = new TicketDetails();
        ticket.setSource(mvStationSource.getText().toString());
        ticket.setDestination(mvStationDestination.getText().toString());
        ticket.setBoarding(mvStationBoarding.getText().toString());
        ticket.setJourneyDate(mvDateJourney.getText().toString());
        ticket.setTrainno(mvTrainNumber.getText().toString());
        ticket.setIrctcClass(mSpClassTrain.getSelectedItem().toString());
        ticket.setQuota(mvGeneralButton.isChecked() ? "GENERAL" : mvTatkalPremium.isChecked() ? "PREMIUM TATKAL" : mvTatkal.isChecked() ? "TATKAL" : mvHandicapped.isChecked() ? "PHYSICALLY HANDICAPPED" : "LADIES");
        ticket.setMobileNumber(mvMobileNumber.getText().toString());
        ticket.setCoach(mCbPreferredCoach.isChecked() ? mvPreferredCoachTxt.getText().toString() : " ");
        ticket.setAutoUpgrade(mCbAutoUpgrade.isChecked() ? "true" : "false");
        ticket.setBookOnConfirm(mCbBookCondition.isChecked() ? "true" : "false");
        ticket.setConditionsOnBook(mRbNone.isChecked() ? "0" : mRbBookOnSameCoach.isChecked() ? "1" : mRbBookOneLower.isChecked() ? "2" : "3");


        //For Storing JSON
        //String tNumber=mvTrainNumber.getText().toString().substring(0, tNumber.indexOf(':')).trim();


        if (mnTrainID > 0)
            ticket.setId((long) mnTrainID);
        if (mnTrainID > 0)
            ticketDetails.update(ticket);
        else
            ticketDetails.insert(ticket);


    }

    public void createJsonValues() {

    }

    public void validation() {

    }

    public void GetTrainInfo(int trainId) {
        TicketDetails train = ticketDetails.load((long) trainId);
        mvStationSource.setText(train.getSource());
        mvStationDestination.setText(train.getDestination());
        mvStationBoarding.setText(train.getBoarding());
        mvDateJourney.setText(train.getJourneyDate());
        mvTrainNumber.setText(train.getTrainno());
        ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) mSpClassTrain.getAdapter();
        mSpClassTrain.setSelection(array_spinner.getPosition(train.getIrctcClass()));

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
        if (!(train.getCoach() == null || train.getCoach() == " ")) {
            mCbPreferredCoach.setChecked(true);
            mvPreferredCoachTxt.setText(train.getCoach());
        }

        if (train.getAutoUpgrade().equals("true"))
            mCbAutoUpgrade.setChecked(true);

        if (train.getBookOnConfirm().equals("true"))
            mCbBookCondition.setChecked(true);


        if (train.getConditionsOnBook().equals("0"))
            mRbNone.setChecked(true);
        else if (train.getConditionsOnBook().equals("1")) {
            mRbBookOnSameCoach.setChecked(true);
            mRbNone.setChecked(false);
        } else if (train.getConditionsOnBook().equals("2")) {
            mRbBookOneLower.setChecked(true);
            mRbNone.setChecked(false);
        } else {
            mRbBookTwoLower.setChecked(true);
            mRbNone.setChecked(false);
        }


    }

    @OnClick(R.id.id_check_preferred_coach)
    public void preferredCheck() {
        if (mCbPreferredCoach.isChecked()) {
            mvPreferredCoachTxt.setEnabled(true);
        }

    }

    public void onListUpdatedEvent() {
        Cursor localCursor;
        localCursor = TicketConstants
                .getReadableDatabase()
                .query(ticketDetails.getTablename(), ticketDetails.getAllColumns(), null, null, null, null, "");
        mAdapter.swapCursor(localCursor);
    }

}
