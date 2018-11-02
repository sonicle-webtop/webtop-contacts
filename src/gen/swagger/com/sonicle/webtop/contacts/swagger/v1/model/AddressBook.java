package com.sonicle.webtop.contacts.swagger.v1.model;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import javax.validation.Valid;


/**
 * Bean for carry addressbook&#39;s fields
 **/
import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
@ApiModel(description = "Bean for carry addressbook's fields")

public class AddressBook   {
  
  private @Valid Integer id = null;
  private @Valid String uid = null;
  private @Valid String displayName = null;
  private @Valid String description = null;
  private @Valid String syncToken = null;
  private @Valid String aclFol = null;
  private @Valid String aclEle = null;
  private @Valid String ownerUsername = null;

  /**
   * AddressBook ID (internal)
   **/
  public AddressBook id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "AddressBook ID (internal)")
  @JsonProperty("id")
  @NotNull
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * AddressBook UID (public)
   **/
  public AddressBook uid(String uid) {
    this.uid = uid;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "AddressBook UID (public)")
  @JsonProperty("uid")
  @NotNull
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * Display name
   **/
  public AddressBook displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Display name")
  @JsonProperty("displayName")
  @NotNull
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Description
   **/
  public AddressBook description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(value = "Description")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Current sync token
   **/
  public AddressBook syncToken(String syncToken) {
    this.syncToken = syncToken;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Current sync token")
  @JsonProperty("syncToken")
  @NotNull
  public String getSyncToken() {
    return syncToken;
  }
  public void setSyncToken(String syncToken) {
    this.syncToken = syncToken;
  }

  /**
   * ACL info for folder itself
   **/
  public AddressBook aclFol(String aclFol) {
    this.aclFol = aclFol;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "ACL info for folder itself")
  @JsonProperty("aclFol")
  @NotNull
  public String getAclFol() {
    return aclFol;
  }
  public void setAclFol(String aclFol) {
    this.aclFol = aclFol;
  }

  /**
   * ACL info for folder elements
   **/
  public AddressBook aclEle(String aclEle) {
    this.aclEle = aclEle;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "ACL info for folder elements")
  @JsonProperty("aclEle")
  @NotNull
  public String getAclEle() {
    return aclEle;
  }
  public void setAclEle(String aclEle) {
    this.aclEle = aclEle;
  }

  /**
   * The owner profile&#39;s username
   **/
  public AddressBook ownerUsername(String ownerUsername) {
    this.ownerUsername = ownerUsername;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "The owner profile's username")
  @JsonProperty("ownerUsername")
  @NotNull
  public String getOwnerUsername() {
    return ownerUsername;
  }
  public void setOwnerUsername(String ownerUsername) {
    this.ownerUsername = ownerUsername;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddressBook addressBook = (AddressBook) o;
    return Objects.equals(id, addressBook.id) &&
        Objects.equals(uid, addressBook.uid) &&
        Objects.equals(displayName, addressBook.displayName) &&
        Objects.equals(description, addressBook.description) &&
        Objects.equals(syncToken, addressBook.syncToken) &&
        Objects.equals(aclFol, addressBook.aclFol) &&
        Objects.equals(aclEle, addressBook.aclEle) &&
        Objects.equals(ownerUsername, addressBook.ownerUsername);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uid, displayName, description, syncToken, aclFol, aclEle, ownerUsername);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddressBook {\n");
    
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

