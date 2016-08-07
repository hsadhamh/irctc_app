package irctc.factor.app.irctcmadeeasy.View.WebView;

import android.net.Uri;

public interface LoadingInterceptor {
    public boolean validate(Uri uri);
    public void exec(Uri uri);
}
