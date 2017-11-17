package hackathon.trimble.trackme.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class NetworkError {

    /*{
        severity: "ERROR"
        text: "Null or empty value: Prescription Id"
        code: "400"
    }*/

    @SerializedName("code")
    public String errorCode;

    @SerializedName("text")
    public String message;

    @SerializedName("message")
    public String nodeMessage;

}
