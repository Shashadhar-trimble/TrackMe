package hackathon.trimble.trackme.net.volley;

import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import hackathon.trimble.trackme.net.NetworkError;
import hackathon.trimble.trackme.net.NetworkException;
import hackathon.trimble.trackme.net.NodeError;
import hackathon.trimble.trackme.net.NodeResponse;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class VolleyWorker {

    public static String USER_SESSION_INVALID_CODE = "402";

    public static void doNetworkOperation(VolleyRequest request, final VolleyResponse.Listener listener,
                                          final VolleyResponse.ErrorListener errorListener, final VolleyResponse.AuthErrorListener authErrorListener) {
        try {
            Map<String, String> headers = new HashMap<>();
            if (request.contentType != null) {
                headers.put("Content-Type", request.contentType);
            }
            headers.put("device-identifier", getDeviceIdentifiers());
            GsonRequest<NodeResponse> gsonRequest = new GsonRequest<>(request.method, request.url, NodeResponse.class, headers,
                    new Response.Listener<NodeResponse>() {
                        @Override
                        public void onResponse(NodeResponse response) {
                            try {
                                listener.onResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (errorListener != null) {
                                    errorListener.onError(new NetworkException());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String response = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        VolleyLog.e("Error response %s", response);
                        NodeError nodeError = new Gson().fromJson(response, NodeError.class);
                        NetworkError networkError = nodeError.error;
                        String message = networkError.message != null ? networkError.message : networkError.nodeMessage;
                        NetworkException ne = new NetworkException(networkError.errorCode, message);
                        if (networkError.errorCode.equals(String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED))) {
                            authErrorListener.onAuthError();
                        } else {
                            errorListener.onError(ne);
                        }
                    } catch (Exception e) {
                        if (errorListener != null) {
                            errorListener.onError(new NetworkException());
                        }
                    }
                }
            });
            VolleyLog.e("Request url %s", request.url);
            if (request.method == Request.Method.POST) {
                gsonRequest.setRequestBody(request.data);
                if (request.data != null) {
                    VolleyLog.e("Request body %s", request.data);
                }
            }
            if (request.tag != null) {
                gsonRequest.setTag(request.tag);
            }
            VolleyRequestQueue.getInstance(request.context).addToRequestQueue(gsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static<T> void doJavaNetworkOperation(VolleyRequest request, final VolleyResponse.JavaApiListener listener,
                                                 final VolleyResponse.ErrorListener errorListener, final VolleyResponse.AuthErrorListener authErrorListener, Class<T> clazz) {
        try {
            Map<String, String> headers = new HashMap<>();
            if (request.contentType != null) {
                headers.put("Content-Type", request.contentType);
            }
            //headers.put("device-identifier", getDeviceIdentifiers());
            GsonRequest<T> gsonRequest = new GsonRequest<>(request.method, request.url, clazz, headers,
                    new Response.Listener<T>() {
                        @Override
                        public void onResponse(T response) {
                            try {
                                listener.onResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                                VolleyLog.e("Error response %s", e);
                                if (errorListener != null) {
                                    errorListener.onError(new NetworkException());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String response = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        VolleyLog.e("Error response %s", response);
                        NetworkError networkError = new Gson().fromJson(response, NetworkError.class);
                        String message = networkError.message != null ? networkError.message : networkError.nodeMessage;
                        NetworkException ne = new NetworkException(networkError.errorCode, message);
                        if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            if (networkError.errorCode.equals(USER_SESSION_INVALID_CODE)) {
                                errorListener.onError(ne);
                            } else {
                                authErrorListener.onAuthError();
                            }
                        } else {
                            errorListener.onError(ne);
                        }
                    } catch (Exception e) {
                        if (errorListener != null) {
                            errorListener.onError(new NetworkException());
                        }
                    }
                }
            });
            VolleyLog.e("Request url %s", request.url);
            if (request.method == Request.Method.POST) {
                gsonRequest.setRequestBody(request.data);
                if (request.data != null) {
                    VolleyLog.e("Request body %s", request.data);
                }
            }
            if (request.method == Request.Method.PUT) {
                gsonRequest.setRequestBody(request.data);
                if (request.data != null) {
                    VolleyLog.e("Request body %s", request.data);
                }
            }
            if (request.tag != null) {
                gsonRequest.setTag(request.tag);
            }
            VolleyRequestQueue.getInstance(request.context).addToRequestQueue(gsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static String getDeviceIdentifiers() {
        String imei = "";
        try {
            //imei = DeviceInformation.getIMEI();
        } catch (Exception e) {
            //e.printStackTrace();
            //imei = SettingsManager.getInstance().getUniqueDeviceId();
        }
        String osName = "ANDROID";
        String osVersion = Build.VERSION.RELEASE;
        return imei + "::" + "Mobile" + "::" + osName + "::" + osVersion;
    }
}
