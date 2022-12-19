package io.proximax.kyc.domain.form;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class ValidateOptions implements Serializable {

   private static final long serialVersionUID = 1L;

   @Field("required")
   private boolean required;
   @Field("custom")
   private String custom;
   @Field("customPrivate")
   private boolean customPrivate;

   public void setRequired(boolean value) {
      this.required = value;
   }

   public boolean getRequired() {
      return this.required;
   }

   public void setCustom(String value) {
      this.custom = value;
   }

   public String getCustom() {
      return this.custom;
   }

   public void setCustomPrivate(boolean value) {
      this.customPrivate = value;
   }

   public boolean getCustomPrivate() {
      return this.customPrivate;
   }
}
