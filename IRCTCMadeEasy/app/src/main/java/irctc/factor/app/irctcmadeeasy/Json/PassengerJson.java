package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by hassanhussain on 7/14/2016.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class PassengerJson {
    @JsonIgnoreProperties(ignoreUnknown=true)
    String name;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String age;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String gender;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String berth;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String nationality;

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

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSenior() {
        return senior;
    }

    public void setSenior(boolean senior) {
        this.senior = senior;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    String idcard;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String idcardno;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String type;

    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean senior;


}