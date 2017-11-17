package hackathon.trimble.trackme.net;
import android.util.Log;



import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import hackathon.trimble.trackme.Exceptions.GenericException;
import hackathon.trimble.trackme.net.ssl.EasyX509TrustManager;
import hackathon.trimble.trackme.utils.AppProperties;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class NetworkConnection {

    private final static String TAG = "NetworkConnection";
    private final  static String endpoint = AppProperties.getInstance().getProperty("nodeurl");
    private final  static String javaEndpoint = AppProperties.getInstance().getProperty("rooturl");

    private static NetworkConnection instance = null;

    public static NetworkConnection getInstance() {
        if (null == instance) {
            instance = new NetworkConnection();
        }
        return instance;
    }

    public boolean checkInternetConnectivity() {
       // ConnectivityManager cm = (ConnectivityManager) SecureApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
       //   NetworkInfo networkInfo = cm.getActiveNetworkInfo();
       //return null != networkInfo && networkInfo.isConnectedOrConnecting();
        return  false;
    }

    private String readBytes(byte[] b){
        String s = new String(b, 0, b.length);
        Log.e(TAG , "response: " + s);
        return s;
    }

    private static byte[] readResponse(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];

        while (true) {
            int r = input.read(buf, 0, buf.length);
            if (r == -1) {
                break;
            }
            baos.write(buf, 0, r);
        }
        return baos.toByteArray();
    }

    public String doPost(String payload) throws NetworkException, GenericException {
        Log.e(TAG, endpoint);
        URL url;
        HttpsURLConnection conn = null;
        try {
            url = new URL(endpoint);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(newSslSocketFactory());
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getOutputStream().write(payload.getBytes());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return readBytes(readResponse(in));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            throw new GenericException("Invalid server endpoint");
        } catch (ConnectException e) {
            Log.e(TAG, e.getMessage());
            throw new NetworkException();
        } catch (IOException e) {
            String error = "We are unable to carry out your request at this moment";
            if (conn != null) {
                InputStream in = new BufferedInputStream(conn.getErrorStream());
                try {
                    error = readBytes(readResponse(in));
                    return error;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw new GenericException(error);
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    public String doJavaPost(String payload) throws NetworkException, GenericException {
        Log.e(TAG, javaEndpoint);
        URL url;
        HttpsURLConnection conn = null;
        try {
            url = new URL(javaEndpoint + "healthcare/services/v1/user/signUp/");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(newSslSocketFactory());
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getOutputStream().write(payload.getBytes());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return readBytes(readResponse(in));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            throw new GenericException("Invalid server endpoint");
        } catch (ConnectException e) {
            Log.e(TAG, e.getMessage());
            throw new NetworkException();
        } catch (IOException e) {
            String error = "Unhandled exception";
            if (conn != null) {
                InputStream in = new BufferedInputStream(conn.getErrorStream());
                try {
                    error = readBytes(readResponse(in));
                    return error;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw new GenericException(error);
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
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
}
