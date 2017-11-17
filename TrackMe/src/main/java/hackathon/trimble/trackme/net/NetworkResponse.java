package hackathon.trimble.trackme.net;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public interface NetworkResponse {

    interface Listener{
        void onResponse(Object result);
    }
    interface ErrorListener{
        void onError(NetworkException error, boolean isSessionInvalid);
    }
}
