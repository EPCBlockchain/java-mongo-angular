package io.proximax.kyc.domain;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.service.dto.SubmittedFormDTO;

import java.io.Serializable;
import java.time.Instant;

@org.springframework.data.mongodb.core.mapping.Document(collection = "submittedforms")
public class SubmittedForm implements Serializable {

   private static final long serialVersionUID = 1L;

   // Add Models here
   @Id
   private String id;

   @Field("user_id")
   private String userId;

   @Field("organization_id")
   private String organizationId;
   
   @Field("form_id")
   private String formId;

   @Field("submission_reference_id")
   private String submissionReferenceId;

   @Field("status")
   private String status;

   @Field("data_hash")
   private String dataHash;

   @Field("screening_data_hash")
   private String screeningDataHash;

   @Field("creation_date")
   private Instant creationDate;
   
   @Field("screening_date")
   private Instant screeningDate;

   @Field("is_screening_form")
   private boolean isScreeningForm;

   @Field("raw")
   private boolean isRaw;

   @Field("screening_type")
   private String screeningType;

   private SubmittedFormDTO formReference;

   private JSONObject data;

   private Form form;

   private JSONObject screeningData;

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
   public JSONObject getData() {
       return data;
   }

   /**
    * @param form the form to set
    */
   public void setData(JSONObject data) {
       this.data = data;
   }

   
    public Form getForm() {
        return this.form ;
    }

    public void setForm(Form form) {
        this.form = form;
    }

   /**
    * @return the formId
    */
   public String getFormId() {
       return formId;
   }

   /**
    * @param formId the formId to set
    */
   public void setFormId(String formId) {
       this.formId = formId;
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

    public void setCreationDate(Instant date) {
        this.creationDate = date;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }
    
    public Instant getScreeningDate() {
        return this.screeningDate;
    }

    public void setScreeningDate(Instant set) {
        this.screeningDate = set;
    }

    public void setOrganizationId(String set) {
        this.organizationId = set;
    }

    public String getOrganizationId() {
        return this.organizationId;
    }

    public void setSubmissionReferenceId(String set) {
        this.submissionReferenceId = set;
    }

    public String getSubmissionReferenceId() {
        return this.submissionReferenceId;
    }

    public void setScreeningData(JSONObject set) {
        this.screeningData = set;
    }

    public JSONObject getScreeningData() {
        return this.screeningData;
    }    
    
    public void setScreeningDataHash(String set) {
        this.screeningDataHash = set;
    }

    public String getScreeningDataHash() {
        return this.screeningDataHash;
    }    

    public SubmittedFormDTO getFormReference() {
        return this.formReference;
    }

    public void setFormReference(SubmittedFormDTO form) {
        this.formReference = form;
    }

    public boolean getIsScreeningForm() {
        return this.isScreeningForm;
    }

    public void setIsScreeningForm(boolean isScreening) {
        this.isScreeningForm = isScreening;
    }

    public boolean getIsRaw() {
        return this.isRaw;
    }
    
    public void setIsRaw(boolean isRaw) {
        this.isRaw = isRaw;
    }

    public String getScreeningType() {
        return this.screeningType;
    }

    public void setScreeningType(String screeningType) {
        this.screeningType = screeningType;
    }
}
