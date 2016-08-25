package irctc.factor.app.irctcmadeeasy.Activity;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import okhttp3.HttpUrl;
import okio.BufferedSource;
import okio.Okio;

public class AdBlocker {
    private static final String AD_HOSTS_FILE = "pgl.yoyo.org.txt";
    private static final Set<String> AD_HOSTS = new HashSet<>();

    public static void init(final Context context) {
        try {
            AdBlocker.loadFromAssets(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
    }

    @WorkerThread
    private static Void loadFromAssets(Context context) throws IOException {
        InputStream stream = context.getAssets().open(AD_HOSTS_FILE);
        BufferedSource buffer = Okio.buffer(Okio.source(stream));
        String line;
        while ((line = buffer.readUtf8Line()) != null) {
            AD_HOSTS.add(line);
        }
        buffer.close();
        stream.close();
        return null;
    }
    public static boolean isAd(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        return isAdHost(httpUrl != null ? httpUrl.host() : "");
    }
    /**
     * Recursively walking up sub domain chain until we exhaust or find a match,
     * effectively doing a longest substring matching here
     */
    private static boolean isAdHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        int index = host.indexOf(".");
        return index >= 0 && (AD_HOSTS.contains(host) ||
                index + 1 < host.length() && isAdHost(host.substring(index + 1)));
    }
}