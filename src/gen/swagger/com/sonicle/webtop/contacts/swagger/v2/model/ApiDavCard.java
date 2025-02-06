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
 * Bean for carry card fields
 **/
@ApiModel(description = "Bean for carry card fields")
@JsonTypeName("DavCard")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-06T10:04:09.242+01:00[Europe/Berlin]")
public class ApiDavCard   {
  private @Valid String id;
  private @Valid String uid;
  private @Valid String href;
  private @Valid Long lastModified;
  private @Valid String etag;
  private @Valid Integer size;
  private @Valid String vcard;

  /**
   * Card ID (internal)
   **/
  public ApiDavCard id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Card ID (internal)")
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
   * Card UID (public)
   **/
  public ApiDavCard uid(String uid) {
    this.uid = uid;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Card UID (public)")
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
   * Reference URI
   **/
  public ApiDavCard href(String href) {
    this.href = href;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Reference URI")
  @JsonProperty("href")
  @NotNull
  public String getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Last modification time (unix timestamp)
   **/
  public ApiDavCard lastModified(Long lastModified) {
    this.lastModified = lastModified;
    return this;
  }

  
  @ApiModelProperty(value = "Last modification time (unix timestamp)")
  @JsonProperty("lastModified")
  public Long getLastModified() {
    return lastModified;
  }

  @JsonProperty("lastModified")
  public void setLastModified(Long lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Revision tag
   **/
  public ApiDavCard etag(String etag) {
    this.etag = etag;
    return this;
  }

  
  @ApiModelProperty(value = "Revision tag")
  @JsonProperty("etag")
  public String getEtag() {
    return etag;
  }

  @JsonProperty("etag")
  public void setEtag(String etag) {
    this.etag = etag;
  }

  /**
   * Size (in bytes) of card data
   **/
  public ApiDavCard size(Integer size) {
    this.size = size;
    return this;
  }

  
  @ApiModelProperty(value = "Size (in bytes) of card data")
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  @JsonProperty("size")
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Card data (vCard format)
   **/
  public ApiDavCard vcard(String vcard) {
    this.vcard = vcard;
    return this;
  }

  
  @ApiModelProperty(value = "Card data (vCard format)")
  @JsonProperty("vcard")
  public String getVcard() {
    return vcard;
  }

  @JsonProperty("vcard")
  public void setVcard(String vcard) {
    this.vcard = vcard;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiDavCard davCard = (ApiDavCard) o;
    return Objects.equals(this.id, davCard.id) &&
        Objects.equals(this.uid, davCard.uid) &&
        Objects.equals(this.href, davCard.href) &&
        Objects.equals(this.lastModified, davCard.lastModified) &&
        Objects.equals(this.etag, davCard.etag) &&
        Objects.equals(this.size, davCard.size) &&
        Objects.equals(this.vcard, davCard.vcard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uid, href, lastModified, etag, size, vcard);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiDavCard {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    lastModified: ").append(toIndentedString(lastModified)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    vcard: ").append(toIndentedString(vcard)).append("\n");
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
