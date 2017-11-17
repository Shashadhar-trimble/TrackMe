package hackathon.trimble.trackme.Exceptions;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class ParsingException extends Exception {

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException() {
        super("Backed server issue, please try after some time.");
    }
}
