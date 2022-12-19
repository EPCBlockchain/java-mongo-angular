package io.proximax.kyc.domain;

import java.io.Serializable;

public class ScreeningData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String middleName;
    private String dob;
    private String gender;
    private String national;
    private String country;
    private String email;
    private String firstLine;
    private String secondLine;
    private String house;
    private String apartmentNumber;
    private String city;
    private String state;
    private String zipCode;
    private String idType;
    private String idNumber;
    private String countryCode;
    private String phoneNumber;
    private String frontSideImage;
    private String backSideImage;
    private String faceImage;
    private String addressImage;
    private String screeningType;
    
    public String getScreeningType() {
        return screeningType;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getDob() {
        return dob;
    }
    public String getGender() {
        return gender;
    }
    public String getNational() {
        return national;
    }
    public String getCountry() {
        return country;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstLine() {
        return firstLine;
    }
    public String getSecondLine() {
        return secondLine;
    }
    public String getHouse() {
        return house;
    }
    public String getApartmentNumber() {
        return apartmentNumber;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZipCode() {
        return zipCode;
    }
    public String getIdType() {
        return idType;
    }
    public String getIdNumber() {
        return idNumber;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getFrontSideImage() {
        return frontSideImage;
    }
    public String getBackSideImage() {
        return backSideImage;
    }
    public String getFaceImage() {
        return faceImage;
    }
    public String getAddressImage() {
        return addressImage;
    }
    
    
    public void setScreeningType(String val) {
        this.screeningType = val;
    }
    public void setFirstName(String val) {
        this.firstName = val;
    }
    public void setLastName(String val) {
        this.lastName = val;
    }
    public void setMiddleName(String val) {
        this.middleName = val;
    }
    public void setDob(String val) {
        this.dob = val;
    }
    public void setGender(String val) {
        this.gender = val;
    }
    public void setNational(String val) {
        this.national = val;
    }
    public void setCountry(String val) {
        this.country = val;
    }
    public void setEmail(String val) {
        this.email = val;
    }
    public void setFirstLine(String val) {
        this.firstLine = val;
    }
    public void setSecondLine(String val) {
        this.secondLine = val;
    }
    public void setHouse(String val) {
        this.house = val;
    }
    public void setApartmentNumber(String val) {
        this.apartmentNumber = val;
    }
    public void setCity(String val) {
        this.city = val;
    }
    public void setState(String val) {
        this.state = val;
    }
    public void setZipCode(String val) {
        this.zipCode = val;
    }
    public void setIdType(String val) {
        this.idType = val;
    }
    public void setIdNumber(String val) {
        this.idNumber = val;
    }
    public void setCountryCode(String val) {
        this.countryCode = val;
    }
    public void setPhoneNumber(String val) {
        this.phoneNumber = val;
    }
    
    public void setFrontSideImage(String val) {
        this.frontSideImage = val;
    }
    public void setBackSideImage(String val) {
        this.backSideImage = val;
    }
    public void setFaceImage(String val) {
        this.faceImage = val;
    }
    public void setAddressImage(String val) {
        this.addressImage = val;
    }
    
}
