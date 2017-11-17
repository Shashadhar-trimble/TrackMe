package hackathon.trimble.trackme.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class NodeError {

    @SerializedName("error")
    public NetworkError error;
}
