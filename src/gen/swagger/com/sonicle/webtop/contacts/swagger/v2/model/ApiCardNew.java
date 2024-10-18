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
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Bean for carry new card fields
 **/
@ApiModel(description = "Bean for carry new card fields")
@JsonTypeName("CardNew")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-10-14T17:59:50.160+02:00[Europe/Berlin]")
public class ApiCardNew   {
  private @Valid String href;
  private @Valid String vcard;

  /**
   * Reference URI
   **/
  public ApiCardNew href(String href) {
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
   * Card data (vCard format)
   **/
  public ApiCardNew vcard(String vcard) {
    this.vcard = vcard;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Card data (vCard format)")
  @JsonProperty("vcard")
  @NotNull
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
    ApiCardNew cardNew = (ApiCardNew) o;
    return Objects.equals(this.href, cardNew.href) &&
        Objects.equals(this.vcard, cardNew.vcard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, vcard);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCardNew {\n");
    
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

