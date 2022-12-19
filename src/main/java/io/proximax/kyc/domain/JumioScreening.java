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
import java.util.Locale;
import io.proximax.xpx.utils.CryptoUtils;

/**
 * A IDM Screening data.
 */

@Document(collection = "jumio_screening")
public class JumioScreening implements Serializable {

	private static final long serialVersionUID = 1L;
	@Field("identify")
		public String Id;

	@Field("type")
		private String type;

	@Field("country")
		private String country;

	@Field("frontsideImage")
		private String frontsideImage;

	@Field("backsideImage")
		private String backsideImage;

	public String getId() {
		return this.Id;
	}
	public void setId(String id) {
		this.Id = id;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
            this.country = country;
            /*
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            if(country.contains(obj.getDisplayCountry())){
                this.country = obj.getISO3Country().toUpperCase();
            }              
        }*/
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param frontsideImage the frontsideImage to set
	 */
	public void setFrontsideImage(String frontsideImage) {
		this.frontsideImage = frontsideImage;
	}

	/**
	 * @return the frontsideImage
	 */
	public String getFrontsideImage() {
		return frontsideImage;
	}

	/**
	 * @param backsideImage the backsideImage to set
	 */
	public void setBacksideImage(String backsideImage) {
		this.backsideImage = backsideImage;
	}

	/**
	 * @return the backsideImage
	 */
	public String getBacksideImage() {
		return backsideImage;
	}
	public JSONObject toJsonObject() {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("type", this.type);
		jsonBody.put("country", this.country);
		jsonBody.put("frontsideImage", this.frontsideImage);
		jsonBody.put("backsideImage", this.backsideImage);
		
		return jsonBody;
	}

	public JumioScreening encrypt(String pass) {
        String front = "";
        String back = "";
        try{
                front = CryptoUtils.encryptToBase64String(frontsideImage.getBytes(), pass.toCharArray());
                back = CryptoUtils.encryptToBase64String(backsideImage.getBytes(), pass.toCharArray());
            }catch (Exception e){
                e.printStackTrace();
        }
		this.frontsideImage = front;
		this.backsideImage = back;
        return this;
    }
    public JumioScreening decrypt(String pass) {
        String front = "";
        String back = "";
        try{
                front = CryptoUtils.decryptToBase64String(this.frontsideImage, pass.toCharArray());
                back = CryptoUtils.decryptToBase64String(this.backsideImage, pass.toCharArray());
            }catch (Exception e){
                e.printStackTrace();
            }
            
		this.frontsideImage = front;
		this.backsideImage = back;
        return this;
    }

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
}
