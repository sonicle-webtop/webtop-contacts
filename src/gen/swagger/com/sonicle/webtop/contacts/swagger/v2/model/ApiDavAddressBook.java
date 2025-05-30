package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Bean for carry addressbook&#39;s fields
 **/
@ApiModel(description = "Bean for carry addressbook's fields")
@JsonTypeName("DavAddressBook")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-05-30T11:31:06.563+02:00[Europe/Berlin]")
public class ApiDavAddressBook   {
  private @Valid String id;
  private @Valid String uid;
  private @Valid String displayName;
  private @Valid String description;
  private @Valid String syncToken;
  private @Valid String aclFol;
  private @Valid String aclEle;
  private @Valid String ownerUsername;

  /**
   * AddressBook ID (internal)
   **/
  public ApiDavAddressBook id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "AddressBook ID (internal)")
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   * AddressBook UID (public)
   **/
  public ApiDavAddressBook uid(String uid) {
    this.uid = uid;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "AddressBook UID (public)")
  @JsonProperty("uid")
  @NotNull
  public String getUid() {
    return uid;
  }

  @JsonProperty("uid")
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * Display name
   **/
  public ApiDavAddressBook displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Display name")
  @JsonProperty("displayName")
  @NotNull
  public String getDisplayName() {
    return displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Description
   **/
  public ApiDavAddressBook description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(value = "Description")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Current sync token
   **/
  public ApiDavAddressBook syncToken(String syncToken) {
    this.syncToken = syncToken;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Current sync token")
  @JsonProperty("syncToken")
  @NotNull
  public String getSyncToken() {
    return syncToken;
  }

  @JsonProperty("syncToken")
  public void setSyncToken(String syncToken) {
    this.syncToken = syncToken;
  }

  /**
   * ACL info for folder itself
   **/
  public ApiDavAddressBook aclFol(String aclFol) {
    this.aclFol = aclFol;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "ACL info for folder itself")
  @JsonProperty("aclFol")
  @NotNull
  public String getAclFol() {
    return aclFol;
  }

  @JsonProperty("aclFol")
  public void setAclFol(String aclFol) {
    this.aclFol = aclFol;
  }

  /**
   * ACL info for folder elements
   **/
  public ApiDavAddressBook aclEle(String aclEle) {
    this.aclEle = aclEle;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "ACL info for folder elements")
  @JsonProperty("aclEle")
  @NotNull
  public String getAclEle() {
    return aclEle;
  }

  @JsonProperty("aclEle")
  public void setAclEle(String aclEle) {
    this.aclEle = aclEle;
  }

  /**
   * The owner profile&#39;s username
   **/
  public ApiDavAddressBook ownerUsername(String ownerUsername) {
    this.ownerUsername = ownerUsername;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "The owner profile's username")
  @JsonProperty("ownerUsername")
  @NotNull
  public String getOwnerUsername() {
    return ownerUsername;
  }

  @JsonProperty("ownerUsername")
  public void setOwnerUsername(String ownerUsername) {
    this.ownerUsername = ownerUsername;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiDavAddressBook davAddressBook = (ApiDavAddressBook) o;
    return Objects.equals(this.id, davAddressBook.id) &&
        Objects.equals(this.uid, davAddressBook.uid) &&
        Objects.equals(this.displayName, davAddressBook.displayName) &&
        Objects.equals(this.description, davAddressBook.description) &&
        Objects.equals(this.syncToken, davAddressBook.syncToken) &&
        Objects.equals(this.aclFol, davAddressBook.aclFol) &&
        Objects.equals(this.aclEle, davAddressBook.aclEle) &&
        Objects.equals(this.ownerUsername, davAddressBook.ownerUsername);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uid, displayName, description, syncToken, aclFol, aclEle, ownerUsername);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiDavAddressBook {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    syncToken: ").append(toIndentedString(syncToken)).append("\n");
    sb.append("    aclFol: ").append(toIndentedString(aclFol)).append("\n");
    sb.append("    aclEle: ").append(toIndentedString(aclEle)).append("\n");
    sb.append("    ownerUsername: ").append(toIndentedString(ownerUsername)).append("\n");
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
