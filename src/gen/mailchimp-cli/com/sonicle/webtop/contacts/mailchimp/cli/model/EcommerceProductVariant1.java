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
 * Information about a specific product variant.
 */
@Schema(description = "Information about a specific product variant.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EcommerceProductVariant1 {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("sku")
  private String sku = null;

  @JsonProperty("price")
  private BigDecimal price = null;

  @JsonProperty("inventory_quantity")
  private Integer inventoryQuantity = null;

  @JsonProperty("image_url")
  private String imageUrl = null;

  @JsonProperty("backorders")
  private String backorders = null;

  @JsonProperty("visibility")
  private String visibility = null;

  public EcommerceProductVariant1 title(String title) {
    this.title = title;
    return this;
  }

   /**
   * The title of a product variant.
   * @return title
  **/
  @Schema(example = "Cat Hat", description = "The title of a product variant.")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public EcommerceProductVariant1 url(String url) {
    this.url = url;
    return this;
  }

   /**
   * The URL for a product variant.
   * @return url
  **/
  @Schema(description = "The URL for a product variant.")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public EcommerceProductVariant1 sku(String sku) {
    this.sku = sku;
    return this;
  }

   /**
   * The stock keeping unit (SKU) of a product variant.
   * @return sku
  **/
  @Schema(description = "The stock keeping unit (SKU) of a product variant.")
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public EcommerceProductVariant1 price(BigDecimal price) {
    this.price = price;
    return this;
  }

   /**
   * The price of a product variant.
   * @return price
  **/
  @Schema(description = "The price of a product variant.")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public EcommerceProductVariant1 inventoryQuantity(Integer inventoryQuantity) {
    this.inventoryQuantity = inventoryQuantity;
    return this;
  }

   /**
   * The inventory quantity of a product variant.
   * @return inventoryQuantity
  **/
  @Schema(description = "The inventory quantity of a product variant.")
  public Integer getInventoryQuantity() {
    return inventoryQuantity;
  }

  public void setInventoryQuantity(Integer inventoryQuantity) {
    this.inventoryQuantity = inventoryQuantity;
  }

  public EcommerceProductVariant1 imageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

   /**
   * The image URL for a product variant.
   * @return imageUrl
  **/
  @Schema(description = "The image URL for a product variant.")
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public EcommerceProductVariant1 backorders(String backorders) {
    this.backorders = backorders;
    return this;
  }

   /**
   * The backorders of a product variant.
   * @return backorders
  **/
  @Schema(description = "The backorders of a product variant.")
  public String getBackorders() {
    return backorders;
  }

  public void setBackorders(String backorders) {
    this.backorders = backorders;
  }

  public EcommerceProductVariant1 visibility(String visibility) {
    this.visibility = visibility;
    return this;
  }

   /**
   * The visibility of a product variant.
   * @return visibility
  **/
  @Schema(description = "The visibility of a product variant.")
  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EcommerceProductVariant1 ecommerceProductVariant1 = (EcommerceProductVariant1) o;
    return Objects.equals(this.title, ecommerceProductVariant1.title) &&
        Objects.equals(this.url, ecommerceProductVariant1.url) &&
        Objects.equals(this.sku, ecommerceProductVariant1.sku) &&
        Objects.equals(this.price, ecommerceProductVariant1.price) &&
        Objects.equals(this.inventoryQuantity, ecommerceProductVariant1.inventoryQuantity) &&
        Objects.equals(this.imageUrl, ecommerceProductVariant1.imageUrl) &&
        Objects.equals(this.backorders, ecommerceProductVariant1.backorders) &&
        Objects.equals(this.visibility, ecommerceProductVariant1.visibility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, url, sku, price, inventoryQuantity, imageUrl, backorders, visibility);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcommerceProductVariant1 {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    inventoryQuantity: ").append(toIndentedString(inventoryQuantity)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    backorders: ").append(toIndentedString(backorders)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
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
