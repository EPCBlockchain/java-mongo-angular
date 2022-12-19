package io.proximax.kyc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An Organization.
 */
public class OrganizationProfile implements Serializable {

   private static final long serialVersionUID = 1L;

   @Field("description")
   private String description;

   @Field("street")
   private String street;

   @Field("state")
   private String state;

   @Field("country")
   private String country;

   @Field("zipcode")
   private String zipcode;

   @Field("phone")
   private String phone;

   @Field("email")
   private String email;

   @Field("website")
   private String website;

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getStreet() {
      return street;
   }

   public void setStreet(String street) {
      this.street = street;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getZipCode() {
      return zipcode;
   }

   public void setZipCode(String zipcode) {
      this.zipcode = zipcode;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getWebsite() {
      return website;
   }

   public void setWebsite(String website) {
      this.website = website;
   }
}
