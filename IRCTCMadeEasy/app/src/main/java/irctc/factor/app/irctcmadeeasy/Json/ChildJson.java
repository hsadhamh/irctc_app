package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hassanhussain on 7/14/2016.
 */


@JsonIgnoreProperties(ignoreUnknown=true)
public class ChildJson {
    @JsonProperty("name")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Name;

    @JsonProperty("age")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Age;

    @JsonProperty("gender")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Gender;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
}
