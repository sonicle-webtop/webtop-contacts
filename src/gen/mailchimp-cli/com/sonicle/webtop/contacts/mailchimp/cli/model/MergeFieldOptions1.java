/*
 * Mailchimp Marketing API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 3.0.55
 * Contact: apihelp@mailchimp.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.sonicle.webtop.contacts.mailchimp.cli.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * Extra options for some merge field types.
 */
@Schema(description = "Extra options for some merge field types.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class MergeFieldOptions1 {
  @JsonProperty("default_country")
  private Integer defaultCountry = null;

  @JsonProperty("phone_format")
  private String phoneFormat = null;

  @JsonProperty("date_format")
  private String dateFormat = null;

  @JsonProperty("choices")
  private List<String> choices = null;

  public MergeFieldOptions1 defaultCountry(Integer defaultCountry) {
    this.defaultCountry = defaultCountry;
    return this;
  }

   /**
   * In an address field, the default country code if none supplied.
   * @return defaultCountry
  **/
  @Schema(description = "In an address field, the default country code if none supplied.")
  public Integer getDefaultCountry() {
    return defaultCountry;
  }

  public void setDefaultCountry(Integer defaultCountry) {
    this.defaultCountry = defaultCountry;
  }

  public MergeFieldOptions1 phoneFormat(String phoneFormat) {
    this.phoneFormat = phoneFormat;
    return this;
  }

   /**
   * In a phone field, the phone number type: US or International.
   * @return phoneFormat
  **/
  @Schema(description = "In a phone field, the phone number type: US or International.")
  public String getPhoneFormat() {
    return phoneFormat;
  }

  public void setPhoneFormat(String phoneFormat) {
    this.phoneFormat = phoneFormat;
  }

  public MergeFieldOptions1 dateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
    return this;
  }

   /**
   * In a date or birthday field, the format of the date.
   * @return dateFormat
  **/
  @Schema(description = "In a date or birthday field, the format of the date.")
  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public MergeFieldOptions1 choices(List<String> choices) {
    this.choices = choices;
    return this;
  }

  public MergeFieldOptions1 addChoicesItem(String choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

   /**
   * In a radio or dropdown non-group field, the available options for members to pick from.
   * @return choices
  **/
  @Schema(example = "[\"First Choice\",\"Second Choice\",\"Third Choice\"]", description = "In a radio or dropdown non-group field, the available options for members to pick from.")
  public List<String> getChoices() {
    return choices;
  }

  public void setChoices(List<String> choices) {
    this.choices = choices;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MergeFieldOptions1 mergeFieldOptions1 = (MergeFieldOptions1) o;
    return Objects.equals(this.defaultCountry, mergeFieldOptions1.defaultCountry) &&
        Objects.equals(this.phoneFormat, mergeFieldOptions1.phoneFormat) &&
        Objects.equals(this.dateFormat, mergeFieldOptions1.dateFormat) &&
        Objects.equals(this.choices, mergeFieldOptions1.choices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultCountry, phoneFormat, dateFormat, choices);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MergeFieldOptions1 {\n");
    
    sb.append("    defaultCountry: ").append(toIndentedString(defaultCountry)).append("\n");
    sb.append("    phoneFormat: ").append(toIndentedString(phoneFormat)).append("\n");
    sb.append("    dateFormat: ").append(toIndentedString(dateFormat)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
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