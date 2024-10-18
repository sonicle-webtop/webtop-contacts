package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Bean for carry addressbook&#39;s updateable fields
 **/
@ApiModel(description = "Bean for carry addressbook's updateable fields")
@JsonTypeName("AddressBookUpdate")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-10-14T17:59:50.160+02:00[Europe/Berlin]")
public class ApiAddressBookUpdate   {
  private @Valid String displayName;
  private @Valid String description;
  private @Valid List<String> updatedFields = null;

  /**
   * New value for displayName
   **/
  public ApiAddressBookUpdate displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  
  @ApiModelProperty(value = "New value for displayName")
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * New value for description
   **/
  public ApiAddressBookUpdate description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(value = "New value for description")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Specifies which fields have been updated
   **/
  public ApiAddressBookUpdate updatedFields(List<String> updatedFields) {
    this.updatedFields = updatedFields;
    return this;
  }

  
  @ApiModelProperty(value = "Specifies which fields have been updated")
  @JsonProperty("updatedFields")
  public List<String> getUpdatedFields() {
    return updatedFields;
  }

  @JsonProperty("updatedFields")
  public void setUpdatedFields(List<String> updatedFields) {
    this.updatedFields = updatedFields;
  }

  public ApiAddressBookUpdate addUpdatedFieldsItem(String updatedFieldsItem) {
    if (this.updatedFields == null) {
      this.updatedFields = new ArrayList<>();
    }

    this.updatedFields.add(updatedFieldsItem);
    return this;
  }

  public ApiAddressBookUpdate removeUpdatedFieldsItem(String updatedFieldsItem) {
    if (updatedFieldsItem != null && this.updatedFields != null) {
      this.updatedFields.remove(updatedFieldsItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiAddressBookUpdate addressBookUpdate = (ApiAddressBookUpdate) o;
    return Objects.equals(this.displayName, addressBookUpdate.displayName) &&
        Objects.equals(this.description, addressBookUpdate.description) &&
        Objects.equals(this.updatedFields, addressBookUpdate.updatedFields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(displayName, description, updatedFields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiAddressBookUpdate {\n");
    
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    updatedFields: ").append(toIndentedString(updatedFields)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

