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
import com.sonicle.webtop.contacts.mailchimp.cli.model.ABTestOptions;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignSettings2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignSocialCard;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignTrackingOptions;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.RSSOptions1;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * A summary of an individual campaign&#x27;s settings and content.
 */
@Schema(description = "A summary of an individual campaign's settings and content.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class Campaign2 {
  @JsonProperty("recipients")
  private List2 recipients = null;

  @JsonProperty("settings")
  private CampaignSettings2 settings = null;

  @JsonProperty("variate_settings")
  private ABTestOptions variateSettings = null;

  @JsonProperty("tracking")
  private CampaignTrackingOptions tracking = null;

  @JsonProperty("rss_opts")
  private RSSOptions1 rssOpts = null;

  @JsonProperty("social_card")
  private CampaignSocialCard socialCard = null;

  public Campaign2 recipients(List2 recipients) {
    this.recipients = recipients;
    return this;
  }

   /**
   * Get recipients
   * @return recipients
  **/
  @Schema(description = "")
  public List2 getRecipients() {
    return recipients;
  }

  public void setRecipients(List2 recipients) {
    this.recipients = recipients;
  }

  public Campaign2 settings(CampaignSettings2 settings) {
    this.settings = settings;
    return this;
  }

   /**
   * Get settings
   * @return settings
  **/
  @Schema(required = true, description = "")
  public CampaignSettings2 getSettings() {
    return settings;
  }

  public void setSettings(CampaignSettings2 settings) {
    this.settings = settings;
  }

  public Campaign2 variateSettings(ABTestOptions variateSettings) {
    this.variateSettings = variateSettings;
    return this;
  }

   /**
   * Get variateSettings
   * @return variateSettings
  **/
  @Schema(description = "")
  public ABTestOptions getVariateSettings() {
    return variateSettings;
  }

  public void setVariateSettings(ABTestOptions variateSettings) {
    this.variateSettings = variateSettings;
  }

  public Campaign2 tracking(CampaignTrackingOptions tracking) {
    this.tracking = tracking;
    return this;
  }

   /**
   * Get tracking
   * @return tracking
  **/
  @Schema(description = "")
  public CampaignTrackingOptions getTracking() {
    return tracking;
  }

  public void setTracking(CampaignTrackingOptions tracking) {
    this.tracking = tracking;
  }

  public Campaign2 rssOpts(RSSOptions1 rssOpts) {
    this.rssOpts = rssOpts;
    return this;
  }

   /**
   * Get rssOpts
   * @return rssOpts
  **/
  @Schema(description = "")
  public RSSOptions1 getRssOpts() {
    return rssOpts;
  }

  public void setRssOpts(RSSOptions1 rssOpts) {
    this.rssOpts = rssOpts;
  }

  public Campaign2 socialCard(CampaignSocialCard socialCard) {
    this.socialCard = socialCard;
    return this;
  }

   /**
   * Get socialCard
   * @return socialCard
  **/
  @Schema(description = "")
  public CampaignSocialCard getSocialCard() {
    return socialCard;
  }

  public void setSocialCard(CampaignSocialCard socialCard) {
    this.socialCard = socialCard;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Campaign2 campaign2 = (Campaign2) o;
    return Objects.equals(this.recipients, campaign2.recipients) &&
        Objects.equals(this.settings, campaign2.settings) &&
        Objects.equals(this.variateSettings, campaign2.variateSettings) &&
        Objects.equals(this.tracking, campaign2.tracking) &&
        Objects.equals(this.rssOpts, campaign2.rssOpts) &&
        Objects.equals(this.socialCard, campaign2.socialCard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipients, settings, variateSettings, tracking, rssOpts, socialCard);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Campaign2 {\n");
    
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    variateSettings: ").append(toIndentedString(variateSettings)).append("\n");
    sb.append("    tracking: ").append(toIndentedString(tracking)).append("\n");
    sb.append("    rssOpts: ").append(toIndentedString(rssOpts)).append("\n");
    sb.append("    socialCard: ").append(toIndentedString(socialCard)).append("\n");
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