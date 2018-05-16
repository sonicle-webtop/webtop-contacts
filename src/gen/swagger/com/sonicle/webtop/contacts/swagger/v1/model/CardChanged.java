package com.sonicle.webtop.contacts.swagger.v1.model;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import javax.validation.Valid;


/**
 * Bean for carry fields of card that have been changed
 **/
import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
@ApiModel(description = "Bean for carry fields of card that have been changed")

public class CardChanged   {
  
  private @Valid Integer id = null;
  private @Valid String href = null;
  private @Valid String etag = null;

  /**
   * Internal unique ID
   **/
  public CardChanged id(Integer id) {
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
   * Reference URI
   **/
  public CardChanged href(String href) {
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
   * Revision tag
   **/
  public CardChanged etag(String etag) {
    this.etag = etag;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Revision tag")
  @JsonProperty("etag")
  @NotNull
  public String getEtag() {
    return etag;
  }
  public void setEtag(String etag) {
    this.etag = etag;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardChanged cardChanged = (CardChanged) o;
    return Objects.equals(id, cardChanged.id) &&
        Objects.equals(href, cardChanged.href) &&
        Objects.equals(etag, cardChanged.etag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, etag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CardChanged {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
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

