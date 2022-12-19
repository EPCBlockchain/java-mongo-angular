package io.proximax.kyc.domain.screening;

import com.google.common.base.Strings;
import org.json.simple.JSONObject;

public class Screening {

    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String national;
    private String country;
    private String countryCode;
    private String email;
    private String phoneNumber;
    protected String dob;
    private String idType;
    private String idNumber;
    private String firstLine;
    private String secondLine;
    private String house;
    private String unit;
    private String apartmentNumber;
    private String street;
    private String zipCode;
    private String state;
    private String city;
    private JSONObject backSideImage;
    private JSONObject frontSideImage;
    private JSONObject faceImage;
    private JSONObject addressImage;
    private String scanData;
    private boolean withAddressVerification = false;

    public void setId(String value) { this.id = value; }
    public String getId() { return this.id; }

    public void setFirstName(String value) { this.firstName = value; }
    public String getFirstName() { return this.firstName; }

    public void setLastName(String value) { this.lastName = value; }
    public String getLastName() { return this.lastName; }

    public void setMiddleName(String value) { this.middleName = value; }
    public String getMiddleName() { return this.middleName; }

    public void setGender(String value) { this.gender = value; }
    public String getGender() { return this.gender; }

    public void setNational(String value) { this.national = value; }
    public String getNational() { return this.national; }

    public void setCountry(String value) { this.country = value; }
    public String getCountry() { return this.country; }

    public void setCountryCode(String value) { this.countryCode = value; }
    public String getCountryCode() { return this.countryCode; }

    public void setEmail(String value) { this.email = value; }
    public String getEmail() { return this.email; }

    public void setPhoneNumber(String value) { this.phoneNumber = value; }
    public String getPhoneNumber() { return this.phoneNumber; }

    public void setDob(String value) { this.dob = value; }
    public String getDob() { return this.dob; }

    public void setIdType(String value) { this.idType = value; }
    public String getIdType() { return this.idType; }

    public void setIdNumber(String value) { this.idNumber = value; }
    public String getIdNumber() { return this.idNumber; }

    public void setFirstLine(String value) { this.firstLine = value; }
    public String getFirstLine() { return this.firstLine; }

    public void setSecondLine(String value) { this.secondLine = value; }
    public String getSecondLine() { return this.secondLine; }

    public void setHouse(String value) { this.house = value; }
    public String getHouse() { return this.house; }

    public void setUnit(String value) { this.unit = value; }
    public String getUnit() { return this.unit; }

    public void setApartmentNumber(String value) { this.apartmentNumber = value; }
    public String getApartmentNumber() { return this.apartmentNumber; }

    public void setStreet(String value) { this.street = value; }
    public String getStreet() { return this.street; }

    public void setZipCode(String value) { this.zipCode = value; }
    public String getZipCode() { return this.zipCode; }

    public void setState(String value) { this.state = value; }
    public String getState() { return this.state; }

    public void setCity(String value) { this.city = value; }
    public String getCity() { return this.city; }

    public String getFullAddress(){
        String address = "";
        if (!Strings.isNullOrEmpty(this.getHouse()))
            address = house;
        if (!Strings.isNullOrEmpty(this.getUnit()))
            address = address + "," + this.getUnit();
        if (!Strings.isNullOrEmpty(this.getFirstLine()))
            address = address + "," + this.getFirstLine();
        if (!Strings.isNullOrEmpty(this.getSecondLine()))
            address = address + "," + this.getSecondLine();
        return address;
    }

    public void setBackSideImage(JSONObject value) { this.backSideImage = value; }
    public JSONObject getBackSideImage() { return this.backSideImage; }
    public String getBackSideImageData() {
        if (this.backSideImage != null && this.backSideImage.containsKey("url") ) {
            return this.backSideImage.get("url").toString();
        }
        return null;
    }

    public void setFrontSideImage(JSONObject value) { this.frontSideImage = value; }
    public JSONObject getFrontSideImage() { return this.frontSideImage; }
    public String getFrontSideImageData() {
        if (this.frontSideImage != null && this.frontSideImage.containsKey("url") ) {
            return this.frontSideImage.get("url").toString();
        }
        return null;
    }

    public void setFaceImage(JSONObject value) { this.faceImage = value; }
    public JSONObject getFaceImage() { return this.faceImage; }
    public String getFaceImageData() {
        if (this.faceImage != null && this.faceImage.containsKey("url") ) {
            return this.faceImage.get("url").toString();
        }
        return null;
    }

    public void setAddressImage(JSONObject value) { this.addressImage = value; }
    public JSONObject getAddressImage() { return this.addressImage; }
    public String getAddressImageData() {
        if (this.addressImage != null && this.addressImage.containsKey("url") ) {
            return this.addressImage.get("url").toString();
        }
        return null;
    }

    public void setScanData(String value) { this.scanData = value; }
    public String getScanData() { return this.scanData; }

    public void setWithAddressVerification(boolean value) { this.withAddressVerification = value; }
    public boolean getWithAddressVerification() { return this.withAddressVerification; }

}
