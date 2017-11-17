package hackathon.trimble.trackme.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class NodeRequest {

    @SerializedName("method")
    public String method;

    @SerializedName("id")
    public int id = 1;

    @SerializedName("params")
    public Object params;
}
