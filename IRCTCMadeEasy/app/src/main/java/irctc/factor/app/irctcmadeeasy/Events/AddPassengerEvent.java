package irctc.factor.app.irctcmadeeasy.Events;

/**
 * Created by hassanhussain on 7/12/2016.
 */
public class AddPassengerEvent {
    String message;

    public AddPassengerEvent(String msg){ message = msg; }
    public String getMessage(){ return message; }
}
