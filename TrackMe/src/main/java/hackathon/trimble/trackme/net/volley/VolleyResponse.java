package hackathon.trimble.trackme.net.volley;


import hackathon.trimble.trackme.net.NetworkException;
import hackathon.trimble.trackme.net.NodeResponse;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public interface VolleyResponse {

    interface JavaApiListener{
        <T> void onResponse(T response);
    }

    interface Listener{
        void onResponse(NodeResponse response);
    }

    interface ErrorListener{
        void onError(NetworkException error);
    }

    interface AuthErrorListener{
        void onAuthError();
    }
}
