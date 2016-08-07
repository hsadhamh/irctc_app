package irctc.factor.app.irctcmadeeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Button;

import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Activity.AddPassengerActivity;
import irctc.factor.app.irctcmadeeasy.Activity.BookTicketsNowActivity;
import irctc.factor.app.irctcmadeeasy.Activity.TicketsListActivity;
import irctc.factor.app.irctcmadeeasy.Activity.IrctcMainActivity;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.Json.flJsonParser;

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
    @BindView(R.id.id_forms)
    Button forms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dummy);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_forms)
    public void Allforms(){
        Intent i=new Intent(DummyActivity.this,TicketsListActivity.class);
        startActivity(i);

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

    @OnClick(R.id.id_btn4)
    public void showJsonCreate() {
        String json  = "{\n" +
                "   \"username\":\"hssbs\",\n" +
                "   \"password\":\"hssbs1992\",\n" +
                "   \"source\":\"COIMBATORE JN - CBE\",\n" +
                "   \"destination\":\"KSR BENGALURU - SBC\",\n" +
                "   \"boarding\":\"ERODE JN - ED\",\n" +
                "   \"date-journey\":\"08-07-2016\",\n" +
                "   \"train-no\":\"12678\",\n" +
                "   \"class\":\"2S\",\n" +
                "   \"Quota\":\"TATKAL\",\n" +

                "   \"child-passenger-info\":[\n" +
                "      {\n" +
                "         \"name\":\"Hussain\",\n" +
                "         \"age\":\"2\",\n" +
                "         \"gender\":\"M\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\":\"Hussain\",\n" +
                "         \"age\":\"2\",\n" +
                "         \"gender\":\"M\"\n" +
                "      }\n" +
                "   ],\n" +

                "   \"passenger-info\":[\n" +
                "      {\n" +
                "         \"name\":\"Sadham Hussain H\",\n" +
                "         \"age\":\"21\",\n" +
                "         \"gender\":\"M\",\n" +
                "         \"berth\":\"UB\",\n" +
                "         \"nationality\":\"indian\",\n" +
                "         \"ID-card\":\"\",\n" +
                "         \"ID-Card-No\":\"\",\n" +
                "         \"type\":\"adult\",\n" +
                "         \"senior\":\"false\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\":\"Sadham Hussain H\",\n" +
                "         \"age\":\"25\",\n" +
                "         \"gender\":\"M\",\n" +
                "         \"berth\":\"LB\",\n" +
                "         \"nationality\":\"indian\",\n" +
                "         \"ID-card\":\"\",\n" +
                "         \"ID-Card-No\":\"\",\n" +
                "         \"type\":\"adult\",\n" +
                "         \"senior\":\"false\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\":\"Sadham Hussain H\",\n" +
                "         \"age\":\"25\",\n" +
                "         \"gender\":\"M\",\n" +
                "         \"berth\":\"LB\",\n" +
                "         \"nationality\":\"indian\",\n" +
                "         \"ID-card\":\"\",\n" +
                "         \"ID-Card-No\":\"\",\n" +
                "         \"type\":\"adult\",\n" +
                "         \"senior\":\"false\"\n" +
                "      }\n" +
                "   ],\n" +

                "   \"Auto-Upgrade\":\"false\",\n" +
                "   \"book-confirm\":\"false\",\n" +
                "   \"book-id-cond\":\"2\",\n" +
                "   \"preferred-coach\":\"false\",\n" +
                "   \"coachID\":\"S7\",\n" +
                "   \"mobile\":\"9500454034\",\n" +
                "   \"payment-mode\":\"CREDIT_CARD\",\n" +
                "   \"payment-mode-id\":\"21\",\n" +
                "   \"card-no-value\":\"5241465278458104\",\n" +
                "   \"card-type\":\"MC\",\n" +
                "   \"expiry-mon\":\"02\",\n" +
                "   \"expiry-year\":\"2018\",\n" +
                "   \"Card-CVV\":\"374\",\n" +
                "   \"name-card\":\"Sadham Hussain H\"\n" +
                "}";
        try {
            TicketJson jsonTicket = flJsonParser.getTicketDetailObject(json);
            new MaterialDialog
                    .Builder(this)
                    .content("Convert successful - " + jsonTicket.getTrainNumber() + " : " + jsonTicket.getBoardingStation())
                    .title("From : " + jsonTicket.getSrcStation() + " - To: " + jsonTicket.getDestStation())
                    .show();

            jsonTicket.setAutoUpgrade(true);
            String sConvertJson = flJsonParser.getTicketDetailString(jsonTicket);
            new MaterialDialog
                    .Builder(this)
                    .content("Convert successful - " + sConvertJson)
                    .title("Convert")
                    .show();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

}
