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

    String mJsForTicket = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irctc_main);

        final LxWebView webView = (LxWebView) findViewById(R.id.webView);

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
                    webView.setInitialScale(1);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(webView, InputMethodManager.SHOW_IMPLICIT);
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
