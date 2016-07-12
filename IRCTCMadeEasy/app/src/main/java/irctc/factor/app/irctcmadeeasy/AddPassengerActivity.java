package irctc.factor.app.irctcmadeeasy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

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
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String sAge = mPassengerAge.getText().toString();
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
        });

        ArrayAdapter<String> spBerthAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_berth));
        spBerthAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mBerthOption.setAdapter(spBerthAdapter);

        ArrayAdapter<String> spFoodAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.passenger_food));
        spFoodAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
        mFoodOption.setAdapter(spFoodAdapter);
    }

    @OnCheckedChanged(R.id.check_box_child)
    public void onChildChecked(){
        if(mCbChild.isChecked()) {
            mHolderBerth.setEnabled(false);
            mHolderFood.setEnabled(false);
            mCbChild.setEnabled(true);
            mCbSenior.setEnabled(false);
            mCbBedRoll.setEnabled(false);
            mCbOptBerth.setEnabled(false);
        }
        else {
            mHolderBerth.setEnabled(true);
            mHolderFood.setEnabled(true);
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

}
