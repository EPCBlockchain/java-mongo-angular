package io.proximax.kyc.domain;

import java.io.Serializable;

import com.google.common.base.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import org.json.*;
import org.json.simple.JSONObject;
import java.util.Set;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
/**
 * A IDM Screening data.
 */

@Document(collection = "idm_screening")
public class IdmScreening implements Serializable {

	private static final long serialVersionUID = 1L;
	@Field("identify")
		public String Id;

	@Field("first_name")
		private String firstName;

	@Field("last_name")
		private String lastName;

	@Field("middle_name")
    private String middleName;


	@Field("national")
		private String national;
        
    @Field("country")
		private String country; 
    
	@Field("dob")
		private String dob;

	@Field("passport_id")
		private String passportId;

	@Field("id_type")
		private String idType;

	@Field("scanData")
		private String scanData;
    @Field("street")
		private String street;

    @Field("zip_code")
		private String zipCode;
        /*
	@Field("state")
		private String state;
        */
	@Field("city")
		private String city;
          
	@Field("backsideImageData")
		private String backsideImageData;

    @Field("faceImages")
    private String faceImages;
    
    public String getFaceImages() {
        return this.faceImages;
    }
    public void setFaceImages(String faceImages) {
        this.faceImages = faceImages;
    }

	public String getScanData() {
		return this.scanData;
	}
	public void setScanData(String scanData) {
		this.scanData = scanData;
	}

	public String getBacksideImageData() {
		return this.backsideImageData;
	}
	public void setBacksideImageData(String backsideImageData) {
		this.backsideImageData = backsideImageData;
	}

	public void setIdType( String id_type) {
		this.idType = id_type;
	}

	public String getIdType() {
		return this.idType;
	}

	public String getId() {
		return this.Id;
	}
	public void setId(String id) {
		this.Id = id;
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
	public String getPassportId() {
		return this.passportId;
	}
	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}
    public String getNational() {
		return this.national;
	}
	public void setNational(String national) {
            this.national = national;
            /*
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            if(national.contains(obj.getDisplayCountry())){
                this.national = obj.getCountry();
            }            
        }*/
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
    public String getStreet() {
        return this.street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getZipCode() {
        return this.zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    /*
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }*/
	public JSONObject toJsonObject() {
		JSONObject jsonBody = new JSONObject();  
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 1000000000 + 1);        
		jsonBody.put("man", this.firstName+"_"+randomNum);
        
        if (this.firstName != null && !this.firstName.isEmpty())
			jsonBody.put("bfn", this.firstName);
        if (this.lastName != null && !this.lastName.isEmpty())
			jsonBody.put("bln", this.lastName);
        
        if (this.country != null && !this.country.isEmpty())
			jsonBody.put("bco", this.country);
        
		if (this.dob != null && !this.dob.isEmpty()){
            jsonBody.put("dob", this.dob);
        }
        if (this.street != null && !this.street.isEmpty())
            jsonBody.put("bsn", this.street);
        /*
        if (this.state != null && !this.state.isEmpty())
            jsonBody.put("bs", this.state);
        */
        if (this.city != null && !this.city.isEmpty())
            jsonBody.put("bc", this.city);
        if (this.zipCode != null && !this.zipCode.isEmpty())
            jsonBody.put("bz", this.zipCode);
        
		if (this.national != null && !this.national.isEmpty())
			jsonBody.put("docCountry", this.national);
        
        if(this.idType == null){
            this.idType= "PP";
        }
		if (this.idType != null && !this.idType.isEmpty())
			jsonBody.put("docType", this.idType);
        
        if(this.idType.equals("PP")){
            if (this.scanData != null && !this.scanData.isEmpty())
			jsonBody.put("scanData", this.scanData);
        }
		if(this.idType.equals("ID")){
            if (this.scanData != null && !this.scanData.isEmpty())
			jsonBody.put("scanData", this.scanData);
            if (this.backsideImageData != null && !this.backsideImageData.isEmpty())
			jsonBody.put("backsideImageData", this.backsideImageData);
        }
        if (this.faceImages != null && !this.faceImages.isEmpty())
			jsonBody.put("faceImageData", "[" +this.faceImages + "]");
		return jsonBody;
	}

	@Override
		public int hashCode() {
			return Objects.hashCode(getId());
		}
}
