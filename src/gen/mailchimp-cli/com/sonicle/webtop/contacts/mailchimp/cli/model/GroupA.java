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
 * Stats for Group A.
 */
@Schema(description = "Stats for Group A.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class GroupA {
  @JsonProperty("total_clicks_a")
  private Integer totalClicksA = null;

  @JsonProperty("click_percentage_a")
  private BigDecimal clickPercentageA = null;

  @JsonProperty("unique_clicks_a")
  private Integer uniqueClicksA = null;

  @JsonProperty("unique_click_percentage_a")
  private BigDecimal uniqueClickPercentageA = null;

   /**
   * The total number of clicks for Group A.
   * @return totalClicksA
  **/
  @Schema(description = "The total number of clicks for Group A.")
  public Integer getTotalClicksA() {
    return totalClicksA;
  }

   /**
   * The percentage of total clicks for Group A.
   * @return clickPercentageA
  **/
  @Schema(description = "The percentage of total clicks for Group A.")
  public BigDecimal getClickPercentageA() {
    return clickPercentageA;
  }

   /**
   * The number of unique clicks for Group A.
   * @return uniqueClicksA
  **/
  @Schema(description = "The number of unique clicks for Group A.")
  public Integer getUniqueClicksA() {
    return uniqueClicksA;
  }

   /**
   * The percentage of unique clicks for Group A.
   * @return uniqueClickPercentageA
  **/
  @Schema(description = "The percentage of unique clicks for Group A.")
  public BigDecimal getUniqueClickPercentageA() {
    return uniqueClickPercentageA;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupA groupA = (GroupA) o;
    return Objects.equals(this.totalClicksA, groupA.totalClicksA) &&
        Objects.equals(this.clickPercentageA, groupA.clickPercentageA) &&
        Objects.equals(this.uniqueClicksA, groupA.uniqueClicksA) &&
        Objects.equals(this.uniqueClickPercentageA, groupA.uniqueClickPercentageA);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalClicksA, clickPercentageA, uniqueClicksA, uniqueClickPercentageA);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupA {\n");
    
    sb.append("    totalClicksA: ").append(toIndentedString(totalClicksA)).append("\n");
    sb.append("    clickPercentageA: ").append(toIndentedString(clickPercentageA)).append("\n");
    sb.append("    uniqueClicksA: ").append(toIndentedString(uniqueClicksA)).append("\n");
    sb.append("    uniqueClickPercentageA: ").append(toIndentedString(uniqueClickPercentageA)).append("\n");
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
