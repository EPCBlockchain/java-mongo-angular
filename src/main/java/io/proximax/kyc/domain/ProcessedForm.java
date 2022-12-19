package io.proximax.kyc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@org.springframework.data.mongodb.core.mapping.Document(collection = "ProcessedForms")
public class ProcessedForm implements Serializable {

   private static final long serialVersionUID = 1L;

   // Add Models here
   @Id
   private String id;

   @Field("userId")
   private String userId;
   
   @Field("submittedFormId")
   private String submittedFormId;

   private FormIOData form;

   // encrypted submitted form from ui
   @Field("data_hash")
   private String dataHash;

   // rejected or approved
   @Field("status")
   private String status;

   @Field("screeningResult")
   private String screeningResult;
   
   /**
    * @param id the id to set
    */
   public void setId(String id) {
       this.id = id;
   }

   /**
    * @return the id
    */
   public String getId() {
       return id;
   }

   /**
    * @param userId the userId to set
    */
   public void setUserId(String userId) {
       this.userId = userId;
   }

   /**
    * @return the userId
    */
   public String getUserId() {
       return userId;
   }

   /**
    * @return the form
    */
   public FormIOData getForm() {
       return form;
   }

   /**
    * @param form the form to set
    */
   public void setForm(FormIOData form) {
       this.form = form;
   }
   
   /**
    * @param dataHash the dataHash to set
    */
   public void setDataHash(String dataHash) {
       this.dataHash = dataHash;
   }

   /**
    * @return the dataHash
    */
   public String getDataHash() {
       return dataHash;
   }
   
   /**
    * @param status the status to set
    */
   public void setStatus(String status) {
       this.status = status;
   }

   /**
    * @return the status
    */
   public String getStatus() {
       return status;
   }
   
   
   /**
    * @param status the status to set
    */
   public void setSubmittedFormId(String submittedFormId) {
       this.submittedFormId = submittedFormId;
   }

   /**
    * @return the status
    */
   public String getSubmittedFormId() {
       return submittedFormId;
   }
   
   
   /**
    * @param status the status to set
    */
   public void setScreeningResult(String screeningResult) {
       this.screeningResult = screeningResult;
   }

   /**
    * @return the status
    */
   public String getScreeningResult() {
       return screeningResult;
   }
   
}
