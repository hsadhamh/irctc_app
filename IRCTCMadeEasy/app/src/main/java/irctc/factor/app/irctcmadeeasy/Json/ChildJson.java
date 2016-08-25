package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by hassanhussain on 7/14/2016.
 */


@JsonIgnoreProperties(ignoreUnknown=true)
public class ChildJson {
    @JsonIgnoreProperties(ignoreUnknown=true)
    String name;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String age;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
