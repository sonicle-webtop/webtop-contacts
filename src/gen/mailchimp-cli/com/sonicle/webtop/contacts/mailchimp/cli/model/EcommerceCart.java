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
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCartLineItem;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCustomer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Information about a specific cart.
 */
@Schema(description = "Information about a specific cart.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EcommerceCart {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("customer")
  private EcommerceCustomer customer = null;

  @JsonProperty("campaign_id")
  private String campaignId = null;

  @JsonProperty("checkout_url")
  private String checkoutUrl = null;

  @JsonProperty("currency_code")
  private String currencyCode = null;

  @JsonProperty("order_total")
  private BigDecimal orderTotal = null;

  @JsonProperty("tax_total")
  private BigDecimal taxTotal = null;

  @JsonProperty("lines")
  private List<EcommerceCartLineItem> lines = new ArrayList<>();

  public EcommerceCart id(String id) {
    this.id = id;
    return this;
  }

   /**
   * A unique identifier for the cart.
   * @return id
  **/
  @Schema(required = true, description = "A unique identifier for the cart.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EcommerceCart customer(EcommerceCustomer customer) {
    this.customer = customer;
    return this;
  }

   /**
   * Get customer
   * @return customer
  **/
  @Schema(required = true, description = "")
  public EcommerceCustomer getCustomer() {
    return customer;
  }

  public void setCustomer(EcommerceCustomer customer) {
    this.customer = customer;
  }

  public EcommerceCart campaignId(String campaignId) {
    this.campaignId = campaignId;
    return this;
  }

   /**
   * A string that uniquely identifies the campaign for a cart.
   * @return campaignId
  **/
  @Schema(example = "839488a60b", description = "A string that uniquely identifies the campaign for a cart.")
  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public EcommerceCart checkoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
    return this;
  }

   /**
   * The URL for the cart. This parameter is required for [Abandoned Cart](https://mailchimp.com/help/create-an-abandoned-cart-email/) automations.
   * @return checkoutUrl
  **/
  @Schema(description = "The URL for the cart. This parameter is required for [Abandoned Cart](https://mailchimp.com/help/create-an-abandoned-cart-email/) automations.")
  public String getCheckoutUrl() {
    return checkoutUrl;
  }

  public void setCheckoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
  }

  public EcommerceCart currencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

   /**
   * The three-letter ISO 4217 code for the currency that the cart uses.
   * @return currencyCode
  **/
  @Schema(required = true, description = "The three-letter ISO 4217 code for the currency that the cart uses.")
  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public EcommerceCart orderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
    return this;
  }

   /**
   * The order total for the cart.
   * @return orderTotal
  **/
  @Schema(required = true, description = "The order total for the cart.")
  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }

  public EcommerceCart taxTotal(BigDecimal taxTotal) {
    this.taxTotal = taxTotal;
    return this;
  }

   /**
   * The total tax for the cart.
   * @return taxTotal
  **/
  @Schema(description = "The total tax for the cart.")
  public BigDecimal getTaxTotal() {
    return taxTotal;
  }

  public void setTaxTotal(BigDecimal taxTotal) {
    this.taxTotal = taxTotal;
  }

  public EcommerceCart lines(List<EcommerceCartLineItem> lines) {
    this.lines = lines;
    return this;
  }

  public EcommerceCart addLinesItem(EcommerceCartLineItem linesItem) {
    this.lines.add(linesItem);
    return this;
  }

   /**
   * An array of the cart&#x27;s line items.
   * @return lines
  **/
  @Schema(required = true, description = "An array of the cart's line items.")
  public List<EcommerceCartLineItem> getLines() {
    return lines;
  }

  public void setLines(List<EcommerceCartLineItem> lines) {
    this.lines = lines;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EcommerceCart ecommerceCart = (EcommerceCart) o;
    return Objects.equals(this.id, ecommerceCart.id) &&
        Objects.equals(this.customer, ecommerceCart.customer) &&
        Objects.equals(this.campaignId, ecommerceCart.campaignId) &&
        Objects.equals(this.checkoutUrl, ecommerceCart.checkoutUrl) &&
        Objects.equals(this.currencyCode, ecommerceCart.currencyCode) &&
        Objects.equals(this.orderTotal, ecommerceCart.orderTotal) &&
        Objects.equals(this.taxTotal, ecommerceCart.taxTotal) &&
        Objects.equals(this.lines, ecommerceCart.lines);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customer, campaignId, checkoutUrl, currencyCode, orderTotal, taxTotal, lines);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcommerceCart {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    checkoutUrl: ").append(toIndentedString(checkoutUrl)).append("\n");
    sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
    sb.append("    orderTotal: ").append(toIndentedString(orderTotal)).append("\n");
    sb.append("    taxTotal: ").append(toIndentedString(taxTotal)).append("\n");
    sb.append("    lines: ").append(toIndentedString(lines)).append("\n");
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
