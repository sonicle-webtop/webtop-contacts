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
import java.math.BigDecimal;
import org.joda.time.DateTime;
/**
 * An object describing the open activity for the campaign.
 */
@Schema(description = "An object describing the open activity for the campaign.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class Opens {
  @JsonProperty("opens_total")
  private Integer opensTotal = null;

  @JsonProperty("unique_opens")
  private Integer uniqueOpens = null;

  @JsonProperty("open_rate")
  private BigDecimal openRate = null;

  @JsonProperty("last_open")
  private DateTime lastOpen = null;

  public Opens opensTotal(Integer opensTotal) {
    this.opensTotal = opensTotal;
    return this;
  }

   /**
   * The total number of opens for a campaign.
   * @return opensTotal
  **/
  @Schema(description = "The total number of opens for a campaign.")
  public Integer getOpensTotal() {
    return opensTotal;
  }

  public void setOpensTotal(Integer opensTotal) {
    this.opensTotal = opensTotal;
  }

  public Opens uniqueOpens(Integer uniqueOpens) {
    this.uniqueOpens = uniqueOpens;
    return this;
  }

   /**
   * The total number of unique opens.
   * @return uniqueOpens
  **/
  @Schema(description = "The total number of unique opens.")
  public Integer getUniqueOpens() {
    return uniqueOpens;
  }

  public void setUniqueOpens(Integer uniqueOpens) {
    this.uniqueOpens = uniqueOpens;
  }

  public Opens openRate(BigDecimal openRate) {
    this.openRate = openRate;
    return this;
  }

   /**
   * The number of unique opens divided by the total number of successful deliveries.
   * @return openRate
  **/
  @Schema(description = "The number of unique opens divided by the total number of successful deliveries.")
  public BigDecimal getOpenRate() {
    return openRate;
  }

  public void setOpenRate(BigDecimal openRate) {
    this.openRate = openRate;
  }

  public Opens lastOpen(DateTime lastOpen) {
    this.lastOpen = lastOpen;
    return this;
  }

   /**
   * The date and time of the last recorded open in ISO 8601 format.
   * @return lastOpen
  **/
  @Schema(description = "The date and time of the last recorded open in ISO 8601 format.")
  public DateTime getLastOpen() {
    return lastOpen;
  }

  public void setLastOpen(DateTime lastOpen) {
    this.lastOpen = lastOpen;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Opens opens = (Opens) o;
    return Objects.equals(this.opensTotal, opens.opensTotal) &&
        Objects.equals(this.uniqueOpens, opens.uniqueOpens) &&
        Objects.equals(this.openRate, opens.openRate) &&
        Objects.equals(this.lastOpen, opens.lastOpen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opensTotal, uniqueOpens, openRate, lastOpen);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Opens {\n");
    
    sb.append("    opensTotal: ").append(toIndentedString(opensTotal)).append("\n");
    sb.append("    uniqueOpens: ").append(toIndentedString(uniqueOpens)).append("\n");
    sb.append("    openRate: ").append(toIndentedString(openRate)).append("\n");
    sb.append("    lastOpen: ").append(toIndentedString(lastOpen)).append("\n");
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