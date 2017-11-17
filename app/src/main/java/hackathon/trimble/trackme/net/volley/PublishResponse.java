package hackathon.trimble.trackme.net.volley;

import com.google.gson.annotations.SerializedName;

import hackathon.trimble.trackme.model.Locator;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class PublishResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("body")
    private Locator body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Locator getBody() {
        return body;
    }

    public void setBody(Locator body) {
        this.body = body;
    }
}
