package hackathon.trimble.trackme.net;

import android.content.Context;

import com.google.gson.Gson;

import hackathon.trimble.trackme.utils.AppProperties;


/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class VolleyCommunicator {
    private final Context mCtx;

    public VolleyCommunicator(Context context) {
        this.mCtx = context;
    }

    private String getNodeUrl() {
        return AppProperties.getInstance().getProperty("nodeurl");
    }

    private String getNodeMethod(String method) {
        return AppProperties.getInstance().getProperty(method);
    }

    private String getEndpointUrl(String endpoint) {
        String rootUrl = AppProperties.getInstance().getProperty("rooturl");
        return rootUrl + AppProperties.getInstance().getProperty(endpoint);
    }
    public static String getImageUrl(String imageUrl) {
        String rootImageUrl = AppProperties.getInstance().getProperty("rootImageUrl");
        return rootImageUrl + imageUrl;
    }

    static String nodeResponseToString(NodeResponse response){
        return new Gson().toJson(response.result);
    }



}
