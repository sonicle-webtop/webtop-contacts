package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactEx;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;

import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represent a contact, which is an item for users to organize and save information about the people and organizations that they communicate with. Contacts are contained in contact categories.
 **/
@ApiModel(description = "Represent a contact, which is an item for users to organize and save information about the people and organizations that they communicate with. Contacts are contained in contact categories.")
@JsonTypeName("Contact")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-06T10:04:09.242+01:00[Europe/Berlin]")
public class ApiContact extends ApiContactEx  {
  private @Valid String id;
  private @Valid String etag;
  private @Valid String createdOn;
  private @Valid String updatedOn;

  /**
   * The contact&#39;s unique ID.
   **/
  public ApiContact id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's unique ID.")
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   * The revision identifier that refers to the last modification.
   **/
  public ApiContact etag(String etag) {
    this.etag = etag;
    return this;
  }

  
  @ApiModelProperty(value = "The revision identifier that refers to the last modification.")
  @JsonProperty("etag")
  public String getEtag() {
    return etag;
  }

  @JsonProperty("etag")
  public void setEtag(String etag) {
    this.etag = etag;
  }

  /**
   * Creation timestamp in ISO 8601 format and UTC time.
   **/
  public ApiContact createdOn(String createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  
  @ApiModelProperty(value = "Creation timestamp in ISO 8601 format and UTC time.")
  @JsonProperty("createdOn")
  public String getCreatedOn() {
    return createdOn;
  }

  @JsonProperty("createdOn")
  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  /**
   * Modification timestamp in ISO 8601 format and UTC time.
   **/
  public ApiContact updatedOn(String updatedOn) {
    this.updatedOn = updatedOn;
    return this;
  }

  
  @ApiModelProperty(value = "Modification timestamp in ISO 8601 format and UTC time.")
  @JsonProperty("updatedOn")
  public String getUpdatedOn() {
    return updatedOn;
  }

  @JsonProperty("updatedOn")
  public void setUpdatedOn(String updatedOn) {
    this.updatedOn = updatedOn;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiContact contact = (ApiContact) o;
    return Objects.equals(this.id, contact.id) &&
        Objects.equals(this.etag, contact.etag) &&
        Objects.equals(this.createdOn, contact.createdOn) &&
        Objects.equals(this.updatedOn, contact.updatedOn) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, etag, createdOn, updatedOn, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiContact {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
    sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
    sb.append("    updatedOn: ").append(toIndentedString(updatedOn)).append("\n");
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
