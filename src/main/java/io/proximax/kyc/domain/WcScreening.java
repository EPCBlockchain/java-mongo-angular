package io.proximax.kyc.domain;

import java.io.Serializable;

import com.google.common.base.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * A IDM Screening data.
 */

@Document(collection = "wc_screening")
public class WcScreening implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    public String Id;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("middle_name")
    private String middleName;
          	
    @Field("national")
    private String national;
    
    @Field("dob")
    private String dob;
    
    @Field("passport_id")
    private String passportId;
    
    @Field("gender")
    private String gender;

    @Field("group_id")
    private String groupId;    
    
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
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
	
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String group_id) {
        this.groupId = group_id;
	}
	
    public String getNational() {
        return this.national;
    }
    public void setNational(String nation) {
        this.national = nation;
        /*
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            if (nation.contains(obj.getDisplayCountry())){
                this.national = obj.getISO3Country().toUpperCase();
            }            
        }*/
    }

    public JSONObject toJsonObject() {
        JSONObject jsonBody = new JSONObject();
		if (this.groupId != null && !this.groupId.isEmpty())
            jsonBody.put("groupId", this.groupId);
        jsonBody.put("entityType","INDIVIDUAL");
        JSONArray ccategoryValues = new JSONArray();
        /*Add customFields with []*/
        jsonBody.put("customFields",ccategoryValues);
        JSONArray categoryValues = new JSONArray();
        categoryValues.add("WATCHLIST");
        jsonBody.put("providerTypes",categoryValues);
        if (this.middleName != null && !this.middleName.isEmpty())
            jsonBody.put("name", this.firstName + " " + this.middleName + " " +this.lastName);
		else
			jsonBody.put("name", this.firstName + " " + this.lastName);
		
        /*Add secondaryFields*/
        JSONArray sfcategoryValues = new JSONArray();
        
        /*national*/
		if (this.national != null && !this.national.isEmpty()) {
		    JSONObject json1 = new JSONObject();
            json1.put("typeId","SFCT_3");
            json1.put("value",this.national);
			sfcategoryValues.add(json1);
	    }        
		
		 /*dob*/        
		if (this.dob != null && !this.dob.isEmpty()) {
			JSONObject json2 = new JSONObject();
            json2.put("typeId","SFCT_2");
            json2.put("dateTimeValue",this.dob);
			sfcategoryValues.add(json2);
		}
        		
		/*gender*/        
		if (this.gender != null && !this.gender.isEmpty()) {
			JSONObject json3 = new JSONObject();
            json3.put("typeId","SFCT_1");
            json3.put("value",this.gender);
			sfcategoryValues.add(json3);
        }        
		
        jsonBody.put("secondaryFields",sfcategoryValues);
		
        /*passportCheck*/
        JSONArray ppcategoryValues = new JSONArray();
        /*PASSPORT_GIVEN_NAMES*/
		if (this.firstName != null && !this.firstName.isEmpty()) {
			JSONObject json4 = new JSONObject();
            json4.put("typeId","SFCT_8");
            json4.put("value",this.firstName);
			ppcategoryValues.add(json4);
		}
       
        /*PASSPORT_LAST_NAME*/
		if (this.lastName != null && !this.lastName.isEmpty()) {
            JSONObject json5 = new JSONObject();
            json5.put("typeId","SFCT_9");
            json5.put("value",this.lastName);
            ppcategoryValues.add(json5);
		}
        /*PASSPORT_NATIONALITY*/
		if (this.national != null && !this.national.isEmpty()) {
            JSONObject json6 = new JSONObject();
            json6.put("typeId","SFCT_12");
            json6.put("value",this.national);
            ppcategoryValues.add(json6);
		}

        /*PASSPORT_DATE_OF_BIRTH*/
		if (this.dob != null && !this.dob.isEmpty()) {
            JSONObject json7 = new JSONObject();
            json7.put("typeId","SFCT_13");
            json7.put("value",this.dob);
            ppcategoryValues.add(json7);
        }
		
        /*PASSPORT_ID_NUMBER*/
		if (this.passportId != null && !this.passportId.isEmpty()) {
            JSONObject json8 = new JSONObject();
            json8.put("typeId","SFCT_15");
            json8.put("value",this.passportId);
            ppcategoryValues.add(json8);
        }
        jsonBody.put("passportCheck",ppcategoryValues);
		return jsonBody;		
	}
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
