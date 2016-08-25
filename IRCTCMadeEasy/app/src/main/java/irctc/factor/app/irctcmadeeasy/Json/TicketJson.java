package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassanhussain on 7/14/2016.
 */
public class TicketJson {
    /* USER DETAILS */
    @JsonIgnoreProperties(ignoreUnknown=true)
    String username;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String password;

    /* TRAIN DETAILS */
    @JsonIgnoreProperties(ignoreUnknown=true)
    String source;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String destination;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String boarding;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String datejourney;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String trainno;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String bclass;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String quota;

    /*  MISC OPTIONS   */
    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean autoupgrade;

    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean bookconfirm;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String bookidcond;

    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean prefercoach;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String coachid;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String mobile;

    /*  PAYMENT OPTIONS   */
    @JsonIgnoreProperties(ignoreUnknown=true)
    String paymentmode;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String paymentmodeid;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String cardnovalue;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String cardtype;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String expirymon;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String expiryyear;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String cardcvv;

    @JsonIgnoreProperties(ignoreUnknown=true)
    String namecard;

    /* Passenger Info */
    @JsonIgnoreProperties(ignoreUnknown=true)
    List<PassengerJson> passengerinfo = new ArrayList<>();

    @JsonIgnoreProperties(ignoreUnknown=true)
    List<ChildJson> childinfo = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBoarding() {
        return boarding;
    }

    public void setBoarding(String boarding) {
        this.boarding = boarding;
    }

    public String getDatejourney() {
        return datejourney;
    }

    public void setDatejourney(String datejourney) {
        this.datejourney = datejourney;
    }

    public String getTrainno() {
        return trainno;
    }

    public void setTrainno(String trainno) {
        this.trainno = trainno;
    }

    public String getBclass() {
        return bclass;
    }

    public void setBclass(String bclass) {
        this.bclass = bclass;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public boolean isAutoupgrade() {
        return autoupgrade;
    }

    public void setAutoupgrade(boolean autoupgrade) {
        this.autoupgrade = autoupgrade;
    }

    public boolean isBookconfirm() {
        return bookconfirm;
    }

    public void setBookconfirm(boolean bookconfirm) {
        this.bookconfirm = bookconfirm;
    }

    public String getBookidcond() {
        return bookidcond;
    }

    public void setBookidcond(String bookidcond) {
        this.bookidcond = bookidcond;
    }

    public boolean isPrefercoach() {
        return prefercoach;
    }

    public void setPrefercoach(boolean prefercoach) {
        this.prefercoach = prefercoach;
    }

    public String getCoachid() {
        return coachid;
    }

    public void setCoachid(String coachid) {
        this.coachid = coachid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getPaymentmodeid() {
        return paymentmodeid;
    }

    public void setPaymentmodeid(String paymentmodeid) {
        this.paymentmodeid = paymentmodeid;
    }

    public String getCardnovalue() {
        return cardnovalue;
    }

    public void setCardnovalue(String cardnovalue) {
        this.cardnovalue = cardnovalue;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getExpirymon() {
        return expirymon;
    }

    public void setExpirymon(String expirymon) {
        this.expirymon = expirymon;
    }

    public String getExpiryyear() {
        return expiryyear;
    }

    public void setExpiryyear(String expiryyear) {
        this.expiryyear = expiryyear;
    }

    public String getCardcvv() {
        return cardcvv;
    }

    public void setCardcvv(String cardcvv) {
        this.cardcvv = cardcvv;
    }

    public String getNamecard() {
        return namecard;
    }

    public void setNamecard(String namecard) {
        this.namecard = namecard;
    }

    public List<PassengerJson> getPassengerinfo() {
        return passengerinfo;
    }

    public void setPassengerinfo(List<PassengerJson> passengerinfo) {
        this.passengerinfo = passengerinfo;
    }

    public List<ChildJson> getChildinfo() {
        return childinfo;
    }

    public void setChildinfo(List<ChildJson> childinfo) {
        this.childinfo = childinfo;
    }
}
