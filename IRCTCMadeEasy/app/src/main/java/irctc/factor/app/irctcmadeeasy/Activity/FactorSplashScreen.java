package irctc.factor.app.irctcmadeeasy.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;

/**
 * Created by hassanhussain on 7/7/2016.
 */
public class FactorSplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new BackgroundOps().execute();


    }

    /**
     * Async Task
     */
    private class BackgroundOps extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                TicketConstants.Initialize(getApplicationContext());
                TicketConstants.InitializeJavaScript(getApplicationContext());
                TicketConstants.InitializeAdsModule(getApplicationContext());
                Thread.sleep(SPLASH_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            Intent i = new Intent(FactorSplashScreen.this, TicketsListActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }

    }
}
