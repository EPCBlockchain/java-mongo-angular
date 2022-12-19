package io.proximax.kyc.service.dto;

import java.time.Instant;

import io.proximax.kyc.domain.SubmittedForm;
import io.proximax.kyc.domain.mongo.Form;

public class SubmittedFormDTO {
   private String id;
   private String status;
   private Instant creationDate;
   private SubmittedFormDTO submissionReference;
   private Object screeningData;
   private Object submissionData;
   private String submissionReferenceId;
   private boolean isScreeningForm;
   private Form form;
   private String screeningType;

   public SubmittedFormDTO() {}

   public SubmittedFormDTO(SubmittedForm form) {
      if (form != null) {
         this.setId(form.getId())
            .setStatus(form.getStatus())
            .setCreationDate(form.getCreationDate())
            .setForm(form.getForm())
            .setSubmissionReferenceId(form.getSubmissionReferenceId())
            .setIsScreeningForm(form.getIsScreeningForm())
            .setScreeningType(form.getScreeningType());
         
         if (form.getDataHash() != null) {
            this.setSubmissionData(form.getData());
         }

         if (form.getFormReference() != null) {            
            this.setSubmissionReference(form.getFormReference());
         }
         
         if (form.getScreeningDataHash() != null) {           
            this.setScreeningData(form.getScreeningData());
         }
      }
   }

   public SubmittedFormDTO setId(String id) {
      this.id = id;
      return this;
   }

   public String getId() {
      return this.id;
   }

   public SubmittedFormDTO setStatus(String status) {
      this.status = status;
      return this;
   }
   
   public String getStatus() {
      return this.status;
   }

   public SubmittedFormDTO setCreationDate(Instant creationDate) {
      this.creationDate = creationDate;
      return this;
   }
   
   public Instant getCreationDate() {
      return this.creationDate;
   }

   public SubmittedFormDTO setSubmissionReference(SubmittedFormDTO submissionReference) {
      this.submissionReference = submissionReference;
      return this;
   }
   
   public SubmittedFormDTO getSubmissionReference() {
      return this.submissionReference;
   }

   public SubmittedFormDTO setScreeningData(Object screeningData) {
      this.screeningData = screeningData;
      return this;
   }
   
   public Object getScreeningData() {
      return this.screeningData;
   }

   public SubmittedFormDTO setSubmissionData(Object data) {
      this.submissionData = data;
      return this;
   }
   
   public Object getSubmissionData() {
      return this.submissionData;
   }

   public SubmittedFormDTO setForm(Form form) {
      this.form = form;
      return this;
   }
   
   public Form getForm() {
      return this.form;
   }

   public SubmittedFormDTO setSubmissionReferenceId(String id) {
      this.submissionReferenceId = id;
      return this;
   }
   
   public String getSubmissionReferenceId() {
      return this.submissionReferenceId;
   }

   public SubmittedFormDTO setIsScreeningForm(boolean isScreeningForm) {
      this.isScreeningForm = isScreeningForm;
      return this;
   }
   
   public boolean getIsScreeningForm() {
      return this.isScreeningForm;
   }

   public SubmittedFormDTO setScreeningType(String screeningType) {
      this.screeningType = screeningType;
      return this;
   }

   public String getScreeningType() {
      return this.screeningType;
   }

}
