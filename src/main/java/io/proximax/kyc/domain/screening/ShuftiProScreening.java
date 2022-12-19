package io.proximax.kyc.domain.screening;

import io.proximax.kyc.domain.ScreeningData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class ShuftiProScreening extends Screening {

    private String passportId;
    private String callBackUrl;
    private String language;
    private String redirectUrl;
    private String clientId;
    private String reference;
    private String secretKey;
    private String hashCode;
    private String referenceId;

    public ShuftiProScreening(HashMap<String, Object> data) {
        
        // NAME
        if (data.containsKey("firstName") && data.get("firstName") != null)
            this.setFirstName(data.get("firstName").toString());
        if (data.containsKey("lastName") && data.get("lastName") != null)
            this.setLastName(data.get("lastName").toString());
        if (data.containsKey("middleName") && data.get("middleName") != null)
            this.setMiddleName(data.get("middleName").toString());

        // DETAILS
        if (data.containsKey("dob") && data.get("dob") != null)
            this.setDob(data.get("dob").toString());
        if (data.containsKey("email") && data.get("email") != null)
            this.setEmail(data.get("email").toString());
        if (data.containsKey("phoneNumber") && data.get("phoneNumber") != null)
            this.setPhoneNumber(data.get("phoneNumber").toString());
        if (data.containsKey("national") && data.get("national") != null)
            this.setNational(data.get("national").toString());

        // ADDRESS
        if (data.containsKey("city") && data.get("city") != null)
            this.setCity(data.get("city").toString());
        if (data.containsKey("firstLine") && data.get("firstLine") != null)
            this.setFirstLine(data.get("firstLine").toString());
        if (data.containsKey("secondLine") && data.get("secondLine") != null)
            this.setSecondLine(data.get("secondLine").toString());
        if (data.containsKey("house") && data.get("house") != null)
            this.setHouse(data.get("house").toString());
        if (data.containsKey("apartmentNumber") && data.get("apartmentNumber") != null)
            this.setApartmentNumber(data.get("apartmentNumber").toString());
        if (data.containsKey("state") && data.get("state") != null)
            this.setState(data.get("state").toString());
        if (data.containsKey("country") && data.get("country") != null)           
            this.setCountry(convertCountry2LetterCode(data.get("country").toString()));
        if (data.containsKey("countryCode") && data.get("countryCode") != null)
            this.setCountryCode(data.get("countryCode").toString());
        if (data.containsKey("zipCode") && data.get("zipCode") != null)
            this.setZipCode(data.get("zipCode").toString());

        // DOCUMENTS
        if (data.containsKey("idNumber") && data.get("idNumber") != null)
            this.setIdNumber(data.get("idNumber").toString());

        if (data.containsKey("addressImage") && data.get("addressImage") != null) {
            JSONArray array = (JSONArray) data.get("addressImage");
            HashMap<String, String> addressImage = (HashMap) array.iterator().next();
            this.setAddressImage(new JSONObject(addressImage));
        }

        if (data.containsKey("frontSideImage") && data.get("frontSideImage") != null){
            JSONArray array = (JSONArray) data.get("frontSideImage");
            HashMap<String, String> addressImage = (HashMap) array.iterator().next();
            this.setFrontSideImage(new JSONObject(addressImage));
        }
        if (data.containsKey("backSideImage") && data.get("backSideImage") != null){
            JSONArray array = (JSONArray) data.get("backSideImage");
            if(!array.isEmpty()){
            HashMap<String, String> addressImage = (HashMap) array.iterator().next();
            this.setBackSideImage(new JSONObject(addressImage));
            }
        }

        if (data.containsKey("faceImage") && data.get("faceImage") != null){
            JSONArray array = (JSONArray) data.get("faceImage");
            HashMap<String, String> addressImage = (HashMap) array.iterator().next();
            this.setFaceImage(new JSONObject(addressImage));
        }
    }

    public void setDob(String dob) {
        SimpleDateFormat inFmt = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat outFmt = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            this.dob = outFmt.format(inFmt.parse(dob));
        }
        catch( ParseException e )
        {
            e.printStackTrace();
        }
    }

    public void setPassportId(String value) { this.passportId = value; }
    public String getPassportId() { return this.passportId; }

    public void setCallbackUrl(String value) { this.callBackUrl = value; }
    public String getCallBackUrl() { return this.callBackUrl; }

    public void setRedirectUrl(String url) { this.redirectUrl = url; }
    public String getRedirectUrl() { return this.redirectUrl; }

    public void setLanguage(String value) { this.language = value; }
    public String getLanguage() { return this.language; }

    public void setClientId(String value) { this.clientId = value; }
    public String getClientId() { return this.clientId; }

    public void setReference(String value) { this.reference = value; }
    public String getReference() { return this.reference; }

    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getSecretKey() { return this.secretKey; }

    public void setHashCode(String hashCode) { this.hashCode = hashCode; }
    public String getHashCode() { return this.hashCode; }
    public String convertCountry2LetterCode(String country){
        
        String countryTemp = country;
        countryTemp = countryTemp.toLowerCase();
        countryTemp = countryTemp.replaceAll("\\s+","");
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            String rawNational = obj.getDisplayCountry();
            rawNational = rawNational.toLowerCase();
            rawNational = rawNational.replaceAll("\\s+","");
            if(countryTemp.contains(rawNational)){
                return obj.getCountry().toUpperCase();

            }              
        }
        return "US";
    }
}
