package hackathon.trimble.trackme.net;
/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class NetworkException extends Exception {

    public String errorCode;

    public NetworkException(){

    }

    public NetworkException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public NetworkException(String message){
        super(message);
    }
}
