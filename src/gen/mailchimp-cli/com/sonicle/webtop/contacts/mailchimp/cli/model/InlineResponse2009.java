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
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009Audience;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009Budget;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009Channel;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009Content;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009Feedback;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009ReportSummary;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009Site;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List10;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * InlineResponse2009
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class InlineResponse2009 {
  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  @JsonProperty("audience")
  private InlineResponse2009Audience audience = null;

  @JsonProperty("budget")
  private InlineResponse2009Budget budget = null;

  @JsonProperty("canceled_at")
  private DateTime canceledAt = null;

  @JsonProperty("channel")
  private InlineResponse2009Channel channel = null;

  @JsonProperty("content")
  private InlineResponse2009Content content = null;

  @JsonProperty("create_time")
  private DateTime createTime = null;

  @JsonProperty("email_source_name")
  private String emailSourceName = null;

  @JsonProperty("end_time")
  private DateTime endTime = null;

  @JsonProperty("feedback")
  private InlineResponse2009Feedback feedback = null;

  @JsonProperty("has_audience")
  private Boolean hasAudience = null;

  @JsonProperty("has_content")
  private Boolean hasContent = null;

  @JsonProperty("has_segment")
  private Boolean hasSegment = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("is_connected")
  private Boolean isConnected = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("needs_attention")
  private Boolean needsAttention = null;

  @JsonProperty("paused_at")
  private DateTime pausedAt = null;

  @JsonProperty("published_time")
  private DateTime publishedTime = null;

  @JsonProperty("recipients")
  private List10 recipients = null;

  @JsonProperty("report_summary")
  private InlineResponse2009ReportSummary reportSummary = null;

  @JsonProperty("show_report")
  private Boolean showReport = null;

  @JsonProperty("site")
  private InlineResponse2009Site site = null;

  @JsonProperty("start_time")
  private DateTime startTime = null;

  /**
   * Campaign, Ad, or Page status
   */
  public enum StatusEnum {
    SAVE("save"),
    PAUSED("paused"),
    SCHEDULE("schedule"),
    SENDING("sending"),
    SENT("sent"),
    CANCELED("canceled"),
    CANCELING("canceling"),
    ACTIVE("active"),
    DISCONNECTED("disconnected"),
    SOMEPAUSED("somepaused"),
    DRAFT("draft"),
    COMPLETED("completed"),
    PARTIALREJECTED("partialRejected"),
    PENDING("pending"),
    REJECTED("rejected"),
    PUBLISHED("published"),
    UNPUBLISHED("unpublished");

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

  @JsonProperty("thumbnail")
  private String thumbnail = null;

  /**
   * Supported Campaign, Ad, Page type
   */
  public enum TypeEnum {
    REGULAR("regular"),
    PLAINTEXT("plaintext"),
    RSS("rss"),
    RECONFIRM("reconfirm"),
    VARIATE("variate"),
    ABSPLIT("absplit"),
    AUTOMATION("automation"),
    FACEBOOK("facebook"),
    GOOGLE("google"),
    AUTORESPONDER("autoresponder"),
    TRANSACTIONAL("transactional"),
    PAGE("page"),
    WEBSITE("website"),
    SURVEY("survey");

    private String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("updated_at")
  private DateTime updatedAt = null;

  @JsonProperty("was_canceled_by_facebook")
  private Boolean wasCanceledByFacebook = null;

  @JsonProperty("web_id")
  private Integer webId = null;

   /**
   * A list of link types and descriptions for the API schema documents.
   * @return _links
  **/
  @Schema(description = "A list of link types and descriptions for the API schema documents.")
  public List<ResourceLink> getLinks() {
    return _links;
  }

  public InlineResponse2009 audience(InlineResponse2009Audience audience) {
    this.audience = audience;
    return this;
  }

   /**
   * Get audience
   * @return audience
  **/
  @Schema(description = "")
  public InlineResponse2009Audience getAudience() {
    return audience;
  }

  public void setAudience(InlineResponse2009Audience audience) {
    this.audience = audience;
  }

  public InlineResponse2009 budget(InlineResponse2009Budget budget) {
    this.budget = budget;
    return this;
  }

   /**
   * Get budget
   * @return budget
  **/
  @Schema(description = "")
  public InlineResponse2009Budget getBudget() {
    return budget;
  }

  public void setBudget(InlineResponse2009Budget budget) {
    this.budget = budget;
  }

  public InlineResponse2009 canceledAt(DateTime canceledAt) {
    this.canceledAt = canceledAt;
    return this;
  }

   /**
   * Get canceledAt
   * @return canceledAt
  **/
  @Schema(description = "")
  public DateTime getCanceledAt() {
    return canceledAt;
  }

  public void setCanceledAt(DateTime canceledAt) {
    this.canceledAt = canceledAt;
  }

  public InlineResponse2009 channel(InlineResponse2009Channel channel) {
    this.channel = channel;
    return this;
  }

   /**
   * Get channel
   * @return channel
  **/
  @Schema(description = "")
  public InlineResponse2009Channel getChannel() {
    return channel;
  }

  public void setChannel(InlineResponse2009Channel channel) {
    this.channel = channel;
  }

  public InlineResponse2009 content(InlineResponse2009Content content) {
    this.content = content;
    return this;
  }

   /**
   * Get content
   * @return content
  **/
  @Schema(description = "")
  public InlineResponse2009Content getContent() {
    return content;
  }

  public void setContent(InlineResponse2009Content content) {
    this.content = content;
  }

  public InlineResponse2009 createTime(DateTime createTime) {
    this.createTime = createTime;
    return this;
  }

   /**
   * Get createTime
   * @return createTime
  **/
  @Schema(description = "")
  public DateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(DateTime createTime) {
    this.createTime = createTime;
  }

  public InlineResponse2009 emailSourceName(String emailSourceName) {
    this.emailSourceName = emailSourceName;
    return this;
  }

   /**
   * Get emailSourceName
   * @return emailSourceName
  **/
  @Schema(description = "")
  public String getEmailSourceName() {
    return emailSourceName;
  }

  public void setEmailSourceName(String emailSourceName) {
    this.emailSourceName = emailSourceName;
  }

  public InlineResponse2009 endTime(DateTime endTime) {
    this.endTime = endTime;
    return this;
  }

   /**
   * Get endTime
   * @return endTime
  **/
  @Schema(description = "")
  public DateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(DateTime endTime) {
    this.endTime = endTime;
  }

  public InlineResponse2009 feedback(InlineResponse2009Feedback feedback) {
    this.feedback = feedback;
    return this;
  }

   /**
   * Get feedback
   * @return feedback
  **/
  @Schema(description = "")
  public InlineResponse2009Feedback getFeedback() {
    return feedback;
  }

  public void setFeedback(InlineResponse2009Feedback feedback) {
    this.feedback = feedback;
  }

  public InlineResponse2009 hasAudience(Boolean hasAudience) {
    this.hasAudience = hasAudience;
    return this;
  }

   /**
   * Check if this ad has audience setup
   * @return hasAudience
  **/
  @Schema(description = "Check if this ad has audience setup")
  public Boolean isHasAudience() {
    return hasAudience;
  }

  public void setHasAudience(Boolean hasAudience) {
    this.hasAudience = hasAudience;
  }

  public InlineResponse2009 hasContent(Boolean hasContent) {
    this.hasContent = hasContent;
    return this;
  }

   /**
   * Check if this ad has content
   * @return hasContent
  **/
  @Schema(description = "Check if this ad has content")
  public Boolean isHasContent() {
    return hasContent;
  }

  public void setHasContent(Boolean hasContent) {
    this.hasContent = hasContent;
  }

  public InlineResponse2009 hasSegment(Boolean hasSegment) {
    this.hasSegment = hasSegment;
    return this;
  }

   /**
   * Get hasSegment
   * @return hasSegment
  **/
  @Schema(description = "")
  public Boolean isHasSegment() {
    return hasSegment;
  }

  public void setHasSegment(Boolean hasSegment) {
    this.hasSegment = hasSegment;
  }

  public InlineResponse2009 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique ID of an Outreach
   * @return id
  **/
  @Schema(description = "Unique ID of an Outreach")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2009 isConnected(Boolean isConnected) {
    this.isConnected = isConnected;
    return this;
  }

   /**
   * Check if this ad is connected to a facebook page
   * @return isConnected
  **/
  @Schema(description = "Check if this ad is connected to a facebook page")
  public Boolean isIsConnected() {
    return isConnected;
  }

  public void setIsConnected(Boolean isConnected) {
    this.isConnected = isConnected;
  }

  public InlineResponse2009 name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Title or name of an Outreach
   * @return name
  **/
  @Schema(description = "Title or name of an Outreach")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InlineResponse2009 needsAttention(Boolean needsAttention) {
    this.needsAttention = needsAttention;
    return this;
  }

   /**
   * Get needsAttention
   * @return needsAttention
  **/
  @Schema(description = "")
  public Boolean isNeedsAttention() {
    return needsAttention;
  }

  public void setNeedsAttention(Boolean needsAttention) {
    this.needsAttention = needsAttention;
  }

  public InlineResponse2009 pausedAt(DateTime pausedAt) {
    this.pausedAt = pausedAt;
    return this;
  }

   /**
   * Get pausedAt
   * @return pausedAt
  **/
  @Schema(description = "")
  public DateTime getPausedAt() {
    return pausedAt;
  }

  public void setPausedAt(DateTime pausedAt) {
    this.pausedAt = pausedAt;
  }

  public InlineResponse2009 publishedTime(DateTime publishedTime) {
    this.publishedTime = publishedTime;
    return this;
  }

   /**
   * Get publishedTime
   * @return publishedTime
  **/
  @Schema(description = "")
  public DateTime getPublishedTime() {
    return publishedTime;
  }

  public void setPublishedTime(DateTime publishedTime) {
    this.publishedTime = publishedTime;
  }

  public InlineResponse2009 recipients(List10 recipients) {
    this.recipients = recipients;
    return this;
  }

   /**
   * Get recipients
   * @return recipients
  **/
  @Schema(description = "")
  public List10 getRecipients() {
    return recipients;
  }

  public void setRecipients(List10 recipients) {
    this.recipients = recipients;
  }

  public InlineResponse2009 reportSummary(InlineResponse2009ReportSummary reportSummary) {
    this.reportSummary = reportSummary;
    return this;
  }

   /**
   * Get reportSummary
   * @return reportSummary
  **/
  @Schema(description = "")
  public InlineResponse2009ReportSummary getReportSummary() {
    return reportSummary;
  }

  public void setReportSummary(InlineResponse2009ReportSummary reportSummary) {
    this.reportSummary = reportSummary;
  }

  public InlineResponse2009 showReport(Boolean showReport) {
    this.showReport = showReport;
    return this;
  }

   /**
   * Outreach report availability
   * @return showReport
  **/
  @Schema(description = "Outreach report availability")
  public Boolean isShowReport() {
    return showReport;
  }

  public void setShowReport(Boolean showReport) {
    this.showReport = showReport;
  }

  public InlineResponse2009 site(InlineResponse2009Site site) {
    this.site = site;
    return this;
  }

   /**
   * Get site
   * @return site
  **/
  @Schema(description = "")
  public InlineResponse2009Site getSite() {
    return site;
  }

  public void setSite(InlineResponse2009Site site) {
    this.site = site;
  }

  public InlineResponse2009 startTime(DateTime startTime) {
    this.startTime = startTime;
    return this;
  }

   /**
   * Get startTime
   * @return startTime
  **/
  @Schema(description = "")
  public DateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(DateTime startTime) {
    this.startTime = startTime;
  }

  public InlineResponse2009 status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Campaign, Ad, or Page status
   * @return status
  **/
  @Schema(description = "Campaign, Ad, or Page status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public InlineResponse2009 thumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
    return this;
  }

   /**
   * The URL of the thumbnail for this outreach
   * @return thumbnail
  **/
  @Schema(description = "The URL of the thumbnail for this outreach")
  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public InlineResponse2009 type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Supported Campaign, Ad, Page type
   * @return type
  **/
  @Schema(description = "Supported Campaign, Ad, Page type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public InlineResponse2009 updatedAt(DateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * Get updatedAt
   * @return updatedAt
  **/
  @Schema(description = "")
  public DateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(DateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public InlineResponse2009 wasCanceledByFacebook(Boolean wasCanceledByFacebook) {
    this.wasCanceledByFacebook = wasCanceledByFacebook;
    return this;
  }

   /**
   * Get wasCanceledByFacebook
   * @return wasCanceledByFacebook
  **/
  @Schema(description = "")
  public Boolean isWasCanceledByFacebook() {
    return wasCanceledByFacebook;
  }

  public void setWasCanceledByFacebook(Boolean wasCanceledByFacebook) {
    this.wasCanceledByFacebook = wasCanceledByFacebook;
  }

  public InlineResponse2009 webId(Integer webId) {
    this.webId = webId;
    return this;
  }

   /**
   * Web ID
   * @return webId
  **/
  @Schema(description = "Web ID")
  public Integer getWebId() {
    return webId;
  }

  public void setWebId(Integer webId) {
    this.webId = webId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2009 inlineResponse2009 = (InlineResponse2009) o;
    return Objects.equals(this._links, inlineResponse2009._links) &&
        Objects.equals(this.audience, inlineResponse2009.audience) &&
        Objects.equals(this.budget, inlineResponse2009.budget) &&
        Objects.equals(this.canceledAt, inlineResponse2009.canceledAt) &&
        Objects.equals(this.channel, inlineResponse2009.channel) &&
        Objects.equals(this.content, inlineResponse2009.content) &&
        Objects.equals(this.createTime, inlineResponse2009.createTime) &&
        Objects.equals(this.emailSourceName, inlineResponse2009.emailSourceName) &&
        Objects.equals(this.endTime, inlineResponse2009.endTime) &&
        Objects.equals(this.feedback, inlineResponse2009.feedback) &&
        Objects.equals(this.hasAudience, inlineResponse2009.hasAudience) &&
        Objects.equals(this.hasContent, inlineResponse2009.hasContent) &&
        Objects.equals(this.hasSegment, inlineResponse2009.hasSegment) &&
        Objects.equals(this.id, inlineResponse2009.id) &&
        Objects.equals(this.isConnected, inlineResponse2009.isConnected) &&
        Objects.equals(this.name, inlineResponse2009.name) &&
        Objects.equals(this.needsAttention, inlineResponse2009.needsAttention) &&
        Objects.equals(this.pausedAt, inlineResponse2009.pausedAt) &&
        Objects.equals(this.publishedTime, inlineResponse2009.publishedTime) &&
        Objects.equals(this.recipients, inlineResponse2009.recipients) &&
        Objects.equals(this.reportSummary, inlineResponse2009.reportSummary) &&
        Objects.equals(this.showReport, inlineResponse2009.showReport) &&
        Objects.equals(this.site, inlineResponse2009.site) &&
        Objects.equals(this.startTime, inlineResponse2009.startTime) &&
        Objects.equals(this.status, inlineResponse2009.status) &&
        Objects.equals(this.thumbnail, inlineResponse2009.thumbnail) &&
        Objects.equals(this.type, inlineResponse2009.type) &&
        Objects.equals(this.updatedAt, inlineResponse2009.updatedAt) &&
        Objects.equals(this.wasCanceledByFacebook, inlineResponse2009.wasCanceledByFacebook) &&
        Objects.equals(this.webId, inlineResponse2009.webId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_links, audience, budget, canceledAt, channel, content, createTime, emailSourceName, endTime, feedback, hasAudience, hasContent, hasSegment, id, isConnected, name, needsAttention, pausedAt, publishedTime, recipients, reportSummary, showReport, site, startTime, status, thumbnail, type, updatedAt, wasCanceledByFacebook, webId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2009 {\n");
    
    sb.append("    _links: ").append(toIndentedString(_links)).append("\n");
    sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
    sb.append("    budget: ").append(toIndentedString(budget)).append("\n");
    sb.append("    canceledAt: ").append(toIndentedString(canceledAt)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    emailSourceName: ").append(toIndentedString(emailSourceName)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    feedback: ").append(toIndentedString(feedback)).append("\n");
    sb.append("    hasAudience: ").append(toIndentedString(hasAudience)).append("\n");
    sb.append("    hasContent: ").append(toIndentedString(hasContent)).append("\n");
    sb.append("    hasSegment: ").append(toIndentedString(hasSegment)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isConnected: ").append(toIndentedString(isConnected)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    needsAttention: ").append(toIndentedString(needsAttention)).append("\n");
    sb.append("    pausedAt: ").append(toIndentedString(pausedAt)).append("\n");
    sb.append("    publishedTime: ").append(toIndentedString(publishedTime)).append("\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    reportSummary: ").append(toIndentedString(reportSummary)).append("\n");
    sb.append("    showReport: ").append(toIndentedString(showReport)).append("\n");
    sb.append("    site: ").append(toIndentedString(site)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    thumbnail: ").append(toIndentedString(thumbnail)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    wasCanceledByFacebook: ").append(toIndentedString(wasCanceledByFacebook)).append("\n");
    sb.append("    webId: ").append(toIndentedString(webId)).append("\n");
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