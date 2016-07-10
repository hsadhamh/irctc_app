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
        startActivity(i);
    }

    @OnClick(R.id.id_btn3)
    public void showAddPass() {
        Intent i = new Intent(DummyActivity.this, AddPassengerActivity.class);
        startActivity(i);
    }

}
