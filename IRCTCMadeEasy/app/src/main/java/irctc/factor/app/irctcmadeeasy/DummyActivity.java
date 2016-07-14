package irctc.factor.app.irctcmadeeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hassanhussain on 7/9/2016.
 */
public class DummyActivity extends AppCompatActivity {

    @BindView(R.id.id_btn1)
    Button btn1;
    @BindView(R.id.id_btn2)
    Button btn2;
    @BindView(R.id.id_btn3)
    Button btn3;
    @BindView(R.id.id_btn4)
    Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dummy);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_btn1)
    public void showIrctcBook() {
        Intent i = new Intent(DummyActivity.this, BookTicketsNowActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.id_btn2)
    public void showIrctcSite() {
        Intent i = new Intent(DummyActivity.this, IrctcMainActivity.class);
        i.putExtra("JSON_String",  "{ \"username\":\"hssbs\", \"password\":\"hssbs1992\", \"source\":\"COIMBATORE JN - CBE\", ' +\n" +
                "    '\"destination\":\"KSR BENGALURU - SBC\", \"boarding\":\"ERODE JN - ED\", \"date-journey\":\"08-07-2016\", ' +\n" +
                "    '\"train-no\":\"12678\", \"class\":\"2S\", \"Quota\":\"TATKAL\", ' +\n" +
                "    '\"child-passenger-info\":[{\"name\":\"Hussain\", \"age\":\"2\", \"gender\":\"M\"}],' +\n" +
                "    '\"passenger-info\":[ ' +\n" +
                "    '{ \"name\":\"Sadham Hussain H\", \"age\":\"21\", \"gender\":\"M\", \"berth\":\"UB\", \"nationality\":\"indian\", ' +\n" +
                "    '\"ID-card\":\"\", \"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" }, ' +\n" +
                "    '{ \"name\":\"Sadham Hussain H\", \"age\":\"25\", \"gender\":\"M\", \"berth\":\"LB\", \"nationality\":\"indian\", \"ID-card\":\"\", ' +\n" +
                "    '\"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" }, ' +\n" +
                "    '{ \"name\":\"Sadham Hussain H\", \"age\":\"25\", \"gender\":\"M\", \"berth\":\"LB\", \"nationality\":\"indian\", ' +\n" +
                "    '\"ID-card\":\"\", \"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" } ], ' +\n" +
                "    '\"Auto-Upgrade\":\"false\", \"book-confirm\":\"false\", \"book-id-cond\":\"2\", \"preferred-coach\":\"false\", \"coachID\":\"S7\", ' +\n" +
                "    '\"mobile\":\"9500454034\", \"payment-mode\":\"CREDIT_CARD\", \"payment-mode-id\":\"21\", \"card-no-value\":\"5241465278458104\", \"card-type\":\"MC\", ' +\n" +
                "    '\"expiry-mon\":\"02\", \"expiry-year\":\"2018\", \"Card-CVV\":\"374\", \"name-card\":\"Sadham Hussain H\" } ");
        startActivity(i);
    }

    @OnClick(R.id.id_btn3)
    public void showAddPass() {
        Intent i = new Intent(DummyActivity.this, AddPassengerActivity.class);
        startActivity(i);
    }

}
