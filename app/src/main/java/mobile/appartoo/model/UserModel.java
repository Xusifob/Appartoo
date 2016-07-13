package mobile.appartoo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class UserModel implements JsonModel {
    private Boolean isSmoker;
    private Boolean inRelationship;
    private Double income;
    private Date birthDate;
    private String id;
    private String user;
    private String society;
    private String function;
    private String contract;
    private String plus;
    private String description;
    private String familyName;
    private String gender;
    private String telephone;
    private String givenName;
    private ImageModel imageModel;
    private AddressModel addressModel;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public Boolean getInRelationship() {
        return inRelationship;
    }

    public void setInRelationship(Boolean inRelationship) {
        this.inRelationship = inRelationship;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getPlus() {
        return plus;
    }

    public void setPlus(String plus) {
        this.plus = plus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public int getAge() {
        Calendar now = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();
        birthdate.setTime(this.birthDate);
        int age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
        if (birthdate.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR)) {
            age -= 1;
        }
        return age;
    }

    @Override
    public UserModel createFromJSON(JSONObject jsonObject) throws JSONException {
        this.isSmoker = Boolean.valueOf(jsonObject.getString("isSmoker"));
        this.inRelationship = Boolean.valueOf(jsonObject.getString("inRelationship"));
        this.id = jsonObject.getString("@id");
        this.user = jsonObject.getString("user");
        this.society = jsonObject.getString("society");
        this.function = jsonObject.getString("function");
        this.contract = jsonObject.getString("contract");
        this.plus = jsonObject.getString("plus");
        this.description = jsonObject.getString("description");
        this.familyName = jsonObject.getString("familyName");
        this.givenName = jsonObject.getString("givenName");
        this.gender = jsonObject.getString("gender");
        this.telephone = jsonObject.getString("telephone");

        String birthDateStr = jsonObject.getString("birthDate");
        try {
            this.birthDate = format.parse(birthDateStr);
        } catch (Exception e) {
            this.birthDate = null;
            e.printStackTrace();
        }

        this.imageModel = new ImageModel().createFromJSON(jsonObject.getJSONObject("image"));
        this.addressModel = new AddressModel().createFromJSON(jsonObject.getJSONObject("address"));
        return this;
    }
}
