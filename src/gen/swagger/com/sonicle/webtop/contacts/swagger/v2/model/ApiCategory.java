package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoryBase;
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

/**
 * Represent a complete category object.
 **/
@ApiModel(description = "Represent a complete category object.")
@JsonTypeName("Category")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-01-31T17:20:03.694+01:00[Europe/Berlin]")
public class ApiCategory extends ApiCategoryBase  {
  private @Valid String id;
  private @Valid String etag;
  private @Valid String itemsETag;
  private @Valid String createdOn;
  private @Valid String updatedOn;
  private @Valid ApiOwnerInfo owner;

  /**
   * The category ID.
   **/
  public ApiCategory id(String id) {
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
  public ApiCategory etag(String etag) {
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
  public ApiCategory itemsETag(String itemsETag) {
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
  public ApiCategory createdOn(String createdOn) {
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
  public ApiCategory updatedOn(String updatedOn) {
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

  /**
   **/
  public ApiCategory owner(ApiOwnerInfo owner) {
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
    ApiCategory category = (ApiCategory) o;
    return Objects.equals(this.id, category.id) &&
        Objects.equals(this.etag, category.etag) &&
        Objects.equals(this.itemsETag, category.itemsETag) &&
        Objects.equals(this.createdOn, category.createdOn) &&
        Objects.equals(this.updatedOn, category.updatedOn) &&
        Objects.equals(this.owner, category.owner) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, etag, itemsETag, createdOn, updatedOn, owner, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCategory {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
    sb.append("    itemsETag: ").append(toIndentedString(itemsETag)).append("\n");
    sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
    sb.append("    updatedOn: ").append(toIndentedString(updatedOn)).append("\n");
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
