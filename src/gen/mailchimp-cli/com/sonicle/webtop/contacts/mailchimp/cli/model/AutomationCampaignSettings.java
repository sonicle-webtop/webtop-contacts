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
/**
 * The settings for the Automation workflow.
 */
@Schema(description = "The settings for the Automation workflow.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class AutomationCampaignSettings {
  @JsonProperty("from_name")
  private String fromName = null;

  @JsonProperty("reply_to")
  private String replyTo = null;

  public AutomationCampaignSettings fromName(String fromName) {
    this.fromName = fromName;
    return this;
  }

   /**
   * The &#x27;from&#x27; name for the Automation (not an email address).
   * @return fromName
  **/
  @Schema(description = "The 'from' name for the Automation (not an email address).")
  public String getFromName() {
    return fromName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public AutomationCampaignSettings replyTo(String replyTo) {
    this.replyTo = replyTo;
    return this;
  }

   /**
   * The reply-to email address for the Automation.
   * @return replyTo
  **/
  @Schema(description = "The reply-to email address for the Automation.")
  public String getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(String replyTo) {
    this.replyTo = replyTo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutomationCampaignSettings automationCampaignSettings = (AutomationCampaignSettings) o;
    return Objects.equals(this.fromName, automationCampaignSettings.fromName) &&
        Objects.equals(this.replyTo, automationCampaignSettings.replyTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromName, replyTo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutomationCampaignSettings {\n");
    
    sb.append("    fromName: ").append(toIndentedString(fromName)).append("\n");
    sb.append("    replyTo: ").append(toIndentedString(replyTo)).append("\n");
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