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
 * The tracking settings applied to this landing page.
 */
@Schema(description = "The tracking settings applied to this landing page.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class TrackingSettings {
  @JsonProperty("track_with_mailchimp")
  private Boolean trackWithMailchimp = null;

  @JsonProperty("enable_restricted_data_processing")
  private Boolean enableRestrictedDataProcessing = null;

  public TrackingSettings trackWithMailchimp(Boolean trackWithMailchimp) {
    this.trackWithMailchimp = trackWithMailchimp;
    return this;
  }

   /**
   * Use cookies to track unique visitors and calculate overall conversion rate. Learn more [here](https://mailchimp.com/help/use-track-mailchimp/).
   * @return trackWithMailchimp
  **/
  @Schema(description = "Use cookies to track unique visitors and calculate overall conversion rate. Learn more [here](https://mailchimp.com/help/use-track-mailchimp/).")
  public Boolean isTrackWithMailchimp() {
    return trackWithMailchimp;
  }

  public void setTrackWithMailchimp(Boolean trackWithMailchimp) {
    this.trackWithMailchimp = trackWithMailchimp;
  }

  public TrackingSettings enableRestrictedDataProcessing(Boolean enableRestrictedDataProcessing) {
    this.enableRestrictedDataProcessing = enableRestrictedDataProcessing;
    return this;
  }

   /**
   * Google offers restricted data processing in connection with the California Consumer Privacy Act (CCPA) to restrict how Google uses certain identifiers and other data processed in the provision of its services. You can learn more about Google&#x27;s restricted data processing within Google Ads [here](https://privacy.google.com/businesses/rdp/).
   * @return enableRestrictedDataProcessing
  **/
  @Schema(description = "Google offers restricted data processing in connection with the California Consumer Privacy Act (CCPA) to restrict how Google uses certain identifiers and other data processed in the provision of its services. You can learn more about Google's restricted data processing within Google Ads [here](https://privacy.google.com/businesses/rdp/).")
  public Boolean isEnableRestrictedDataProcessing() {
    return enableRestrictedDataProcessing;
  }

  public void setEnableRestrictedDataProcessing(Boolean enableRestrictedDataProcessing) {
    this.enableRestrictedDataProcessing = enableRestrictedDataProcessing;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrackingSettings trackingSettings = (TrackingSettings) o;
    return Objects.equals(this.trackWithMailchimp, trackingSettings.trackWithMailchimp) &&
        Objects.equals(this.enableRestrictedDataProcessing, trackingSettings.enableRestrictedDataProcessing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trackWithMailchimp, enableRestrictedDataProcessing);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrackingSettings {\n");
    
    sb.append("    trackWithMailchimp: ").append(toIndentedString(trackWithMailchimp)).append("\n");
    sb.append("    enableRestrictedDataProcessing: ").append(toIndentedString(enableRestrictedDataProcessing)).append("\n");
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