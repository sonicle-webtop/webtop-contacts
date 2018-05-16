package com.sonicle.webtop.contacts.swagger.v1.model;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import javax.validation.Valid;


/**
 * Bean for carry card fields
 **/
import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
@ApiModel(description = "Bean for carry card fields")

public class Card   {
  
  private @Valid Integer id = null;
  private @Valid String uid = null;
  private @Valid String href = null;
  private @Valid Long lastModified = null;
  private @Valid String etag = null;
  private @Valid Integer size = null;
  private @Valid String vcard = null;

  /**
   * Internal unique ID
   **/
  public Card id(Integer id) {
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
  public Card uid(String uid) {
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
   * Reference URI
   **/
  public Card href(String href) {
    this.href = href;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Reference URI")
  @JsonProperty("href")
  @NotNull
  public String getHref() {
    return href;
  }
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Last modification time (unix timestamp)
   **/
  public Card lastModified(Long lastModified) {
    this.lastModified = lastModified;
    return this;
  }

  
  @ApiModelProperty(value = "Last modification time (unix timestamp)")
  @JsonProperty("lastModified")
  public Long getLastModified() {
    return lastModified;
  }
  public void setLastModified(Long lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Revision tag
   **/
  public Card etag(String etag) {
    this.etag = etag;
    return this;
  }

  
  @ApiModelProperty(value = "Revision tag")
  @JsonProperty("etag")
  public String getEtag() {
    return etag;
  }
  public void setEtag(String etag) {
    this.etag = etag;
  }

  /**
   * Size (in bytes) of card data
   **/
  public Card size(Integer size) {
    this.size = size;
    return this;
  }

  
  @ApiModelProperty(value = "Size (in bytes) of card data")
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Card data (vCard format)
   **/
  public Card vcard(String vcard) {
    this.vcard = vcard;
    return this;
  }

  
  @ApiModelProperty(value = "Card data (vCard format)")
  @JsonProperty("vcard")
  public String getVcard() {
    return vcard;
  }
  public void setVcard(String vcard) {
    this.vcard = vcard;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Card card = (Card) o;
    return Objects.equals(id, card.id) &&
        Objects.equals(uid, card.uid) &&
        Objects.equals(href, card.href) &&
        Objects.equals(lastModified, card.lastModified) &&
        Objects.equals(etag, card.etag) &&
        Objects.equals(size, card.size) &&
        Objects.equals(vcard, card.vcard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uid, href, lastModified, etag, size, vcard);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Card {\n");
    
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

