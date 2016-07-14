package irctc.factor.app.irctcmadeeasy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by hassanhussain on 7/7/2016.
 */
public class FactorSplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
                TicketConstants.InitializeDatabase(getApplicationContext());
                TicketConstants.InitializeJavaScript(getApplicationContext());
                Thread.sleep(SPLASH_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            Intent i = new Intent(FactorSplashScreen.this, DummyActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }

    }
}
