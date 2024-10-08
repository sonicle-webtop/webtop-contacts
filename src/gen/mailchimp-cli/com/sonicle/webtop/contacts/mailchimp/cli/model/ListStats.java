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
/**
 * The average campaign statistics for your list. This won&#x27;t be present if we haven&#x27;t calculated it yet for this list.
 */
@Schema(description = "The average campaign statistics for your list. This won't be present if we haven't calculated it yet for this list.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class ListStats {
  @JsonProperty("sub_rate")
  private BigDecimal subRate = null;

  @JsonProperty("unsub_rate")
  private BigDecimal unsubRate = null;

  @JsonProperty("open_rate")
  private BigDecimal openRate = null;

  @JsonProperty("click_rate")
  private BigDecimal clickRate = null;

   /**
   * The average number of subscriptions per month for the list.
   * @return subRate
  **/
  @Schema(description = "The average number of subscriptions per month for the list.")
  public BigDecimal getSubRate() {
    return subRate;
  }

   /**
   * The average number of unsubscriptions per month for the list.
   * @return unsubRate
  **/
  @Schema(description = "The average number of unsubscriptions per month for the list.")
  public BigDecimal getUnsubRate() {
    return unsubRate;
  }

   /**
   * The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list.
   * @return openRate
  **/
  @Schema(description = "The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list.")
  public BigDecimal getOpenRate() {
    return openRate;
  }

   /**
   * The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list.
   * @return clickRate
  **/
  @Schema(description = "The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list.")
  public BigDecimal getClickRate() {
    return clickRate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListStats listStats = (ListStats) o;
    return Objects.equals(this.subRate, listStats.subRate) &&
        Objects.equals(this.unsubRate, listStats.unsubRate) &&
        Objects.equals(this.openRate, listStats.openRate) &&
        Objects.equals(this.clickRate, listStats.clickRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subRate, unsubRate, openRate, clickRate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListStats {\n");
    
    sb.append("    subRate: ").append(toIndentedString(subRate)).append("\n");
    sb.append("    unsubRate: ").append(toIndentedString(unsubRate)).append("\n");
    sb.append("    openRate: ").append(toIndentedString(openRate)).append("\n");
    sb.append("    clickRate: ").append(toIndentedString(clickRate)).append("\n");
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
