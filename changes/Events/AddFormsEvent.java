package irctc.factor.app.irctcmadeeasy.Events;

/**
 * Created by Ahamed.Yaseen on 17-07-2016.
 */
public class AddFormsEvent {

    String message;
    public AddFormsEvent(String msg){message=msg;}

    public String getMessage() {
        return message;
    }
}
