package hackathon.trimble.trackme.net.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;


import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import hackathon.trimble.trackme.net.ssl.EasyX509TrustManager;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class VolleyRequestQueue {

    private static final String TAG = "VolleyRequestQueue";

    private static char[] KEYSTORE_PASSWORD = "positra".toCharArray();

    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleyRequestQueue(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
           // mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new HurlStack(null, newSslSocketFactory()));
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new EasyX509TrustManager(trusted)}, null);

            SSLSocketFactory sf = context.getSocketFactory();
            return sf;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
