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
 * BatchAddremoveListMembersTofromStaticSegmentErrors
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class BatchAddremoveListMembersTofromStaticSegmentErrors {
  @JsonProperty("email_addresses")
  private List<String> emailAddresses = null;

  @JsonProperty("error")
  private String error = null;

  public BatchAddremoveListMembersTofromStaticSegmentErrors emailAddresses(List<String> emailAddresses) {
    this.emailAddresses = emailAddresses;
    return this;
  }

  public BatchAddremoveListMembersTofromStaticSegmentErrors addEmailAddressesItem(String emailAddressesItem) {
    if (this.emailAddresses == null) {
      this.emailAddresses = new ArrayList<>();
    }
    this.emailAddresses.add(emailAddressesItem);
    return this;
  }

   /**
   * Email addresses added to the static segment or removed
   * @return emailAddresses
  **/
  @Schema(description = "Email addresses added to the static segment or removed")
  public List<String> getEmailAddresses() {
    return emailAddresses;
  }

  public void setEmailAddresses(List<String> emailAddresses) {
    this.emailAddresses = emailAddresses;
  }

  public BatchAddremoveListMembersTofromStaticSegmentErrors error(String error) {
    this.error = error;
    return this;
  }

   /**
   * The error message indicating why the email addresses could not be added or updated.
   * @return error
  **/
  @Schema(description = "The error message indicating why the email addresses could not be added or updated.")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BatchAddremoveListMembersTofromStaticSegmentErrors batchAddremoveListMembersTofromStaticSegmentErrors = (BatchAddremoveListMembersTofromStaticSegmentErrors) o;
    return Objects.equals(this.emailAddresses, batchAddremoveListMembersTofromStaticSegmentErrors.emailAddresses) &&
        Objects.equals(this.error, batchAddremoveListMembersTofromStaticSegmentErrors.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(emailAddresses, error);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BatchAddremoveListMembersTofromStaticSegmentErrors {\n");
    
    sb.append("    emailAddresses: ").append(toIndentedString(emailAddresses)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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
