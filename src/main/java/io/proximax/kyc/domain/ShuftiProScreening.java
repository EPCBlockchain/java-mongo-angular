package io.proximax.kyc.domain;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.base.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Set;
import java.util.HashSet;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * A IDM Screening data.
 */

@Document(collection = "shuftipro_screening")
public class ShuftiProScreening implements Serializable {

	private static final long serialVersionUID = 1L;

	@Field("first_name")
		private String firstName;

	@Field("last_name")
		private String lastName;

	@Field("middle_name")
		private String middleName;
        
        @Field("dob")
		private String dob;

	@Field("document_type")
		private String documentType;
	
	@Field("document_id_no")
		private String documentIdNo;
		
	@Field("client_id")
		private String clientId;
	
	@Field("reference")
		private String reference;
		
	@Field("phone_number")
		private String phoneNumber;
	
	@Field("lang")
		private String language;
		
	@Field("callback_url")
		private String callbackUrl;
		
	@Field("redirect_url")
		private String redirectUrl;
		
	@Field("face_image")
		private String faceImage; 
    	
	@Field("document_front_image")
		private String documentFrontImage; 
        
    @Field("document_back_image")
		private String documentBackImage; 
    
    @Field("address_image")
		private String addressImage; 
		
	@Field("secret_key")
		private String secretKey; 
		
	@Field("country")
		private String country;
        
    @Field("full_address")
		private String fullAddress;
		
	@Field("hash")
		private String hashCode;
        
	@Field("email")
		private String email;
        
        public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
        
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return this.middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getDob() {
		return this.dob;
	}
	public void setDob(String dob) {
        SimpleDateFormat inFmt = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat outFmt = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            String out = outFmt.format(inFmt.parse(dob));
            this.dob = out;
        }
        catch( ParseException e )
        {
            e.printStackTrace();
        }
	}
	
	public String getDocumentType() {
		return this.documentType;
	}
	public void setDocumentType(String docType) {
		this.documentType=docType;
	}
	
	public String getDocumentIdNo() {
		return this.documentIdNo;
	}
	public void setDocumentIdNo(String docIdNo) {
		this.documentIdNo = docIdNo;
	}
	
	public String getClientId() {
		return this.clientId;
	}
	public void setClientId(String client_id) {
		this.clientId = client_id;
	}
	
	public String getReference() {
		return this.reference;
	}
	public void setReference(String ref) {
		this.reference = ref;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(String phone) {
		this.phoneNumber = phone;
	}
	
        public String getAddress() {
		return this.fullAddress;
	}
	public void setAddress(String address) {
		this.fullAddress = address;
	}
    
    
	public String getLanguage() {
		return this.language;
	}
	public void setLanguage(String lang) {
		this.language = lang;
	}
	
	public String getCallbackUrl() {
		return this.callbackUrl;
	}
	public void setCallbackUrl(String url) {
		this.callbackUrl = url;
	}
	
	public String getRedirectUrl() {
		return this.redirectUrl;
	}
	public void setRedirectUrl(String url) {
		this.redirectUrl = url;
	}
		
	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
            this.country = country;
            /*
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            if(country.contains(obj.getDisplayCountry())){
                this.country = obj.getCountry();
            }                
        }*/
	}
	
	public String getFaceImage() {
		return this.faceImage;
	}
	public void setFaceImage(String face_image) {
		this.faceImage = face_image;
	}
	
	public String getDocumentFrontImage() {
		return this.documentFrontImage;
	}
	public void setDocumentFrontImage(String image) {
		this.documentFrontImage = image;
	}
    
        public String getDocumentBackImage() {
		return this.documentBackImage;
	}
	public void setDocumentBackImage(String image) {
		this.documentBackImage = image;
	}
        public String getAddressImage() {
		return this.addressImage;
	}
	public void setAddressImage(String image) {
		this.addressImage = image;
	}

	public void setSecretKey(String key) {
		this.secretKey = key;
	}
	
	private void setHashCode(String code) {
		this.hashCode = code;
	}
	public JSONObject getVerficationServices() {
		JSONObject jsonBody = new JSONObject();
        String newDate = "";
		if (this.firstName != null && !this.firstName.isEmpty())
			jsonBody.put("first_name", this.firstName);
		if (this.lastName != null && !this.lastName.isEmpty())
			jsonBody.put("last_name", this.lastName);
		if (this.middleName != null && !this.middleName.isEmpty())
			jsonBody.put("middle_name", this.middleName);
		if (this.dob != null && !this.dob.isEmpty())
        {
            jsonBody.put("dob", this.dob);
        }
		if (this.documentType != null && !this.documentType.isEmpty())
			jsonBody.put("document_type", this.documentType);
		jsonBody.put("document_id_no", this.documentIdNo);
        if (this.fullAddress != null && !this.fullAddress.isEmpty())
			jsonBody.put("address", this.fullAddress);
        
		return jsonBody;
	}
	
	public JSONObject getVerficationData() {
		JSONObject jsonBody = new JSONObject();
		if (this.faceImage != null && !this.faceImage.isEmpty())
			jsonBody.put("face_image", this.faceImage);
		if (this.documentFrontImage != null && !this.documentFrontImage.isEmpty())
            jsonBody.put("document_front_image",this.documentFrontImage);
        if (this.documentBackImage != null && !this.documentBackImage.isEmpty())
            jsonBody.put("document_back_image",this.documentBackImage); 
        if (this.addressImage != null && !this.addressImage.isEmpty())
            jsonBody.put("document_address_image",this.addressImage);         
		return jsonBody;
	}
    
	public Map <String,String> getPostData() {
		Map<String, String> postData = new HashMap<String, String>();
		int randomNum = ThreadLocalRandom.current().nextInt(1000, 1000000000 + 1);
		String RawData = "";
		this.setReference("sd-"+randomNum);
		if (this.clientId != null && !this.clientId.isEmpty())
			postData.put("client_id",this.clientId);
		if (this.reference != null && !this.reference.isEmpty())
			postData.put("reference",this.reference);
		if (this.phoneNumber != null && !this.phoneNumber.isEmpty())
			postData.put("phone_number",this.phoneNumber);
		if (this.country != null && !this.country.isEmpty())
			postData.put("country",this.country);
		if (this.language != null && !this.language.isEmpty())
			postData.put("lang",this.language);
		if (this.callbackUrl != null && !this.callbackUrl.isEmpty())
			postData.put("callback_url",this.callbackUrl);
		if (this.redirectUrl != null && !this.redirectUrl.isEmpty())
			postData.put("redirect_url",this.redirectUrl);
		postData.put("verification_services",this.getVerficationServices().toString());
		postData.put("verification_data",this.getVerficationData().toString());
		
		Map<String, String> treeMap = new TreeMap<String, String>(postData);
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			RawData += entry.getValue();
		}   
		if (this.secretKey != null && !this.secretKey.isEmpty()) {
			RawData = RawData + this.secretKey;
			String hash = sha256(RawData);   
			this.setHashCode(hash);
			postData.put("signature",this.hashCode);
		}
		return postData;
	}
	
	public List<NameValuePair> getUrlParameters() {		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		if (this.clientId != null && !this.clientId.isEmpty())
			urlParameters.add(new BasicNameValuePair("client_id",this.clientId));
		if (this.reference != null && !this.reference.isEmpty())
			urlParameters.add(new BasicNameValuePair("reference",this.reference));
		if (this.phoneNumber != null && !this.phoneNumber.isEmpty())
			urlParameters.add(new BasicNameValuePair("phone_number",this.phoneNumber));
		if (this.country != null && !this.country.isEmpty())
			urlParameters.add(new BasicNameValuePair("country",this.country));
		if (this.language != null && !this.language.isEmpty())
			urlParameters.add(new BasicNameValuePair("lang",this.language));
		if (this.callbackUrl != null && !this.callbackUrl.isEmpty())
			urlParameters.add(new BasicNameValuePair("callback_url",this.callbackUrl));
		if (this.redirectUrl != null && !this.redirectUrl.isEmpty())
			urlParameters.add(new BasicNameValuePair("redirect_url",this.redirectUrl));
		urlParameters.add(new BasicNameValuePair("verification_services",this.getVerficationServices().toString()));
		urlParameters.add(new BasicNameValuePair("verification_data",this.getVerficationData().toString()));
		if (this.hashCode != null && !this.hashCode.isEmpty())
		urlParameters.add(new BasicNameValuePair("signature",this.hashCode));
		return urlParameters;
	}
	
    
	public static String encoder(byte[] imageData) {
		String base64Image = "";
		base64Image = Base64.encodeBase64String(imageData);
		return base64Image;
        }
	
        public static String sha256(String base) {
            try{
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(base.getBytes("UTF-8"));
                    StringBuffer hexString = new StringBuffer();

                    for (int i = 0; i < hash.length; i++) {
                            String hex = Integer.toHexString(0xff & hash[i]);
                            if(hex.length() == 1) hexString.append('0');
                            hexString.append(hex);
                    } 

                return hexString.toString();
            } catch(Exception ex){
           throw new RuntimeException(ex);
            }
        }

	@Override
	public int hashCode() {
		return 0;
	}
        public JSONObject getFaceData(){
		JSONObject jsonBody = new JSONObject();
		
		if (this.faceImage!= null && !this.faceImage.isEmpty())
			jsonBody.put("proof",this.faceImage);		
		return jsonBody;
	}
        public JSONObject getPhoneData(){
		JSONObject jsonBody = new JSONObject();
		
		if (this.phoneNumber!= null && !this.phoneNumber.isEmpty())
			jsonBody.put("phone_number",this.phoneNumber);		
		return jsonBody;
	}
        public JSONObject getBackgroundData(){
            JSONObject jsonBody = new JSONObject();
		
            JSONObject name = new JSONObject();
            if (this.firstName != null && !this.firstName.isEmpty())
                    name.put("first_name",this.firstName);
            if (this.lastName != null && !this.lastName.isEmpty())
                    name.put("last_name", this.lastName);
            if (this.middleName != null && !this.middleName.isEmpty())
                    name.put("middle_name", this.middleName);
            jsonBody.put("name",name);
            if (this.dob != null && !this.dob.isEmpty())
                    jsonBody.put("dob", this.dob);		
            return jsonBody;
	}
        public JSONObject getDocumentData() {
            JSONObject jsonBody = new JSONObject();
            if (this.documentFrontImage!= null && !this.documentFrontImage.isEmpty())
                jsonBody.put("proof",this.documentFrontImage);
            if (this.documentBackImage!= null && !this.documentBackImage.isEmpty())
                jsonBody.put("additional_proof",this.documentBackImage);
            JSONArray supportedType = new JSONArray();
            supportedType.add(this.documentType);
		/*supportedType.add("passport");
		supportedType.add("id_card");
		supportedType.add("driving_license");
		supportedType.add("credit_or_debit_card");
        */
            jsonBody.put("supported_types",supportedType);
		
            JSONObject name = new JSONObject();
            if (this.firstName != null && !this.firstName.isEmpty())
                    name.put("first_name",this.firstName);
            if (this.lastName != null && !this.lastName.isEmpty())
                    name.put("last_name", this.lastName);
            if (this.middleName != null && !this.middleName.isEmpty())
                    name.put("middle_name", this.middleName);
            jsonBody.put("name",name);
            if (this.dob != null && !this.dob.isEmpty())
                    jsonBody.put("dob", this.dob);
            if (this.documentIdNo != null && !this.documentIdNo.isEmpty())
                    jsonBody.put("document_number", this.documentIdNo);

            return jsonBody;
	}
        public JSONObject getAddressData() {
		JSONObject jsonBody = new JSONObject();
		if (this.addressImage!= null && !this.addressImage.isEmpty())
			jsonBody.put("proof",this.addressImage);
		
		if (this.fullAddress != null && !this.fullAddress.isEmpty())
			jsonBody.put("full_address",this.fullAddress);
		
		JSONObject name = new JSONObject();
		if (this.firstName != null && !this.firstName.isEmpty())
			name.put("first_name", this.firstName);
		if (this.lastName != null && !this.lastName.isEmpty())
			name.put("last_name", this.lastName);
		if (this.middleName != null && !this.middleName.isEmpty())
			name.put("middle_name", this.middleName);
		jsonBody.put("name",name);
		
		JSONArray supportedType = new JSONArray();
		supportedType.add("id_card");
		supportedType.add("bank_statement");
		jsonBody.put("supported_types",supportedType);
		
		return jsonBody;
	}
        public JSONObject toJsonObject(){
        JSONObject jsonBody = new JSONObject();
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 1000000000 + 1);     
        jsonBody.put("reference",""+randomNum);
        if (this.callbackUrl != null && !this.callbackUrl.isEmpty())
            jsonBody.put("callback_url",this.callbackUrl);
        if (this.email != null && !this.email.isEmpty())
            jsonBody.put("email",this.email);
        if (this.country != null && !this.country.isEmpty())
            jsonBody.put("country",this.country);
        if (this.language != null && !this.language.isEmpty())
            jsonBody.put("language",this.language);
        jsonBody.put("verification_mode","any");
        
        jsonBody.put("face",this.getFaceData());
        jsonBody.put("background_checks",this.getBackgroundData());
        jsonBody.put("document",this.getDocumentData());
        jsonBody.put("address",this.getAddressData());
        return jsonBody;
        
    }
}
