package irctc.factor.app.irctcmadeeasy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.fab_save_passenger)
    public FloatingActionButton mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_row_passenger);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
    }
}
