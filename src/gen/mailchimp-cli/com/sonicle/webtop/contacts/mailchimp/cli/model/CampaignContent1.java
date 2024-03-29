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
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignscampaignIdcontentVariateContents;
import com.sonicle.webtop.contacts.mailchimp.cli.model.TemplateContent;
import com.sonicle.webtop.contacts.mailchimp.cli.model.UploadArchive;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * The HTML and plain-text content for a campaign
 */
@Schema(description = "The HTML and plain-text content for a campaign")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class CampaignContent1 {
  @JsonProperty("plain_text")
  private String plainText = null;

  @JsonProperty("html")
  private String html = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("template")
  private TemplateContent template = null;

  @JsonProperty("archive")
  private UploadArchive archive = null;

  @JsonProperty("variate_contents")
  private List<CampaignscampaignIdcontentVariateContents> variateContents = null;

  public CampaignContent1 plainText(String plainText) {
    this.plainText = plainText;
    return this;
  }

   /**
   * The plain-text portion of the campaign. If left unspecified, we&#x27;ll generate this automatically.
   * @return plainText
  **/
  @Schema(description = "The plain-text portion of the campaign. If left unspecified, we'll generate this automatically.")
  public String getPlainText() {
    return plainText;
  }

  public void setPlainText(String plainText) {
    this.plainText = plainText;
  }

  public CampaignContent1 html(String html) {
    this.html = html;
    return this;
  }

   /**
   * The raw HTML for the campaign.
   * @return html
  **/
  @Schema(description = "The raw HTML for the campaign.")
  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public CampaignContent1 url(String url) {
    this.url = url;
    return this;
  }

   /**
   * When importing a campaign, the URL where the HTML lives.
   * @return url
  **/
  @Schema(description = "When importing a campaign, the URL where the HTML lives.")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public CampaignContent1 template(TemplateContent template) {
    this.template = template;
    return this;
  }

   /**
   * Get template
   * @return template
  **/
  @Schema(description = "")
  public TemplateContent getTemplate() {
    return template;
  }

  public void setTemplate(TemplateContent template) {
    this.template = template;
  }

  public CampaignContent1 archive(UploadArchive archive) {
    this.archive = archive;
    return this;
  }

   /**
   * Get archive
   * @return archive
  **/
  @Schema(description = "")
  public UploadArchive getArchive() {
    return archive;
  }

  public void setArchive(UploadArchive archive) {
    this.archive = archive;
  }

  public CampaignContent1 variateContents(List<CampaignscampaignIdcontentVariateContents> variateContents) {
    this.variateContents = variateContents;
    return this;
  }

  public CampaignContent1 addVariateContentsItem(CampaignscampaignIdcontentVariateContents variateContentsItem) {
    if (this.variateContents == null) {
      this.variateContents = new ArrayList<>();
    }
    this.variateContents.add(variateContentsItem);
    return this;
  }

   /**
   * Content options for [Multivariate Campaigns](https://mailchimp.com/help/about-multivariate-campaigns/). Each content option must provide HTML content and may optionally provide plain text. For campaigns not testing content, only one object should be provided.
   * @return variateContents
  **/
  @Schema(description = "Content options for [Multivariate Campaigns](https://mailchimp.com/help/about-multivariate-campaigns/). Each content option must provide HTML content and may optionally provide plain text. For campaigns not testing content, only one object should be provided.")
  public List<CampaignscampaignIdcontentVariateContents> getVariateContents() {
    return variateContents;
  }

  public void setVariateContents(List<CampaignscampaignIdcontentVariateContents> variateContents) {
    this.variateContents = variateContents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CampaignContent1 campaignContent1 = (CampaignContent1) o;
    return Objects.equals(this.plainText, campaignContent1.plainText) &&
        Objects.equals(this.html, campaignContent1.html) &&
        Objects.equals(this.url, campaignContent1.url) &&
        Objects.equals(this.template, campaignContent1.template) &&
        Objects.equals(this.archive, campaignContent1.archive) &&
        Objects.equals(this.variateContents, campaignContent1.variateContents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plainText, html, url, template, archive, variateContents);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampaignContent1 {\n");
    
    sb.append("    plainText: ").append(toIndentedString(plainText)).append("\n");
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
    sb.append("    archive: ").append(toIndentedString(archive)).append("\n");
    sb.append("    variateContents: ").append(toIndentedString(variateContents)).append("\n");
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
