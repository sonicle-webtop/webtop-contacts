package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiOwnerInfo;
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



@JsonTypeName("Category_allOf")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-05-30T11:31:06.563+02:00[Europe/Berlin]")
public class ApiCategoryAllOf   {
  private @Valid String id;
  private @Valid String etag;
  private @Valid String itemsETag;
  private @Valid String createdAt;
  private @Valid String updatedAt;
  private @Valid ApiOwnerInfo owner;

  /**
   * The category ID.
   **/
  public ApiCategoryAllOf id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(value = "The category ID.")
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
  public ApiCategoryAllOf etag(String etag) {
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
   * The revision identifier that refers to the last modification of the items.
   **/
  public ApiCategoryAllOf itemsETag(String itemsETag) {
    this.itemsETag = itemsETag;
    return this;
  }

  
  @ApiModelProperty(value = "The revision identifier that refers to the last modification of the items.")
  @JsonProperty("itemsETag")
  public String getItemsETag() {
    return itemsETag;
  }

  @JsonProperty("itemsETag")
  public void setItemsETag(String itemsETag) {
    this.itemsETag = itemsETag;
  }

  /**
   * Creation timestamp in ISO 8601 format and UTC time.
   **/
  public ApiCategoryAllOf createdAt(String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  
  @ApiModelProperty(value = "Creation timestamp in ISO 8601 format and UTC time.")
  @JsonProperty("createdAt")
  public String getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Modification timestamp in ISO 8601 format and UTC time.
   **/
  public ApiCategoryAllOf updatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  
  @ApiModelProperty(value = "Modification timestamp in ISO 8601 format and UTC time.")
  @JsonProperty("updatedAt")
  public String getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   **/
  public ApiCategoryAllOf owner(ApiOwnerInfo owner) {
    this.owner = owner;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("owner")
  public ApiOwnerInfo getOwner() {
    return owner;
  }

  @JsonProperty("owner")
  public void setOwner(ApiOwnerInfo owner) {
    this.owner = owner;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiCategoryAllOf categoryAllOf = (ApiCategoryAllOf) o;
    return Objects.equals(this.id, categoryAllOf.id) &&
        Objects.equals(this.etag, categoryAllOf.etag) &&
        Objects.equals(this.itemsETag, categoryAllOf.itemsETag) &&
        Objects.equals(this.createdAt, categoryAllOf.createdAt) &&
        Objects.equals(this.updatedAt, categoryAllOf.updatedAt) &&
        Objects.equals(this.owner, categoryAllOf.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, etag, itemsETag, createdAt, updatedAt, owner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCategoryAllOf {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
    sb.append("    itemsETag: ").append(toIndentedString(itemsETag)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
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
