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
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCartLineItem5;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCustomer6;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * Information about a specific cart.
 */
@Schema(description = "Information about a specific cart.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EcommerceCart3 {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("customer")
  private EcommerceCustomer6 customer = null;

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
  private List<EcommerceCartLineItem5> lines = null;

  @JsonProperty("created_at")
  private DateTime createdAt = null;

  @JsonProperty("updated_at")
  private DateTime updatedAt = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * A unique identifier for the cart.
   * @return id
  **/
  @Schema(description = "A unique identifier for the cart.")
  public String getId() {
    return id;
  }

  public EcommerceCart3 customer(EcommerceCustomer6 customer) {
    this.customer = customer;
    return this;
  }

   /**
   * Get customer
   * @return customer
  **/
  @Schema(description = "")
  public EcommerceCustomer6 getCustomer() {
    return customer;
  }

  public void setCustomer(EcommerceCustomer6 customer) {
    this.customer = customer;
  }

  public EcommerceCart3 campaignId(String campaignId) {
    this.campaignId = campaignId;
    return this;
  }

   /**
   * A string that uniquely identifies the campaign associated with a cart.
   * @return campaignId
  **/
  @Schema(example = "839488a60b", description = "A string that uniquely identifies the campaign associated with a cart.")
  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public EcommerceCart3 checkoutUrl(String checkoutUrl) {
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

  public EcommerceCart3 currencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

   /**
   * The three-letter ISO 4217 code for the currency that the cart uses.
   * @return currencyCode
  **/
  @Schema(description = "The three-letter ISO 4217 code for the currency that the cart uses.")
  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public EcommerceCart3 orderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
    return this;
  }

   /**
   * The order total for the cart.
   * @return orderTotal
  **/
  @Schema(description = "The order total for the cart.")
  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }

  public EcommerceCart3 taxTotal(BigDecimal taxTotal) {
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

  public EcommerceCart3 lines(List<EcommerceCartLineItem5> lines) {
    this.lines = lines;
    return this;
  }

  public EcommerceCart3 addLinesItem(EcommerceCartLineItem5 linesItem) {
    if (this.lines == null) {
      this.lines = new ArrayList<>();
    }
    this.lines.add(linesItem);
    return this;
  }

   /**
   * An array of the cart&#x27;s line items.
   * @return lines
  **/
  @Schema(description = "An array of the cart's line items.")
  public List<EcommerceCartLineItem5> getLines() {
    return lines;
  }

  public void setLines(List<EcommerceCartLineItem5> lines) {
    this.lines = lines;
  }

   /**
   * The date and time the cart was created in ISO 8601 format.
   * @return createdAt
  **/
  @Schema(example = "2015-07-15T19:28Z", description = "The date and time the cart was created in ISO 8601 format.")
  public DateTime getCreatedAt() {
    return createdAt;
  }

   /**
   * The date and time the cart was last updated in ISO 8601 format.
   * @return updatedAt
  **/
  @Schema(example = "2015-07-15T19:28Z", description = "The date and time the cart was last updated in ISO 8601 format.")
  public DateTime getUpdatedAt() {
    return updatedAt;
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
    EcommerceCart3 ecommerceCart3 = (EcommerceCart3) o;
    return Objects.equals(this.id, ecommerceCart3.id) &&
        Objects.equals(this.customer, ecommerceCart3.customer) &&
        Objects.equals(this.campaignId, ecommerceCart3.campaignId) &&
        Objects.equals(this.checkoutUrl, ecommerceCart3.checkoutUrl) &&
        Objects.equals(this.currencyCode, ecommerceCart3.currencyCode) &&
        Objects.equals(this.orderTotal, ecommerceCart3.orderTotal) &&
        Objects.equals(this.taxTotal, ecommerceCart3.taxTotal) &&
        Objects.equals(this.lines, ecommerceCart3.lines) &&
        Objects.equals(this.createdAt, ecommerceCart3.createdAt) &&
        Objects.equals(this.updatedAt, ecommerceCart3.updatedAt) &&
        Objects.equals(this._links, ecommerceCart3._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customer, campaignId, checkoutUrl, currencyCode, orderTotal, taxTotal, lines, createdAt, updatedAt, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcommerceCart3 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    checkoutUrl: ").append(toIndentedString(checkoutUrl)).append("\n");
    sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
    sb.append("    orderTotal: ").append(toIndentedString(orderTotal)).append("\n");
    sb.append("    taxTotal: ").append(toIndentedString(taxTotal)).append("\n");
    sb.append("    lines: ").append(toIndentedString(lines)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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