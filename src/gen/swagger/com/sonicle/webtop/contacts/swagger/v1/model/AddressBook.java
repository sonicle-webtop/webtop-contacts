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

  /**
   * Internal unique ID
   **/
  public AddressBook id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Internal unique ID")
  @JsonProperty("id")
  @NotNull
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Public unique ID
   **/
  public AddressBook uid(String uid) {
    this.uid = uid;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Public unique ID")
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
        Objects.equals(syncToken, addressBook.syncToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uid, displayName, description, syncToken);
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

