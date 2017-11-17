package hackathon.trimble.trackme.Exceptions;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class GenericException extends Exception {

    public GenericException(String message) {
        super(message);
    }

    public GenericException() {
        super("Unhandled exception occure, please contact developer. We regret for inconvenience");
    }
}
