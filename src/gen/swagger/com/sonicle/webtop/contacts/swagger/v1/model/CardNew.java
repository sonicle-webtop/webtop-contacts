package com.sonicle.webtop.contacts.swagger.v1.model;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import javax.validation.Valid;


/**
 * Bean for carry new card fields
 **/
import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
@ApiModel(description = "Bean for carry new card fields")

public class CardNew   {
  
  private @Valid String href = null;
  private @Valid String vcard = null;

  /**
   * Reference URI
   **/
  public CardNew href(String href) {
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
   * Card data (vCard format)
   **/
  public CardNew vcard(String vcard) {
    this.vcard = vcard;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Card data (vCard format)")
  @JsonProperty("vcard")
  @NotNull
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
    CardNew cardNew = (CardNew) o;
    return Objects.equals(href, cardNew.href) &&
        Objects.equals(vcard, cardNew.vcard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, vcard);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CardNew {\n");
    
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

