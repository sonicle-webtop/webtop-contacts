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
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2009ReportSummaryEcommerce;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
/**
 * InlineResponse2009ReportSummary
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class InlineResponse2009ReportSummary {
  @JsonProperty("opens")
  private Integer opens = null;

  @JsonProperty("unique_opens")
  private Integer uniqueOpens = null;

  @JsonProperty("open_rate")
  private BigDecimal openRate = null;

  @JsonProperty("clicks")
  private Integer clicks = null;

  @JsonProperty("subscriber_clicks")
  private Integer subscriberClicks = null;

  @JsonProperty("click_rate")
  private BigDecimal clickRate = null;

  @JsonProperty("visits")
  private Integer visits = null;

  @JsonProperty("unique_visits")
  private Integer uniqueVisits = null;

  @JsonProperty("conversion_rate")
  private BigDecimal conversionRate = null;

  @JsonProperty("subscribes")
  private Integer subscribes = null;

  @JsonProperty("ecommerce")
  private InlineResponse2009ReportSummaryEcommerce ecommerce = null;

  @JsonProperty("impressions")
  private BigDecimal impressions = null;

  @JsonProperty("reach")
  private Integer reach = null;

  @JsonProperty("engagements")
  private Integer engagements = null;

  @JsonProperty("total_sent")
  private Integer totalSent = null;

  public InlineResponse2009ReportSummary opens(Integer opens) {
    this.opens = opens;
    return this;
  }

   /**
   * Get opens
   * @return opens
  **/
  @Schema(description = "")
  public Integer getOpens() {
    return opens;
  }

  public void setOpens(Integer opens) {
    this.opens = opens;
  }

  public InlineResponse2009ReportSummary uniqueOpens(Integer uniqueOpens) {
    this.uniqueOpens = uniqueOpens;
    return this;
  }

   /**
   * Get uniqueOpens
   * @return uniqueOpens
  **/
  @Schema(description = "")
  public Integer getUniqueOpens() {
    return uniqueOpens;
  }

  public void setUniqueOpens(Integer uniqueOpens) {
    this.uniqueOpens = uniqueOpens;
  }

  public InlineResponse2009ReportSummary openRate(BigDecimal openRate) {
    this.openRate = openRate;
    return this;
  }

   /**
   * Get openRate
   * @return openRate
  **/
  @Schema(description = "")
  public BigDecimal getOpenRate() {
    return openRate;
  }

  public void setOpenRate(BigDecimal openRate) {
    this.openRate = openRate;
  }

  public InlineResponse2009ReportSummary clicks(Integer clicks) {
    this.clicks = clicks;
    return this;
  }

   /**
   * Get clicks
   * @return clicks
  **/
  @Schema(description = "")
  public Integer getClicks() {
    return clicks;
  }

  public void setClicks(Integer clicks) {
    this.clicks = clicks;
  }

  public InlineResponse2009ReportSummary subscriberClicks(Integer subscriberClicks) {
    this.subscriberClicks = subscriberClicks;
    return this;
  }

   /**
   * Get subscriberClicks
   * @return subscriberClicks
  **/
  @Schema(description = "")
  public Integer getSubscriberClicks() {
    return subscriberClicks;
  }

  public void setSubscriberClicks(Integer subscriberClicks) {
    this.subscriberClicks = subscriberClicks;
  }

  public InlineResponse2009ReportSummary clickRate(BigDecimal clickRate) {
    this.clickRate = clickRate;
    return this;
  }

   /**
   * Get clickRate
   * @return clickRate
  **/
  @Schema(description = "")
  public BigDecimal getClickRate() {
    return clickRate;
  }

  public void setClickRate(BigDecimal clickRate) {
    this.clickRate = clickRate;
  }

  public InlineResponse2009ReportSummary visits(Integer visits) {
    this.visits = visits;
    return this;
  }

   /**
   * Get visits
   * @return visits
  **/
  @Schema(description = "")
  public Integer getVisits() {
    return visits;
  }

  public void setVisits(Integer visits) {
    this.visits = visits;
  }

  public InlineResponse2009ReportSummary uniqueVisits(Integer uniqueVisits) {
    this.uniqueVisits = uniqueVisits;
    return this;
  }

   /**
   * Get uniqueVisits
   * @return uniqueVisits
  **/
  @Schema(description = "")
  public Integer getUniqueVisits() {
    return uniqueVisits;
  }

  public void setUniqueVisits(Integer uniqueVisits) {
    this.uniqueVisits = uniqueVisits;
  }

  public InlineResponse2009ReportSummary conversionRate(BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
    return this;
  }

   /**
   * Get conversionRate
   * @return conversionRate
  **/
  @Schema(description = "")
  public BigDecimal getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
  }

  public InlineResponse2009ReportSummary subscribes(Integer subscribes) {
    this.subscribes = subscribes;
    return this;
  }

   /**
   * Get subscribes
   * @return subscribes
  **/
  @Schema(description = "")
  public Integer getSubscribes() {
    return subscribes;
  }

  public void setSubscribes(Integer subscribes) {
    this.subscribes = subscribes;
  }

  public InlineResponse2009ReportSummary ecommerce(InlineResponse2009ReportSummaryEcommerce ecommerce) {
    this.ecommerce = ecommerce;
    return this;
  }

   /**
   * Get ecommerce
   * @return ecommerce
  **/
  @Schema(description = "")
  public InlineResponse2009ReportSummaryEcommerce getEcommerce() {
    return ecommerce;
  }

  public void setEcommerce(InlineResponse2009ReportSummaryEcommerce ecommerce) {
    this.ecommerce = ecommerce;
  }

  public InlineResponse2009ReportSummary impressions(BigDecimal impressions) {
    this.impressions = impressions;
    return this;
  }

   /**
   * Get impressions
   * @return impressions
  **/
  @Schema(description = "")
  public BigDecimal getImpressions() {
    return impressions;
  }

  public void setImpressions(BigDecimal impressions) {
    this.impressions = impressions;
  }

  public InlineResponse2009ReportSummary reach(Integer reach) {
    this.reach = reach;
    return this;
  }

   /**
   * Get reach
   * @return reach
  **/
  @Schema(description = "")
  public Integer getReach() {
    return reach;
  }

  public void setReach(Integer reach) {
    this.reach = reach;
  }

  public InlineResponse2009ReportSummary engagements(Integer engagements) {
    this.engagements = engagements;
    return this;
  }

   /**
   * Get engagements
   * @return engagements
  **/
  @Schema(description = "")
  public Integer getEngagements() {
    return engagements;
  }

  public void setEngagements(Integer engagements) {
    this.engagements = engagements;
  }

  public InlineResponse2009ReportSummary totalSent(Integer totalSent) {
    this.totalSent = totalSent;
    return this;
  }

   /**
   * Get totalSent
   * @return totalSent
  **/
  @Schema(description = "")
  public Integer getTotalSent() {
    return totalSent;
  }

  public void setTotalSent(Integer totalSent) {
    this.totalSent = totalSent;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2009ReportSummary inlineResponse2009ReportSummary = (InlineResponse2009ReportSummary) o;
    return Objects.equals(this.opens, inlineResponse2009ReportSummary.opens) &&
        Objects.equals(this.uniqueOpens, inlineResponse2009ReportSummary.uniqueOpens) &&
        Objects.equals(this.openRate, inlineResponse2009ReportSummary.openRate) &&
        Objects.equals(this.clicks, inlineResponse2009ReportSummary.clicks) &&
        Objects.equals(this.subscriberClicks, inlineResponse2009ReportSummary.subscriberClicks) &&
        Objects.equals(this.clickRate, inlineResponse2009ReportSummary.clickRate) &&
        Objects.equals(this.visits, inlineResponse2009ReportSummary.visits) &&
        Objects.equals(this.uniqueVisits, inlineResponse2009ReportSummary.uniqueVisits) &&
        Objects.equals(this.conversionRate, inlineResponse2009ReportSummary.conversionRate) &&
        Objects.equals(this.subscribes, inlineResponse2009ReportSummary.subscribes) &&
        Objects.equals(this.ecommerce, inlineResponse2009ReportSummary.ecommerce) &&
        Objects.equals(this.impressions, inlineResponse2009ReportSummary.impressions) &&
        Objects.equals(this.reach, inlineResponse2009ReportSummary.reach) &&
        Objects.equals(this.engagements, inlineResponse2009ReportSummary.engagements) &&
        Objects.equals(this.totalSent, inlineResponse2009ReportSummary.totalSent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opens, uniqueOpens, openRate, clicks, subscriberClicks, clickRate, visits, uniqueVisits, conversionRate, subscribes, ecommerce, impressions, reach, engagements, totalSent);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2009ReportSummary {\n");
    
    sb.append("    opens: ").append(toIndentedString(opens)).append("\n");
    sb.append("    uniqueOpens: ").append(toIndentedString(uniqueOpens)).append("\n");
    sb.append("    openRate: ").append(toIndentedString(openRate)).append("\n");
    sb.append("    clicks: ").append(toIndentedString(clicks)).append("\n");
    sb.append("    subscriberClicks: ").append(toIndentedString(subscriberClicks)).append("\n");
    sb.append("    clickRate: ").append(toIndentedString(clickRate)).append("\n");
    sb.append("    visits: ").append(toIndentedString(visits)).append("\n");
    sb.append("    uniqueVisits: ").append(toIndentedString(uniqueVisits)).append("\n");
    sb.append("    conversionRate: ").append(toIndentedString(conversionRate)).append("\n");
    sb.append("    subscribes: ").append(toIndentedString(subscribes)).append("\n");
    sb.append("    ecommerce: ").append(toIndentedString(ecommerce)).append("\n");
    sb.append("    impressions: ").append(toIndentedString(impressions)).append("\n");
    sb.append("    reach: ").append(toIndentedString(reach)).append("\n");
    sb.append("    engagements: ").append(toIndentedString(engagements)).append("\n");
    sb.append("    totalSent: ").append(toIndentedString(totalSent)).append("\n");
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