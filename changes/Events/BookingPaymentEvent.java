package irctc.factor.app.irctcmadeeasy.Events;

/**
 * Created by AHAMED on 7/30/2016.
 */
public class BookingPaymentEvent {

    String message;
    public BookingPaymentEvent(String msg){message=msg;}

    public String getMessage() {
        return message;
    }
}
