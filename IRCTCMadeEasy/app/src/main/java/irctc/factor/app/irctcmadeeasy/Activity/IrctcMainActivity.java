package irctc.factor.app.irctcmadeeasy.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.Json.flJsonParser;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.TicketConstants;
import irctc.factor.app.irctcmadeeasy.View.WebView.LxWebView;

public class IrctcMainActivity extends AppCompatActivity {

    String mJsAutoFile = TicketConstants.JavaScriptForAutoFill;

    String mLoginUrlPattern =".*?(www\\.irctc\\.co\\.in\\/eticketing\\/loginHome\\.jsf)";
    String mPassengerListPage = ".*?(www\\.irctc\\.co\\.in\\/eticketing\\/trainbetweenstns\\.jsf)";
    String mPaymentPage = ".*?(www\\.irctc\\.co\\.in\\/eticketing\\/jpInput\\.jsf)";
    String mHomeURL = "http://www.irctc.co.in/eticketing/loginHome.jsf";


    String mJson;

    @BindView(R.id.irctc_web_view_id)
    LxWebView mWebIrctc;
    @BindView(R.id.progress_linear_page)
    ProgressView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irctc_main);
        ButterKnife.bind(this);

        Intent i = getIntent();
        mJson = i.getStringExtra("JSON_String");
        GetJsString(mJson);

        assert mWebIrctc != null;

        mWebIrctc.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/12");
        mWebIrctc.getSettings().setLoadWithOverviewMode(true);
        setChromeClientProgress();
        setWebViewClient();
        mWebIrctc.loadUrl(mHomeURL);
    }

    public boolean isLoginPage(String url){ return isMatch(url, mLoginUrlPattern); }

    public boolean isPassengerListPage(String url){ return isMatch(url, mPassengerListPage); }

    public boolean isPaymentPage(String url){ return isMatch(url, mPaymentPage); }


    public boolean isMatch(String text, String pattern){
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public void setChromeClientProgress(){
        mWebIrctc.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress){
                assert mProgress != null;
                mProgress.setProgress(progress + 0.01f);
                if(progress == 100) {
                    //mProgress.stop();
                    mProgress.setVisibility(View.GONE);
                }
                else if(mProgress.getVisibility() == View.GONE) {
                    //mProgress.start();
                    mProgress.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setWebViewClient() {
        mWebIrctc.setWebViewClient(new WebViewClient() {


            private Map<String, Boolean> loadedUrls = new HashMap<>();

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (!loadedUrls.containsKey(url)) {
                    loadedUrls.put(url, AdBlocker.isAd(url));
                }
                return loadedUrls.get(url) ?
                        AdBlocker.createEmptyResource() : super.shouldInterceptRequest(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT >= 19) {
                    mWebIrctc.requestFocus(View.FOCUS_DOWN);
                    view.evaluateJavascript(mJsAutoFile, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {}
                    });

                    String webUrl = mWebIrctc.getUrl();
                    if(isLoginPage(webUrl) || isPassengerListPage(webUrl) || doShowInputForPaymentPage(webUrl)) {
                        showSoftKeyBoard();
                    }
                } else  {
                    view.loadUrl(mJsAutoFile);
                }
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(),
                        "URL : " + failingUrl +"; ErrorCode : " + errorCode + "; Description : " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showSoftKeyBoard() {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(mWebIrctc, InputMethodManager.SHOW_IMPLICIT);
    }

    public void GetJsString(String sJson){
        String JsPrefix = String.format("%s='%s';", TicketConstants.mJsAutoPrefix, sJson);
        mJsAutoFile = JsPrefix + mJsAutoFile;
        Log.d("GetJS", sJson);
    }

    public boolean doShowInputForPaymentPage(String webUrl){
        try {
            TicketJson ticket = flJsonParser.getTicketDetailObject(mJson);
            return (((Integer.parseInt(ticket.getPaymentmodeid()) ==  21)
                    || (Integer.parseInt(ticket.getPaymentmodeid()) == 59))
                    && isPaymentPage(webUrl));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
