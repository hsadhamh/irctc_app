package irctc.factor.app.irctcmadeeasy.Events;

/**
 * Created by hassanhussain on 7/12/2016.
 */
public class DeletePassengerEvent {
    int passengerID;
    public DeletePassengerEvent(int n){ passengerID = n; }
    public int getPassengerID(){ return passengerID; }
}
