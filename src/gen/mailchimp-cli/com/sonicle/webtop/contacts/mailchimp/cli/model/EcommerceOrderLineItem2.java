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
 * Information about a specific order line.
 */
@Schema(description = "Information about a specific order line.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EcommerceOrderLineItem2 {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("product_id")
  private String productId = null;

  @JsonProperty("product_variant_id")
  private String productVariantId = null;

  @JsonProperty("quantity")
  private Integer quantity = null;

  @JsonProperty("price")
  private BigDecimal price = null;

  @JsonProperty("discount")
  private BigDecimal discount = null;

  public EcommerceOrderLineItem2 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * A unique identifier for the order line item.
   * @return id
  **/
  @Schema(required = true, description = "A unique identifier for the order line item.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EcommerceOrderLineItem2 productId(String productId) {
    this.productId = productId;
    return this;
  }

   /**
   * A unique identifier for the product associated with the order line item.
   * @return productId
  **/
  @Schema(required = true, description = "A unique identifier for the product associated with the order line item.")
  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public EcommerceOrderLineItem2 productVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
    return this;
  }

   /**
   * A unique identifier for the product variant associated with the order line item.
   * @return productVariantId
  **/
  @Schema(required = true, description = "A unique identifier for the product variant associated with the order line item.")
  public String getProductVariantId() {
    return productVariantId;
  }

  public void setProductVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
  }

  public EcommerceOrderLineItem2 quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * The quantity of an order line item.
   * @return quantity
  **/
  @Schema(required = true, description = "The quantity of an order line item.")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public EcommerceOrderLineItem2 price(BigDecimal price) {
    this.price = price;
    return this;
  }

   /**
   * The price of an order line item.
   * @return price
  **/
  @Schema(required = true, description = "The price of an order line item.")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public EcommerceOrderLineItem2 discount(BigDecimal discount) {
    this.discount = discount;
    return this;
  }

   /**
   * The total discount amount applied to this line item.
   * @return discount
  **/
  @Schema(description = "The total discount amount applied to this line item.")
  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EcommerceOrderLineItem2 ecommerceOrderLineItem2 = (EcommerceOrderLineItem2) o;
    return Objects.equals(this.id, ecommerceOrderLineItem2.id) &&
        Objects.equals(this.productId, ecommerceOrderLineItem2.productId) &&
        Objects.equals(this.productVariantId, ecommerceOrderLineItem2.productVariantId) &&
        Objects.equals(this.quantity, ecommerceOrderLineItem2.quantity) &&
        Objects.equals(this.price, ecommerceOrderLineItem2.price) &&
        Objects.equals(this.discount, ecommerceOrderLineItem2.discount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId, productVariantId, quantity, price, discount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcommerceOrderLineItem2 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    productVariantId: ").append(toIndentedString(productVariantId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
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
