package irctc.factor.app.irctcmadeeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.dao.query.Query;
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

    private int mnPassengerID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_row_passenger);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null){
            mnPassengerID = intent.getIntExtra("passenger", 0);
        }

        setUiListeners();
        setAdapters();

        mDaoMaster = new DaoMaster(TicketConstants.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mPassengerInfo = mDaoSession.getPassengerInfoDao();

        if(mnPassengerID > 0)
            GetPassengerInfo();
    }

    public void setAdapters(){
        ArrayAdapter<String> spBerthAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_berth));
        spBerthAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mBerthOption.setAdapter(spBerthAdapter);

        ArrayAdapter<String> spFoodAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_food));
        spFoodAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mFoodOption.setAdapter(spFoodAdapter);
    }

    public void setUiListeners(){
        mPassengerAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sAge = s.toString();
                if(!sAge.isEmpty()) {
                    int age = Integer.parseInt(sAge);
                    mCbChild.setEnabled(age > 0 && age < 6);
                    mCbChild.setChecked(age > 0 && age < 6);
                    mCbSenior.setEnabled(age >= 60);
                    mCbSenior.setChecked(age >= 60);
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
    }

    @OnCheckedChanged(R.id.check_box_child)
    public void onChildChecked(){
        mBerthOption.setEnabled(!mCbChild.isChecked());
        mFoodOption.setEnabled(!mCbChild.isChecked());
        mCbChild.setEnabled(mCbChild.isChecked());
        mCbSenior.setEnabled(!mCbChild.isChecked());
        mCbBedRoll.setEnabled(!mCbChild.isChecked());
        mCbOptBerth.setEnabled(!mCbChild.isChecked());
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
        if(isValidInformationGiven()) {
        /*  SAVE INFORMATION TO DB  */
            PassengerInfo passenger = new PassengerInfo();
            passenger.setAadharCardNo(1234);
            passenger.setAge(Integer.parseInt(mPassengerAge.getText().toString()));
            passenger.setBerth(mBerthOption.getSelectedItem().toString());
            passenger.setChild(mCbChild.isChecked() ? "CHILD" : mCbSenior.isChecked() ? "SENIOR" : "ADULT");
            passenger.setFood(mFoodOption.getSelectedItem().toString());
            passenger.setGender(mRbMale.isChecked() ? "MALE" : "FEMALE");
            passenger.setName(mPassengerName.getText().toString());
            passenger.setNationality("Indian");
            passenger.setTransactionId(256);

            if (mnPassengerID > 0)
                passenger.setId((long) mnPassengerID);

            if (mnPassengerID > 0)
                mPassengerInfo.update(passenger);
            else
                mPassengerInfo.insert(passenger);

            Intent intent = new Intent();
            intent.putExtra("dummy_value", "value_here");
            setResult(EventConstants.EVENT_RESP_ADD_PASSENGER_OK, intent);
            finish();
        }
    }

    public void GetPassengerInfo(){
        PassengerInfo pass = mPassengerInfo.load((long) mnPassengerID);
        mPassengerName.setText(pass.getName());
        mPassengerAge.setText(""+pass.getAge());

        if(pass.getGender().equals("MALE"))
            mRbMale.setChecked(true);
        else
            mRbFemale.setChecked(true);

        if(pass.getChild().equals("CHILD"))
            mCbChild.setChecked(true);
        else if(pass.getChild().equals("SENIOR"))
            mCbSenior.setChecked(true);

        if(pass.getFood().equals("NonVeg"))
            mFoodOption.setSelection(2);
        else
            mFoodOption.setSelection(1);
        mBerthOption.setSelection(getBerthIndex(pass.getBerth()));
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

    public boolean isValidInformationGiven(){
        String sAge = mPassengerAge.getText().toString();
        if(sAge.isEmpty()
                || Integer.parseInt(sAge) > 135
                || Integer.parseInt(sAge) == 0) {
            new MaterialDialog
                    .Builder(this)
                    .content("Given age is not valid.")
                    .positiveText("OK")
                    .show();
            return false;
        }

        String sName = mPassengerName.getText().toString();
        if(sName.isEmpty() || sName.length() <= 2) {
            new MaterialDialog
                    .Builder(this)
                    .content("Given name is not valid.")
                    .positiveText("OK")
                    .show();
            return false;
        }
        return true;
    }
}
