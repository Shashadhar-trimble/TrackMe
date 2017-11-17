package hackathon.trimble.trackme.Exceptions;


/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class NetworkException extends Exception {

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException() {
        super("Unable to connect internet");
    }
}
