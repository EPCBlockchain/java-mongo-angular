package io.proximax.kyc.domain.form;
import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

public class ConditionalOptions implements Serializable {

   private static final long serialVersionUID = 1L;

   @Field("show")
   private boolean show;

   @Field("when")
   private String when;

   @Field("eq")
   private String eq;

   public void setShow(boolean value) {
      this.show = value;
   }

   public boolean getShow() {
      return this.show;
   }

   public String getEq() {
      return this.eq;
   }

   public void setConditional(String value) {
      this.eq = value;
   }

   public void setWhen(String value) {
      this.when = value;
   }

   public String getWhen() {
      return this.when;
   }

}
