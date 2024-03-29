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
import com.sonicle.webtop.contacts.mailchimp.cli.model.AutomationDelay1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AutomationTrigger1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignReportSummary3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignSettings5;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignSocialCard;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignTrackingOptions1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List11;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * A summary of an individual Automation workflow email.
 */
@Schema(description = "A summary of an individual Automation workflow email.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class AutomationWorkflowEmail {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("web_id")
  private Integer webId = null;

  @JsonProperty("workflow_id")
  private String workflowId = null;

  @JsonProperty("position")
  private Integer position = null;

  @JsonProperty("delay")
  private AutomationDelay1 delay = null;

  @JsonProperty("create_time")
  private DateTime createTime = null;

  @JsonProperty("start_time")
  private DateTime startTime = null;

  @JsonProperty("archive_url")
  private String archiveUrl = null;

  /**
   * The current status of the campaign.
   */
  public enum StatusEnum {
    SAVE("save"),
    PAUSED("paused"),
    SENDING("sending");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("emails_sent")
  private Integer emailsSent = null;

  @JsonProperty("send_time")
  private DateTime sendTime = null;

  @JsonProperty("content_type")
  private String contentType = null;

  @JsonProperty("needs_block_refresh")
  private Boolean needsBlockRefresh = null;

  @JsonProperty("has_logo_merge_tag")
  private Boolean hasLogoMergeTag = null;

  @JsonProperty("recipients")
  private List11 recipients = null;

  @JsonProperty("settings")
  private CampaignSettings5 settings = null;

  @JsonProperty("tracking")
  private CampaignTrackingOptions1 tracking = null;

  @JsonProperty("social_card")
  private CampaignSocialCard socialCard = null;

  @JsonProperty("trigger_settings")
  private AutomationTrigger1 triggerSettings = null;

  @JsonProperty("report_summary")
  private CampaignReportSummary3 reportSummary = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * A string that uniquely identifies the Automation email.
   * @return id
  **/
  @Schema(description = "A string that uniquely identifies the Automation email.")
  public String getId() {
    return id;
  }

   /**
   * The ID used in the Mailchimp web application. View this automation in your Mailchimp account at &#x60;https://{dc}.admin.mailchimp.com/campaigns/show/?id&#x3D;{web_id}&#x60;.
   * @return webId
  **/
  @Schema(example = "33345484", description = "The ID used in the Mailchimp web application. View this automation in your Mailchimp account at `https://{dc}.admin.mailchimp.com/campaigns/show/?id={web_id}`.")
  public Integer getWebId() {
    return webId;
  }

   /**
   * A string that uniquely identifies an Automation workflow.
   * @return workflowId
  **/
  @Schema(description = "A string that uniquely identifies an Automation workflow.")
  public String getWorkflowId() {
    return workflowId;
  }

   /**
   * The position of an Automation email in a workflow.
   * @return position
  **/
  @Schema(description = "The position of an Automation email in a workflow.")
  public Integer getPosition() {
    return position;
  }

  public AutomationWorkflowEmail delay(AutomationDelay1 delay) {
    this.delay = delay;
    return this;
  }

   /**
   * Get delay
   * @return delay
  **/
  @Schema(description = "")
  public AutomationDelay1 getDelay() {
    return delay;
  }

  public void setDelay(AutomationDelay1 delay) {
    this.delay = delay;
  }

   /**
   * The date and time the campaign was created in ISO 8601 format.
   * @return createTime
  **/
  @Schema(description = "The date and time the campaign was created in ISO 8601 format.")
  public DateTime getCreateTime() {
    return createTime;
  }

   /**
   * The date and time the campaign was started in ISO 8601 format.
   * @return startTime
  **/
  @Schema(description = "The date and time the campaign was started in ISO 8601 format.")
  public DateTime getStartTime() {
    return startTime;
  }

   /**
   * The link to the campaign&#x27;s archive version in ISO 8601 format.
   * @return archiveUrl
  **/
  @Schema(description = "The link to the campaign's archive version in ISO 8601 format.")
  public String getArchiveUrl() {
    return archiveUrl;
  }

   /**
   * The current status of the campaign.
   * @return status
  **/
  @Schema(description = "The current status of the campaign.")
  public StatusEnum getStatus() {
    return status;
  }

   /**
   * The total number of emails sent for this campaign.
   * @return emailsSent
  **/
  @Schema(description = "The total number of emails sent for this campaign.")
  public Integer getEmailsSent() {
    return emailsSent;
  }

   /**
   *  The date and time a campaign was sent in ISO 8601 format
   * @return sendTime
  **/
  @Schema(description = " The date and time a campaign was sent in ISO 8601 format")
  public DateTime getSendTime() {
    return sendTime;
  }

   /**
   * How the campaign&#x27;s content is put together (&#x27;template&#x27;, &#x27;drag_and_drop&#x27;, &#x27;html&#x27;, &#x27;url&#x27;).
   * @return contentType
  **/
  @Schema(description = "How the campaign's content is put together ('template', 'drag_and_drop', 'html', 'url').")
  public String getContentType() {
    return contentType;
  }

   /**
   * Determines if the automation email needs its blocks refreshed by opening the web-based campaign editor.
   * @return needsBlockRefresh
  **/
  @Schema(example = "true", description = "Determines if the automation email needs its blocks refreshed by opening the web-based campaign editor.")
  public Boolean isNeedsBlockRefresh() {
    return needsBlockRefresh;
  }

   /**
   * Determines if the campaign contains the *|BRAND:LOGO|* merge tag.
   * @return hasLogoMergeTag
  **/
  @Schema(example = "true", description = "Determines if the campaign contains the *|BRAND:LOGO|* merge tag.")
  public Boolean isHasLogoMergeTag() {
    return hasLogoMergeTag;
  }

  public AutomationWorkflowEmail recipients(List11 recipients) {
    this.recipients = recipients;
    return this;
  }

   /**
   * Get recipients
   * @return recipients
  **/
  @Schema(description = "")
  public List11 getRecipients() {
    return recipients;
  }

  public void setRecipients(List11 recipients) {
    this.recipients = recipients;
  }

  public AutomationWorkflowEmail settings(CampaignSettings5 settings) {
    this.settings = settings;
    return this;
  }

   /**
   * Get settings
   * @return settings
  **/
  @Schema(description = "")
  public CampaignSettings5 getSettings() {
    return settings;
  }

  public void setSettings(CampaignSettings5 settings) {
    this.settings = settings;
  }

  public AutomationWorkflowEmail tracking(CampaignTrackingOptions1 tracking) {
    this.tracking = tracking;
    return this;
  }

   /**
   * Get tracking
   * @return tracking
  **/
  @Schema(description = "")
  public CampaignTrackingOptions1 getTracking() {
    return tracking;
  }

  public void setTracking(CampaignTrackingOptions1 tracking) {
    this.tracking = tracking;
  }

  public AutomationWorkflowEmail socialCard(CampaignSocialCard socialCard) {
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

  public AutomationWorkflowEmail triggerSettings(AutomationTrigger1 triggerSettings) {
    this.triggerSettings = triggerSettings;
    return this;
  }

   /**
   * Get triggerSettings
   * @return triggerSettings
  **/
  @Schema(description = "")
  public AutomationTrigger1 getTriggerSettings() {
    return triggerSettings;
  }

  public void setTriggerSettings(AutomationTrigger1 triggerSettings) {
    this.triggerSettings = triggerSettings;
  }

  public AutomationWorkflowEmail reportSummary(CampaignReportSummary3 reportSummary) {
    this.reportSummary = reportSummary;
    return this;
  }

   /**
   * Get reportSummary
   * @return reportSummary
  **/
  @Schema(description = "")
  public CampaignReportSummary3 getReportSummary() {
    return reportSummary;
  }

  public void setReportSummary(CampaignReportSummary3 reportSummary) {
    this.reportSummary = reportSummary;
  }

   /**
   * A list of link types and descriptions for the API schema documents.
   * @return _links
  **/
  @Schema(description = "A list of link types and descriptions for the API schema documents.")
  public List<ResourceLink> getLinks() {
    return _links;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutomationWorkflowEmail automationWorkflowEmail = (AutomationWorkflowEmail) o;
    return Objects.equals(this.id, automationWorkflowEmail.id) &&
        Objects.equals(this.webId, automationWorkflowEmail.webId) &&
        Objects.equals(this.workflowId, automationWorkflowEmail.workflowId) &&
        Objects.equals(this.position, automationWorkflowEmail.position) &&
        Objects.equals(this.delay, automationWorkflowEmail.delay) &&
        Objects.equals(this.createTime, automationWorkflowEmail.createTime) &&
        Objects.equals(this.startTime, automationWorkflowEmail.startTime) &&
        Objects.equals(this.archiveUrl, automationWorkflowEmail.archiveUrl) &&
        Objects.equals(this.status, automationWorkflowEmail.status) &&
        Objects.equals(this.emailsSent, automationWorkflowEmail.emailsSent) &&
        Objects.equals(this.sendTime, automationWorkflowEmail.sendTime) &&
        Objects.equals(this.contentType, automationWorkflowEmail.contentType) &&
        Objects.equals(this.needsBlockRefresh, automationWorkflowEmail.needsBlockRefresh) &&
        Objects.equals(this.hasLogoMergeTag, automationWorkflowEmail.hasLogoMergeTag) &&
        Objects.equals(this.recipients, automationWorkflowEmail.recipients) &&
        Objects.equals(this.settings, automationWorkflowEmail.settings) &&
        Objects.equals(this.tracking, automationWorkflowEmail.tracking) &&
        Objects.equals(this.socialCard, automationWorkflowEmail.socialCard) &&
        Objects.equals(this.triggerSettings, automationWorkflowEmail.triggerSettings) &&
        Objects.equals(this.reportSummary, automationWorkflowEmail.reportSummary) &&
        Objects.equals(this._links, automationWorkflowEmail._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, webId, workflowId, position, delay, createTime, startTime, archiveUrl, status, emailsSent, sendTime, contentType, needsBlockRefresh, hasLogoMergeTag, recipients, settings, tracking, socialCard, triggerSettings, reportSummary, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutomationWorkflowEmail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    webId: ").append(toIndentedString(webId)).append("\n");
    sb.append("    workflowId: ").append(toIndentedString(workflowId)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    delay: ").append(toIndentedString(delay)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    archiveUrl: ").append(toIndentedString(archiveUrl)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    emailsSent: ").append(toIndentedString(emailsSent)).append("\n");
    sb.append("    sendTime: ").append(toIndentedString(sendTime)).append("\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    needsBlockRefresh: ").append(toIndentedString(needsBlockRefresh)).append("\n");
    sb.append("    hasLogoMergeTag: ").append(toIndentedString(hasLogoMergeTag)).append("\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    tracking: ").append(toIndentedString(tracking)).append("\n");
    sb.append("    socialCard: ").append(toIndentedString(socialCard)).append("\n");
    sb.append("    triggerSettings: ").append(toIndentedString(triggerSettings)).append("\n");
    sb.append("    reportSummary: ").append(toIndentedString(reportSummary)).append("\n");
    sb.append("    _links: ").append(toIndentedString(_links)).append("\n");
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
