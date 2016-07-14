package irctc.factor.app.irctcmadeeasy.Json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;

/**
 * Created by hassanhussain on 7/14/2016.
 */
public class TicketJson {
    /* USER DETAILS */
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonProperty("username")
    String UserName;

    @JsonProperty("password")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Password;

    /* TRAIN DETAILS */
    @JsonProperty("source")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String SrcStation;

    @JsonProperty("destination")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String DestStation;

    @JsonProperty("boarding")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String BoardingStation;

    @JsonProperty("date-journey")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String DateOfJourney;

    @JsonProperty("train-no")
    @JsonIgnoreProperties(ignoreUnknown=true)
    long TrainNumber;

    @JsonProperty("class")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String ClassSelection;

    @JsonProperty("Quota")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String Quota;

    /*  MISC OPTIONS   */
    @JsonProperty("Auto-Upgrade")
    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean AutoUpgrade;

    @JsonProperty("book-confirm")
    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean BookConfirmTickets;

    @JsonProperty("book-id-cond")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String BookConfirmTicketOption;

    @JsonProperty("preferred-coach")
    @JsonIgnoreProperties(ignoreUnknown=true)
    boolean PreferredCoachSelection;

    @JsonProperty("coachID")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String PreferredCoachID;

    @JsonProperty("mobile")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String MobileNumber;

    /*  PAYMENT OPTIONS   */
    @JsonProperty("payment-mode")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String PaymentMode;

    @JsonProperty("payment-mode-id")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String PaymentModeOptionID;

    @JsonProperty("card-no-value")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String CardNumberValue;

    @JsonProperty("card-type")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String CardType;

    @JsonProperty("expiry-mon")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String ExpiryMonth;

    @JsonProperty("expiry-year")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String ExpiryYear;

    @JsonProperty("Card-CVV")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String CardCvvNumber;

    @JsonProperty("name-card")
    @JsonIgnoreProperties(ignoreUnknown=true)
    String NameOnCard;

    /* Passenger Info */
    @JsonProperty("passenger-info")
    @JsonIgnoreProperties(ignoreUnknown=true)
    List<PassengerJson> PassengerInfo = new ArrayList<>();

    @JsonProperty("child-passenger-info")
    @JsonIgnoreProperties(ignoreUnknown=true)
    List<ChildJson> ChildInfo = new ArrayList<>();

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSrcStation() {
        return SrcStation;
    }

    public void setSrcStation(String srcStation) {
        SrcStation = srcStation;
    }

    public String getDestStation() {
        return DestStation;
    }

    public void setDestStation(String destStation) {
        DestStation = destStation;
    }

    public String getBoardingStation() {
        return BoardingStation;
    }

    public void setBoardingStation(String boardingStation) {
        BoardingStation = boardingStation;
    }

    public String getDateOfJourney() {
        return DateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        DateOfJourney = dateOfJourney;
    }

    public long getTrainNumber() {
        return TrainNumber;
    }

    public void setTrainNumber(long trainNumber) {
        TrainNumber = trainNumber;
    }

    public String getClassSelection() {
        return ClassSelection;
    }

    public void setClassSelection(String classSelection) {
        ClassSelection = classSelection;
    }

    public String getQuota() {
        return Quota;
    }

    public void setQuota(String quota) {
        Quota = quota;
    }

    public boolean getAutoUpgrade() {
        return AutoUpgrade;
    }

    public void setAutoUpgrade(boolean autoUpgrade) {
        AutoUpgrade = autoUpgrade;
    }

    public boolean getBookConfirmTickets() {
        return BookConfirmTickets;
    }

    public void setBookConfirmTickets(boolean bookConfirmTickets) {
        BookConfirmTickets = bookConfirmTickets;
    }

    public String getBookConfirmTicketOption() {
        return BookConfirmTicketOption;
    }

    public void setBookConfirmTicketOption(String bookConfirmTicketOption) {
        BookConfirmTicketOption = bookConfirmTicketOption;
    }

    public boolean getPreferredCoachSelection() {
        return PreferredCoachSelection;
    }

    public void setPreferredCoachSelection(boolean preferredCoachSelection) {
        PreferredCoachSelection = preferredCoachSelection;
    }

    public String getPreferredCoachID() {
        return PreferredCoachID;
    }

    public void setPreferredCoachID(String preferredCoachID) {
        PreferredCoachID = preferredCoachID;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentModeOptionID() {
        return PaymentModeOptionID;
    }

    public void setPaymentModeOptionID(String paymentModeOptionID) {
        PaymentModeOptionID = paymentModeOptionID;
    }

    public String getCardNumberValue() {
        return CardNumberValue;
    }

    public void setCardNumberValue(String cardNumberValue) {
        CardNumberValue = cardNumberValue;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getExpiryMonth() {
        return ExpiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        ExpiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return ExpiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        ExpiryYear = expiryYear;
    }

    public String getCardCvvNumber() {
        return CardCvvNumber;
    }

    public void setCardCvvNumber(String cardCvvNumber) {
        CardCvvNumber = cardCvvNumber;
    }

    public String getNameOnCard() {
        return NameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        NameOnCard = nameOnCard;
    }

}
