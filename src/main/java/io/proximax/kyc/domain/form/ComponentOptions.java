package io.proximax.kyc.domain.form;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class ComponentOptions<T,V> implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   private String id;

   @Field("defaultValue")
   private T defaultValue;

   @Field("type")
   private String type;

   @Field("key")
   private String key;

   @Field("label")
   private String label;

   @Field("input")
   private boolean input;

   @Field("required")
   private boolean required;

   @Field("multiple")
   private boolean multiple;

   @Field("protected")
   private boolean isProtected;

   @Field("unique")
   private boolean unique;

   @Field("persistent")
   private boolean persistent;

   @Field("tableView")
   private boolean tableView;

   @Field("lockKey")
   private boolean lockKey;

   @Field("validate")
   private V validate;

   @Field("conditional")
   private ConditionalOptions conditional;

   @Field("customConditional")
   private String customConditional;

   public void setId(String value) {
      this.id = value;
   }
   public String getId() {
      return this.id;
   }

   public void setDefaultValue(T value) {
      this.defaultValue = value;
   }
   public T getDefaultValue() {
      return this.defaultValue;
   }

   public void setType(String value) {
      this.type = value;
   }
   public String getType() {
      return this.type;
   }

   public void setKey(String value) {
      this.key = value;
   }

   public String getKey() {
      return this.key;
   }

   public void setLabel(String value) {
      this.label = value;
   }
   public String getLabel() {
      return this.label;
   }

   public void setInput(boolean value) {
      this.input = value;
   }
   public boolean getInput() {
      return this.input;
   }

   public void setRequired(boolean value) {
      this.required = value;
   }
   public boolean getRequired() {
      return this.required;
   }

   public void setMultiple(boolean value) {
      this.multiple = value;
   }
   public boolean getMultiple() {
      return this.multiple;
   }

   public void setIsProtected(boolean value) {
      this.isProtected = value;
   }
   public boolean getIsProtected() {
      return this.isProtected;
   }

   public void setUnique(boolean value) {
      this.unique = value;
   }
   public boolean getUnique() {
      return this.unique;
   }

   public void setPersistent(boolean value) {
      this.persistent = value;
   }
   public boolean getPersistent() {
      return this.persistent;
   }

   public void setTableView(boolean value) {
      this.tableView = value;
   }
   public boolean getTableView() {
      return this.tableView;
   }

   public void setLockKey(boolean value) {
      this.lockKey = value;
   }
   public boolean getLockKey() {
      return this.lockKey;
   }

   public V getValidate() {
      return this.validate;
   }
   public void setValidate(V value) {
      this.validate = value;
   }

   public ConditionalOptions getConditional() {
      return this.conditional;
   }
   public void setConditional(ConditionalOptions value) {
      this.conditional = value;
   }

   public String getCustomConditional() {
      return this.customConditional;
   }
   public void setCustomConditional(String value) {
      this.customConditional = value;
   }
}
