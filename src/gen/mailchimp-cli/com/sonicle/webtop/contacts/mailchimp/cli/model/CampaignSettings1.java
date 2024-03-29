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
 * The settings for your campaign, including subject, from name, reply-to address, and more.
 */
@Schema(description = "The settings for your campaign, including subject, from name, reply-to address, and more.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class CampaignSettings1 {
  @JsonProperty("subject_line")
  private String subjectLine = null;

  @JsonProperty("preview_text")
  private String previewText = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("from_name")
  private String fromName = null;

  @JsonProperty("reply_to")
  private String replyTo = null;

  @JsonProperty("use_conversation")
  private Boolean useConversation = null;

  @JsonProperty("to_name")
  private String toName = null;

  @JsonProperty("folder_id")
  private String folderId = null;

  @JsonProperty("authenticate")
  private Boolean authenticate = null;

  @JsonProperty("auto_footer")
  private Boolean autoFooter = null;

  @JsonProperty("inline_css")
  private Boolean inlineCss = null;

  @JsonProperty("auto_tweet")
  private Boolean autoTweet = null;

  @JsonProperty("auto_fb_post")
  private List<String> autoFbPost = null;

  @JsonProperty("fb_comments")
  private Boolean fbComments = null;

  @JsonProperty("template_id")
  private Integer templateId = null;

  public CampaignSettings1 subjectLine(String subjectLine) {
    this.subjectLine = subjectLine;
    return this;
  }

   /**
   * The subject line for the campaign.
   * @return subjectLine
  **/
  @Schema(description = "The subject line for the campaign.")
  public String getSubjectLine() {
    return subjectLine;
  }

  public void setSubjectLine(String subjectLine) {
    this.subjectLine = subjectLine;
  }

  public CampaignSettings1 previewText(String previewText) {
    this.previewText = previewText;
    return this;
  }

   /**
   * The preview text for the campaign.
   * @return previewText
  **/
  @Schema(description = "The preview text for the campaign.")
  public String getPreviewText() {
    return previewText;
  }

  public void setPreviewText(String previewText) {
    this.previewText = previewText;
  }

  public CampaignSettings1 title(String title) {
    this.title = title;
    return this;
  }

   /**
   * The title of the campaign.
   * @return title
  **/
  @Schema(description = "The title of the campaign.")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CampaignSettings1 fromName(String fromName) {
    this.fromName = fromName;
    return this;
  }

   /**
   * The &#x27;from&#x27; name on the campaign (not an email address).
   * @return fromName
  **/
  @Schema(description = "The 'from' name on the campaign (not an email address).")
  public String getFromName() {
    return fromName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public CampaignSettings1 replyTo(String replyTo) {
    this.replyTo = replyTo;
    return this;
  }

   /**
   * The reply-to email address for the campaign. Note: while this field is not required for campaign creation, it is required for sending.
   * @return replyTo
  **/
  @Schema(description = "The reply-to email address for the campaign. Note: while this field is not required for campaign creation, it is required for sending.")
  public String getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(String replyTo) {
    this.replyTo = replyTo;
  }

  public CampaignSettings1 useConversation(Boolean useConversation) {
    this.useConversation = useConversation;
    return this;
  }

   /**
   * Use Mailchimp Conversation feature to manage out-of-office replies.
   * @return useConversation
  **/
  @Schema(description = "Use Mailchimp Conversation feature to manage out-of-office replies.")
  public Boolean isUseConversation() {
    return useConversation;
  }

  public void setUseConversation(Boolean useConversation) {
    this.useConversation = useConversation;
  }

  public CampaignSettings1 toName(String toName) {
    this.toName = toName;
    return this;
  }

   /**
   * The campaign&#x27;s custom &#x27;To&#x27; name. Typically the first name [audience field](https://mailchimp.com/help/getting-started-with-merge-tags/).
   * @return toName
  **/
  @Schema(description = "The campaign's custom 'To' name. Typically the first name [audience field](https://mailchimp.com/help/getting-started-with-merge-tags/).")
  public String getToName() {
    return toName;
  }

  public void setToName(String toName) {
    this.toName = toName;
  }

  public CampaignSettings1 folderId(String folderId) {
    this.folderId = folderId;
    return this;
  }

   /**
   * If the campaign is listed in a folder, the id for that folder.
   * @return folderId
  **/
  @Schema(description = "If the campaign is listed in a folder, the id for that folder.")
  public String getFolderId() {
    return folderId;
  }

  public void setFolderId(String folderId) {
    this.folderId = folderId;
  }

  public CampaignSettings1 authenticate(Boolean authenticate) {
    this.authenticate = authenticate;
    return this;
  }

   /**
   * Whether Mailchimp [authenticated](https://mailchimp.com/help/about-email-authentication/) the campaign. Defaults to &#x60;true&#x60;.
   * @return authenticate
  **/
  @Schema(description = "Whether Mailchimp [authenticated](https://mailchimp.com/help/about-email-authentication/) the campaign. Defaults to `true`.")
  public Boolean isAuthenticate() {
    return authenticate;
  }

  public void setAuthenticate(Boolean authenticate) {
    this.authenticate = authenticate;
  }

  public CampaignSettings1 autoFooter(Boolean autoFooter) {
    this.autoFooter = autoFooter;
    return this;
  }

   /**
   * Automatically append Mailchimp&#x27;s [default footer](https://mailchimp.com/help/about-campaign-footers/) to the campaign.
   * @return autoFooter
  **/
  @Schema(description = "Automatically append Mailchimp's [default footer](https://mailchimp.com/help/about-campaign-footers/) to the campaign.")
  public Boolean isAutoFooter() {
    return autoFooter;
  }

  public void setAutoFooter(Boolean autoFooter) {
    this.autoFooter = autoFooter;
  }

  public CampaignSettings1 inlineCss(Boolean inlineCss) {
    this.inlineCss = inlineCss;
    return this;
  }

   /**
   * Automatically inline the CSS included with the campaign content.
   * @return inlineCss
  **/
  @Schema(description = "Automatically inline the CSS included with the campaign content.")
  public Boolean isInlineCss() {
    return inlineCss;
  }

  public void setInlineCss(Boolean inlineCss) {
    this.inlineCss = inlineCss;
  }

  public CampaignSettings1 autoTweet(Boolean autoTweet) {
    this.autoTweet = autoTweet;
    return this;
  }

   /**
   * Automatically tweet a link to the [campaign archive](https://mailchimp.com/help/about-email-campaign-archives-and-pages/) page when the campaign is sent.
   * @return autoTweet
  **/
  @Schema(description = "Automatically tweet a link to the [campaign archive](https://mailchimp.com/help/about-email-campaign-archives-and-pages/) page when the campaign is sent.")
  public Boolean isAutoTweet() {
    return autoTweet;
  }

  public void setAutoTweet(Boolean autoTweet) {
    this.autoTweet = autoTweet;
  }

  public CampaignSettings1 autoFbPost(List<String> autoFbPost) {
    this.autoFbPost = autoFbPost;
    return this;
  }

  public CampaignSettings1 addAutoFbPostItem(String autoFbPostItem) {
    if (this.autoFbPost == null) {
      this.autoFbPost = new ArrayList<>();
    }
    this.autoFbPost.add(autoFbPostItem);
    return this;
  }

   /**
   * An array of [Facebook](https://mailchimp.com/help/connect-or-disconnect-the-facebook-integration/) page ids to auto-post to.
   * @return autoFbPost
  **/
  @Schema(description = "An array of [Facebook](https://mailchimp.com/help/connect-or-disconnect-the-facebook-integration/) page ids to auto-post to.")
  public List<String> getAutoFbPost() {
    return autoFbPost;
  }

  public void setAutoFbPost(List<String> autoFbPost) {
    this.autoFbPost = autoFbPost;
  }

  public CampaignSettings1 fbComments(Boolean fbComments) {
    this.fbComments = fbComments;
    return this;
  }

   /**
   * Allows Facebook comments on the campaign (also force-enables the Campaign Archive toolbar). Defaults to &#x60;true&#x60;.
   * @return fbComments
  **/
  @Schema(description = "Allows Facebook comments on the campaign (also force-enables the Campaign Archive toolbar). Defaults to `true`.")
  public Boolean isFbComments() {
    return fbComments;
  }

  public void setFbComments(Boolean fbComments) {
    this.fbComments = fbComments;
  }

  public CampaignSettings1 templateId(Integer templateId) {
    this.templateId = templateId;
    return this;
  }

   /**
   * The id of the template to use.
   * @return templateId
  **/
  @Schema(description = "The id of the template to use.")
  public Integer getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CampaignSettings1 campaignSettings1 = (CampaignSettings1) o;
    return Objects.equals(this.subjectLine, campaignSettings1.subjectLine) &&
        Objects.equals(this.previewText, campaignSettings1.previewText) &&
        Objects.equals(this.title, campaignSettings1.title) &&
        Objects.equals(this.fromName, campaignSettings1.fromName) &&
        Objects.equals(this.replyTo, campaignSettings1.replyTo) &&
        Objects.equals(this.useConversation, campaignSettings1.useConversation) &&
        Objects.equals(this.toName, campaignSettings1.toName) &&
        Objects.equals(this.folderId, campaignSettings1.folderId) &&
        Objects.equals(this.authenticate, campaignSettings1.authenticate) &&
        Objects.equals(this.autoFooter, campaignSettings1.autoFooter) &&
        Objects.equals(this.inlineCss, campaignSettings1.inlineCss) &&
        Objects.equals(this.autoTweet, campaignSettings1.autoTweet) &&
        Objects.equals(this.autoFbPost, campaignSettings1.autoFbPost) &&
        Objects.equals(this.fbComments, campaignSettings1.fbComments) &&
        Objects.equals(this.templateId, campaignSettings1.templateId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subjectLine, previewText, title, fromName, replyTo, useConversation, toName, folderId, authenticate, autoFooter, inlineCss, autoTweet, autoFbPost, fbComments, templateId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampaignSettings1 {\n");
    
    sb.append("    subjectLine: ").append(toIndentedString(subjectLine)).append("\n");
    sb.append("    previewText: ").append(toIndentedString(previewText)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    fromName: ").append(toIndentedString(fromName)).append("\n");
    sb.append("    replyTo: ").append(toIndentedString(replyTo)).append("\n");
    sb.append("    useConversation: ").append(toIndentedString(useConversation)).append("\n");
    sb.append("    toName: ").append(toIndentedString(toName)).append("\n");
    sb.append("    folderId: ").append(toIndentedString(folderId)).append("\n");
    sb.append("    authenticate: ").append(toIndentedString(authenticate)).append("\n");
    sb.append("    autoFooter: ").append(toIndentedString(autoFooter)).append("\n");
    sb.append("    inlineCss: ").append(toIndentedString(inlineCss)).append("\n");
    sb.append("    autoTweet: ").append(toIndentedString(autoTweet)).append("\n");
    sb.append("    autoFbPost: ").append(toIndentedString(autoFbPost)).append("\n");
    sb.append("    fbComments: ").append(toIndentedString(fbComments)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
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
