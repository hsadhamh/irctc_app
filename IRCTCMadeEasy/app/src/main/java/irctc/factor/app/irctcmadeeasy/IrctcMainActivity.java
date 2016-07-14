package irctc.factor.app.irctcmadeeasy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import irctc.factor.app.irctcmadeeasy.WebView.LxWebView;

public class IrctcMainActivity extends AppCompatActivity {

    String mJsAutoFile = TicketConstants.JavaScriptForAutoFill;
    String mLoginUrlPattern =".*?(\\/www\\.irctc\\.co\\.in\\/eticketing\\/loginHome\\.jsf)";
    String mHomeURL = "http://www.irctc.co.in/eticketing/loginHome.jsf";

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
        String sJson = i.getStringExtra("JSON_String");
        GetJsString(sJson);

        assert mWebIrctc != null;

        mWebIrctc.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/12");
        setChromeClientProgress();
        setWebViewClient();
        mWebIrctc.loadUrl(mHomeURL);
    }

    public boolean isLoginPage(String url){
        Pattern p = Pattern.compile(mLoginUrlPattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(url);
        return m.find();
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
            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(getApplicationContext(), "Load Url Override", Toast.LENGTH_SHORT).show();
                mProgress.setProgress(0.0f);
                mProgress.start();
                mProgress.setVisibility(View.VISIBLE);

                view.loadUrl(url);
                return true;
            }*/

            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(getApplicationContext(), "Load Finished", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 19) {
                    mWebIrctc.requestFocus(View.FOCUS_DOWN);
                    view.evaluateJavascript(mJsAutoFile, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Toast.makeText(getApplicationContext(), "on receive - eval script.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    String webUrl = mWebIrctc.getUrl();
                    if(isLoginPage(webUrl)) {
                        showSoftKeyBoard();
                    }
                } else {
                    view.loadUrl(mJsAutoFile);
                }
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
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
    }
}
