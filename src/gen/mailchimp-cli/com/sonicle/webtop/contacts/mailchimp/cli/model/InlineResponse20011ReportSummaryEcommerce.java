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
 * InlineResponse20011ReportSummaryEcommerce
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class InlineResponse20011ReportSummaryEcommerce {
  @JsonProperty("total_revenue")
  private BigDecimal totalRevenue = null;

  @JsonProperty("currency_code")
  private String currencyCode = null;

  public InlineResponse20011ReportSummaryEcommerce totalRevenue(BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
    return this;
  }

   /**
   * Get totalRevenue
   * @return totalRevenue
  **/
  @Schema(description = "")
  public BigDecimal getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public InlineResponse20011ReportSummaryEcommerce currencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

   /**
   * Get currencyCode
   * @return currencyCode
  **/
  @Schema(description = "")
  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse20011ReportSummaryEcommerce inlineResponse20011ReportSummaryEcommerce = (InlineResponse20011ReportSummaryEcommerce) o;
    return Objects.equals(this.totalRevenue, inlineResponse20011ReportSummaryEcommerce.totalRevenue) &&
        Objects.equals(this.currencyCode, inlineResponse20011ReportSummaryEcommerce.currencyCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalRevenue, currencyCode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse20011ReportSummaryEcommerce {\n");
    
    sb.append("    totalRevenue: ").append(toIndentedString(totalRevenue)).append("\n");
    sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
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
