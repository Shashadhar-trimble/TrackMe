package hackathon.trimble.trackme.net;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import hackathon.trimble.trackme.model.Locator;
import hackathon.trimble.trackme.net.volley.PublishResponse;
import hackathon.trimble.trackme.net.volley.VolleyRequest;
import hackathon.trimble.trackme.net.volley.VolleyResponse;
import hackathon.trimble.trackme.net.volley.VolleyWorker;
import hackathon.trimble.trackme.utils.AppProperties;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class VolleyJavaCommunicator {

    private final Context mCtx;

    public VolleyJavaCommunicator(Context context) {
        this.mCtx = context;
    }

    private String getEndpointUrl(String endpoint) {
        String rootUrl = AppProperties.getInstance().getProperty("rooturl");
        return rootUrl + AppProperties.getInstance().getProperty(endpoint);
    }

    private String getNodeUrl() {
        return AppProperties.getInstance().getProperty("nodeurl");
    }

    private String getNodeMethod(String method) {
        return AppProperties.getInstance().getProperty(method);
    }

    public void getInfoById(String id, final NetworkResponse.Listener listener, final NetworkResponse.ErrorListener errorListener, String tag) {
        try {
            String url = getEndpointUrl("video_chat_session");
            url += "?appointmentId=" + String.valueOf(id);
            final VolleyRequest volleyRequest = new VolleyRequest();
            volleyRequest.url = url;
            volleyRequest.method = Request.Method.GET;
            volleyRequest.context = mCtx;
            volleyRequest.tag = tag;

            VolleyWorker.doJavaNetworkOperation(volleyRequest, new VolleyResponse.JavaApiListener() {
                @Override
                public <T> void onResponse(T response) {
                    try {
                        if(response!=null ) {
                            listener.onResponse("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new VolleyResponse.ErrorListener() {
                @Override
                public void onError(NetworkException error) {
                    try {
                        errorListener.onError(error, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new VolleyResponse.AuthErrorListener() {
                @Override
                public void onAuthError() {

                }
            },String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static String nodeResponseToString(NodeResponse response){
        return new Gson().toJson(response.result);
    }

    public  void publishData(String id, Locator locator , final NetworkResponse.Listener listener, final NetworkResponse.ErrorListener errorListener, String tag) {
        try {
            String url = getEndpointUrl("locator");
            url += id;
            final VolleyRequest volleyRequest = new VolleyRequest();
            volleyRequest.url = url;
            volleyRequest.method = Request.Method.PUT;
            volleyRequest.context = mCtx;
            volleyRequest.tag = tag;
            volleyRequest.data= new Gson().toJson(locator);
            volleyRequest.contentType="application/json";


            VolleyWorker.doJavaNetworkOperation(volleyRequest, new VolleyResponse.JavaApiListener() {
                @Override
                public <T> void onResponse(T response) {
                    try {
                        if (response != null) {
                            listener.onResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new VolleyResponse.ErrorListener() {
                @Override
                public void onError(NetworkException error) {
                    try {
                        errorListener.onError(error, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new VolleyResponse.AuthErrorListener() {
                @Override
                public void onAuthError() {

                }
            }, PublishResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
