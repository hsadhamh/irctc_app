package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by hassanhussain on 7/14/2016.
 */
public class flJsonParser {
    public static TicketJson getTicketDetailObject(String json) throws JSONException, IOException {
         TicketJson dataHolder = new ObjectMapper().readValue(json, TicketJson.class);
        return dataHolder;
    }
    public static String getTicketDetailString(TicketJson ticket) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(ticket);
        return json;
    }


}
