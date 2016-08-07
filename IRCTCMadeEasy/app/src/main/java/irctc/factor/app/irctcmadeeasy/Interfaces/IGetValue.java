package irctc.factor.app.irctcmadeeasy.Interfaces;

import irctc.factor.app.irctcmadeeasy.Json.TicketJson;

/**
 * Created by AHAMED on 7/30/2016.
 */
public interface IGetValue {
     public void getTicketJsonValue(TicketJson tj);
     public void getPaymentJsonValue(TicketJson tj);
     public void getPassengerJsonValue(TicketJson tj);
}
