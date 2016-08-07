package irctc.factor.app.irctcmadeeasy.Events;

import irctc.factor.app.irctcmadeeasy.Json.TicketJson;

/**
 * Created by hassanhussain on 8/7/2016.
 */
public class UpdateJsonValues {
    TicketJson json;
    boolean createIfExists;

    public TicketJson getJson() { return json; }

    public void setJson(TicketJson json) { this.json = json; }

    public boolean isCreateIfExists() { return createIfExists; }

    public void setCreateIfExists(boolean createIfExists) { this.createIfExists = createIfExists; }

    public UpdateJsonValues(TicketJson o, boolean b){ json = o; createIfExists = b; }
}
