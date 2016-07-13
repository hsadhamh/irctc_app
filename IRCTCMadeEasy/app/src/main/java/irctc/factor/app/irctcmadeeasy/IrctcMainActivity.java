package irctc.factor.app.irctcmadeeasy;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import irctc.factor.app.irctcmadeeasy.WebView.LxWebView;

public class IrctcMainActivity extends AppCompatActivity {

    String mJsForTicket = "javascript:\n" +
            "\n" +
            " var ticketDetails = new Array();\n" +
            " var timerID;\n" +
            " var jsonString = '{ \"username\":\"hssbs\", \"password\":\"hssbs1992\", \"source\":\"COIMBATORE JN - CBE\", ' + '\"destination\":\"KSR BENGALURU - SBC\", \"boarding\":\"ERODE JN - ED\", \"date-journey\":\"08-07-2016\", ' + '\"train-no\":\"12678\", \"class\":\"2S\", \"Quota\":\"TATKAL\", ' + '\"child-passenger-info\":[{\"name\":\"Hussain\", \"age\":\"2\", \"gender\":\"M\"}],' + '\"passenger-info\":[ ' + '{ \"name\":\"Sadham Hussain H\", \"age\":\"21\", \"gender\":\"M\", \"berth\":\"UB\", \"nationality\":\"indian\", ' + '\"ID-card\":\"\", \"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" }, ' + '{ \"name\":\"Sadham Hussain H\", \"age\":\"25\", \"gender\":\"M\", \"berth\":\"LB\", \"nationality\":\"indian\", \"ID-card\":\"\", ' + '\"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" }, ' + '{ \"name\":\"Sadham Hussain H\", \"age\":\"25\", \"gender\":\"M\", \"berth\":\"LB\", \"nationality\":\"indian\", ' + '\"ID-card\":\"\", \"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" } ], ' + '\"Auto-Upgrade\":\"false\", \"book-confirm\":\"false\", \"book-id-cond\":\"2\", \"preferred-coach\":\"false\", \"coachID\":\"S7\", ' + '\"mobile\":\"9500454034\", \"payment-mode\":\"CREDIT_CARD\", \"payment-mode-id\":\"21\", \"card-no-value\":\"5241465278458104\", \"card-type\":\"MC\", ' + '\"expiry-mon\":\"02\", \"expiry-year\":\"2018\", \"Card-CVV\":\"374\", \"name-card\":\"Sadham Hussain H\" } ';\n" +
            " var timercounter = 0;\n" +
            "\n" +
            " function isUserLoggedIn() {\n" +
            "     var elements = $(\"a[title='Log Out']\").get();\n" +
            "     if (elements != null && elements.length > 0) {\n" +
            "         return true;\n" +
            "     }\n" +
            "     return false;\n" +
            " }\n" +
            "\n" +
            " function isLoginPage() {\n" +
            "     var elements = $(\"form[id='loginFormId']\").get();\n" +
            "     if (elements != null && elements.length > 0) {\n" +
            "         return true;\n" +
            "     }\n" +
            "     return false;\n" +
            " }\n" +
            "\n" +
            "\n" +
            " function loginToForm() {\n" +
            "     if (ticketDetails['username']) {\n" +
            "         $(\"input[id='usernameId']\").val(ticketDetails['username']);\n" +
            "         $(\"input[name='j_password']\").val(ticketDetails['password']);\n" +
            "        \n" +
            "         $(\"input[name='j_captcha']\").click(\n" +
            "         \tfunction(e)\n" +
            "         \t{ \n" +
            "         \t\tconsole.log('Click event done');\n" +
            "         \t\t$(this).focus(); \n" +
            "         \t});\n" +
            "\n" +
            "         $(\"a[id=refresh]\").click(function(e){ $(\"input[name='j_captcha']\").trigger('click'); });\n" +
            "         $(\"a[id=refresh]\").trigger('click');\n" +
            "     }\n" +
            " }\n" +
            "\n" +
            "\n" +
            " function doAutomation() {\n" +
            "     ticketDetails = JSON.parse(jsonString);\n" +
            "     if (!isUserLoggedIn()) {\n" +
            "         console.log('debug user logged');\n" +
            "         if (isLoginPage()) {\n" +
            "             loginToForm();\n" +
            "             $(\"img[id='cimage']\").width('125%');\n" +
            "         } else {\n" +
            "             window.location.href = \"https://www.irctc.co.in/eticketing/loginHome.jsf\";\n" +
            "         }\n" +
            "     } \n" +
            " }\n" +
            "\n" +
            " doAutomation();";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irctc_main);

        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        //  http://stackoverflow.com/questions/21552912/android-web-view-inject-local-javascript-file-to-remote-webpage
        assert webView != null;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(getApplicationContext(), "Load Finsihsed", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 19) {
                    webView.requestFocus(View.FOCUS_DOWN);
                    view.evaluateJavascript(mJsForTicket, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Toast.makeText(getApplicationContext(), "on receive - eval script.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //webView.setInitialScale(1);

                    //InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //mgr.showSoftInput(webView, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    view.loadUrl(mJsForTicket);
                }
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl("http://www.irctc.co.in/");

    }
}
