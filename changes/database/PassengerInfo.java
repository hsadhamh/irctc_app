package irctc.factor.app.irctcmadeeasy.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PASSENGER_INFO".
 */
public class PassengerInfo {

    private Long id;
    private String Name;
    private Integer Age;
    private String Gender;
    private String Food;
    private String Berth;
    private String Child;
    private String Nationality;
    private Integer AadharCardNo;
    private long transactionId;

    public PassengerInfo() {
    }

    public PassengerInfo(Long id) {
        this.id = id;
    }

    public PassengerInfo(Long id, String Name, Integer Age, String Gender, String Food, String Berth, String Child, String Nationality, Integer AadharCardNo, long transactionId) {
        this.id = id;
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.Food = Food;
        this.Berth = Berth;
        this.Child = Child;
        this.Nationality = Nationality;
        this.AadharCardNo = AadharCardNo;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer Age) {
        this.Age = Age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String Food) {
        this.Food = Food;
    }

    public String getBerth() {
        return Berth;
    }

    public void setBerth(String Berth) {
        this.Berth = Berth;
    }

    public String getChild() {
        return Child;
    }

    public void setChild(String Child) {
        this.Child = Child;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String Nationality) {
        this.Nationality = Nationality;
    }

    public Integer getAadharCardNo() {
        return AadharCardNo;
    }

    public void setAadharCardNo(Integer AadharCardNo) {
        this.AadharCardNo = AadharCardNo;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

}
