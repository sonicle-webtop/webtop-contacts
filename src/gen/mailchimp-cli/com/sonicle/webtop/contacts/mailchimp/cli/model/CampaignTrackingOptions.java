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
import com.sonicle.webtop.contacts.mailchimp.cli.model.CapsuleCRMTracking;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SalesforceCRMTracking;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * The tracking options for a campaign.
 */
@Schema(description = "The tracking options for a campaign.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class CampaignTrackingOptions {
  @JsonProperty("opens")
  private Boolean opens = null;

  @JsonProperty("html_clicks")
  private Boolean htmlClicks = null;

  @JsonProperty("text_clicks")
  private Boolean textClicks = null;

  @JsonProperty("goal_tracking")
  private Boolean goalTracking = null;

  @JsonProperty("ecomm360")
  private Boolean ecomm360 = null;

  @JsonProperty("google_analytics")
  private String googleAnalytics = null;

  @JsonProperty("clicktale")
  private String clicktale = null;

  @JsonProperty("salesforce")
  private SalesforceCRMTracking salesforce = null;

  @JsonProperty("capsule")
  private CapsuleCRMTracking capsule = null;

  public CampaignTrackingOptions opens(Boolean opens) {
    this.opens = opens;
    return this;
  }

   /**
   * Whether to [track opens](https://mailchimp.com/help/about-open-tracking/). Defaults to &#x60;true&#x60;. Cannot be set to false for variate campaigns.
   * @return opens
  **/
  @Schema(description = "Whether to [track opens](https://mailchimp.com/help/about-open-tracking/). Defaults to `true`. Cannot be set to false for variate campaigns.")
  public Boolean isOpens() {
    return opens;
  }

  public void setOpens(Boolean opens) {
    this.opens = opens;
  }

  public CampaignTrackingOptions htmlClicks(Boolean htmlClicks) {
    this.htmlClicks = htmlClicks;
    return this;
  }

   /**
   * Whether to [track clicks](https://mailchimp.com/help/enable-and-view-click-tracking/) in the HTML version of the campaign. Defaults to &#x60;true&#x60;. Cannot be set to false for variate campaigns.
   * @return htmlClicks
  **/
  @Schema(description = "Whether to [track clicks](https://mailchimp.com/help/enable-and-view-click-tracking/) in the HTML version of the campaign. Defaults to `true`. Cannot be set to false for variate campaigns.")
  public Boolean isHtmlClicks() {
    return htmlClicks;
  }

  public void setHtmlClicks(Boolean htmlClicks) {
    this.htmlClicks = htmlClicks;
  }

  public CampaignTrackingOptions textClicks(Boolean textClicks) {
    this.textClicks = textClicks;
    return this;
  }

   /**
   * Whether to [track clicks](https://mailchimp.com/help/enable-and-view-click-tracking/) in the plain-text version of the campaign. Defaults to &#x60;true&#x60;. Cannot be set to false for variate campaigns.
   * @return textClicks
  **/
  @Schema(description = "Whether to [track clicks](https://mailchimp.com/help/enable-and-view-click-tracking/) in the plain-text version of the campaign. Defaults to `true`. Cannot be set to false for variate campaigns.")
  public Boolean isTextClicks() {
    return textClicks;
  }

  public void setTextClicks(Boolean textClicks) {
    this.textClicks = textClicks;
  }

  public CampaignTrackingOptions goalTracking(Boolean goalTracking) {
    this.goalTracking = goalTracking;
    return this;
  }

   /**
   * Deprecated
   * @return goalTracking
  **/
  @Schema(description = "Deprecated")
  public Boolean isGoalTracking() {
    return goalTracking;
  }

  public void setGoalTracking(Boolean goalTracking) {
    this.goalTracking = goalTracking;
  }

  public CampaignTrackingOptions ecomm360(Boolean ecomm360) {
    this.ecomm360 = ecomm360;
    return this;
  }

   /**
   * Whether to enable e-commerce tracking.
   * @return ecomm360
  **/
  @Schema(description = "Whether to enable e-commerce tracking.")
  public Boolean isEcomm360() {
    return ecomm360;
  }

  public void setEcomm360(Boolean ecomm360) {
    this.ecomm360 = ecomm360;
  }

  public CampaignTrackingOptions googleAnalytics(String googleAnalytics) {
    this.googleAnalytics = googleAnalytics;
    return this;
  }

   /**
   * The custom slug for [Google Analytics](https://mailchimp.com/help/integrate-google-analytics-with-mailchimp/) tracking (max of 50 bytes).
   * @return googleAnalytics
  **/
  @Schema(description = "The custom slug for [Google Analytics](https://mailchimp.com/help/integrate-google-analytics-with-mailchimp/) tracking (max of 50 bytes).")
  public String getGoogleAnalytics() {
    return googleAnalytics;
  }

  public void setGoogleAnalytics(String googleAnalytics) {
    this.googleAnalytics = googleAnalytics;
  }

  public CampaignTrackingOptions clicktale(String clicktale) {
    this.clicktale = clicktale;
    return this;
  }

   /**
   * The custom slug for [ClickTale](https://mailchimp.com/help/additional-tracking-options-for-campaigns/) tracking (max of 50 bytes).
   * @return clicktale
  **/
  @Schema(description = "The custom slug for [ClickTale](https://mailchimp.com/help/additional-tracking-options-for-campaigns/) tracking (max of 50 bytes).")
  public String getClicktale() {
    return clicktale;
  }

  public void setClicktale(String clicktale) {
    this.clicktale = clicktale;
  }

  public CampaignTrackingOptions salesforce(SalesforceCRMTracking salesforce) {
    this.salesforce = salesforce;
    return this;
  }

   /**
   * Get salesforce
   * @return salesforce
  **/
  @Schema(description = "")
  public SalesforceCRMTracking getSalesforce() {
    return salesforce;
  }

  public void setSalesforce(SalesforceCRMTracking salesforce) {
    this.salesforce = salesforce;
  }

  public CampaignTrackingOptions capsule(CapsuleCRMTracking capsule) {
    this.capsule = capsule;
    return this;
  }

   /**
   * Get capsule
   * @return capsule
  **/
  @Schema(description = "")
  public CapsuleCRMTracking getCapsule() {
    return capsule;
  }

  public void setCapsule(CapsuleCRMTracking capsule) {
    this.capsule = capsule;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CampaignTrackingOptions campaignTrackingOptions = (CampaignTrackingOptions) o;
    return Objects.equals(this.opens, campaignTrackingOptions.opens) &&
        Objects.equals(this.htmlClicks, campaignTrackingOptions.htmlClicks) &&
        Objects.equals(this.textClicks, campaignTrackingOptions.textClicks) &&
        Objects.equals(this.goalTracking, campaignTrackingOptions.goalTracking) &&
        Objects.equals(this.ecomm360, campaignTrackingOptions.ecomm360) &&
        Objects.equals(this.googleAnalytics, campaignTrackingOptions.googleAnalytics) &&
        Objects.equals(this.clicktale, campaignTrackingOptions.clicktale) &&
        Objects.equals(this.salesforce, campaignTrackingOptions.salesforce) &&
        Objects.equals(this.capsule, campaignTrackingOptions.capsule);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opens, htmlClicks, textClicks, goalTracking, ecomm360, googleAnalytics, clicktale, salesforce, capsule);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampaignTrackingOptions {\n");
    
    sb.append("    opens: ").append(toIndentedString(opens)).append("\n");
    sb.append("    htmlClicks: ").append(toIndentedString(htmlClicks)).append("\n");
    sb.append("    textClicks: ").append(toIndentedString(textClicks)).append("\n");
    sb.append("    goalTracking: ").append(toIndentedString(goalTracking)).append("\n");
    sb.append("    ecomm360: ").append(toIndentedString(ecomm360)).append("\n");
    sb.append("    googleAnalytics: ").append(toIndentedString(googleAnalytics)).append("\n");
    sb.append("    clicktale: ").append(toIndentedString(clicktale)).append("\n");
    sb.append("    salesforce: ").append(toIndentedString(salesforce)).append("\n");
    sb.append("    capsule: ").append(toIndentedString(capsule)).append("\n");
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