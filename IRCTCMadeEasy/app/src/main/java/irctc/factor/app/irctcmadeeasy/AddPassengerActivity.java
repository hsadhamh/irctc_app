package irctc.factor.app.irctcmadeeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import irctc.factor.app.irctcmadeeasy.Events.EventConstants;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;
import irctc.factor.app.irctcmadeeasy.database.DaoSession;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

/**
 * Created by hassanhussain on 7/9/2016.
 */
public class AddPassengerActivity extends AppCompatActivity{

    @BindView(R.id.txt_passenger_name)
    public EditText mPassengerName;
    @BindView(R.id.txt_passenger_age)
    public EditText mPassengerAge;
    @BindView(R.id.id_radio_male)
    public RadioButton mRbMale;
    @BindView(R.id.id_radio_female)
    public RadioButton mRbFemale;
    @BindView(R.id.spinner_berth)
    public Spinner mBerthOption;
    @BindView(R.id.spinner_food)
    public Spinner mFoodOption;
    @BindView(R.id.check_box_child)
    public CheckBox mCbChild;
    @BindView(R.id.check_box_senior)
    public CheckBox mCbSenior;
    @BindView(R.id.check_box_bed_roll)
    public CheckBox mCbBedRoll;
    @BindView(R.id.check_box_opt_berth)
    public CheckBox mCbOptBerth;

    @BindView(R.id.id_holder_berth)
    public LinearLayout mHolderBerth;
    @BindView(R.id.id_holder_food)
    public LinearLayout mHolderFood;

    @BindView(R.id.fab_save_passenger)
    public FloatingActionButton mSaveButton;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private PassengerInfoDao mPassengerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_row_passenger);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        mPassengerAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sAge = s.toString();
                if(!sAge.isEmpty()) {
                    int age = Integer.parseInt(sAge);
                    if (age > 0 && age < 6) {
                        mCbChild.setEnabled(true);
                        mCbChild.setChecked(true);
                        mCbSenior.setEnabled(false);
                        mCbSenior.setChecked(false);
                    } else if (age >= 60) {
                        mCbChild.setEnabled(false);
                        mCbChild.setChecked(false);
                        mCbSenior.setEnabled(true);
                        mCbSenior.setChecked(true);
                    }
                    else {
                        mCbChild.setEnabled(false);
                        mCbChild.setChecked(false);
                        mCbSenior.setEnabled(false);
                        mCbSenior.setChecked(false);
                    }
                }
                else {
                    mCbChild.setEnabled(false);
                    mCbChild.setChecked(false);
                    mCbSenior.setEnabled(false);
                    mCbSenior.setChecked(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        ArrayAdapter<String> spBerthAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_berth));
        spBerthAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mBerthOption.setAdapter(spBerthAdapter);

        ArrayAdapter<String> spFoodAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_food));
        spFoodAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mFoodOption.setAdapter(spFoodAdapter);

        mDaoMaster = new DaoMaster(TicketConstants.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mPassengerInfo = mDaoSession.getPassengerInfoDao();
    }

    @OnCheckedChanged(R.id.check_box_child)
    public void onChildChecked(){
        if(mCbChild.isChecked()) {
            mBerthOption.setEnabled(false);
            mFoodOption.setEnabled(false);
            mCbChild.setEnabled(true);
            mCbSenior.setEnabled(false);
            mCbBedRoll.setEnabled(false);
            mCbOptBerth.setEnabled(false);
        }
        else {
            mBerthOption.setEnabled(true);
            mFoodOption.setEnabled(true);
            mCbChild.setEnabled(false);
            mCbSenior.setEnabled(true);
            mCbBedRoll.setEnabled(true);
            mCbOptBerth.setEnabled(true);
        }
    }

    @OnCheckedChanged({R.id.id_radio_male, R.id.id_radio_female})
    public void onRadioClick(RadioButton btn, boolean isChecked){
        if(isChecked){
            if(btn.getId() == R.id.id_radio_male){
                mRbFemale.setChecked(false);
            }
            else if(btn.getId() == R.id.id_radio_female){
                mRbMale.setChecked(false);
            }
        }
    }

    @OnClick(R.id.fab_save_passenger)
    public void SavePassengerInfo(){
        /*    VALIDATE INFORMATION    */

        /*  SAVE INFORMATION TO DB  */
        PassengerInfo passenger = new PassengerInfo();
        passenger.setAadharCardNo(1234);
        passenger.setAge(Integer.parseInt(mPassengerAge.getText().toString()));
        passenger.setBerth(mBerthOption.getSelectedItem().toString());
        passenger.setChild(mCbChild.isChecked()? "CHILD" : mCbSenior.isChecked()? "SENIOR" : "ADULT");
        passenger.setFood(mFoodOption.getSelectedItem().toString());
        passenger.setGender(mRbMale.isChecked()? "MALE" : "FEMALE");
        passenger.setName(mPassengerName.getText().toString());
        passenger.setNationality("Indian");
        passenger.setTransactionId(256);
        Toast.makeText(getApplicationContext(), "add to Db info", Toast.LENGTH_SHORT).show();
        mPassengerInfo.insert(passenger);
        Toast.makeText(getApplicationContext(), "added to Db info", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("dummy_value","value_here");
        setResult(EventConstants.EVENT_RESP_ADD_PASSENGER_OK, intent);
        finish();

    }

}
