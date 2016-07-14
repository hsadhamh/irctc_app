package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hassanhussain on 7/14/2016.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class PassengerJson {
    @JsonProperty("name")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Name;

    @JsonProperty("age")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Age;

    @JsonProperty("gender")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Gender;

    @JsonProperty("berth")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Berth;

    @JsonProperty("nationality")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Nationality;

    @JsonProperty("ID-card")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String IDCard;

    @JsonProperty("ID-card-no")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String IDCardNumber;

    @JsonProperty("type")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Type;

    @JsonProperty("senior")
    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean Senior;

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

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBerth() {
        return Berth;
    }

    public void setBerth(String berth) {
        Berth = berth;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getIDCardNumber() {
        return IDCardNumber;
    }

    public void setIDCardNumber(String IDCardNumber) {
        this.IDCardNumber = IDCardNumber;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isSenior() {
        return Senior;
    }

    public void setSenior(boolean senior) {
        Senior = senior;
    }
}