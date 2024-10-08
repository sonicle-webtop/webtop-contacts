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
/**
 * A single marketing permission a subscriber has either opted-in to or opted-out of.
 */
@Schema(description = "A single marketing permission a subscriber has either opted-in to or opted-out of.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class MarketingPermission1 {
  @JsonProperty("marketing_permission_id")
  private String marketingPermissionId = null;

  @JsonProperty("text")
  private String text = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  public MarketingPermission1 marketingPermissionId(String marketingPermissionId) {
    this.marketingPermissionId = marketingPermissionId;
    return this;
  }

   /**
   * The id for the marketing permission on the list
   * @return marketingPermissionId
  **/
  @Schema(description = "The id for the marketing permission on the list")
  public String getMarketingPermissionId() {
    return marketingPermissionId;
  }

  public void setMarketingPermissionId(String marketingPermissionId) {
    this.marketingPermissionId = marketingPermissionId;
  }

  public MarketingPermission1 text(String text) {
    this.text = text;
    return this;
  }

   /**
   * The text of the marketing permission.
   * @return text
  **/
  @Schema(description = "The text of the marketing permission.")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public MarketingPermission1 enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * If the subscriber has opted-in to the marketing permission.
   * @return enabled
  **/
  @Schema(description = "If the subscriber has opted-in to the marketing permission.")
  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketingPermission1 marketingPermission1 = (MarketingPermission1) o;
    return Objects.equals(this.marketingPermissionId, marketingPermission1.marketingPermissionId) &&
        Objects.equals(this.text, marketingPermission1.text) &&
        Objects.equals(this.enabled, marketingPermission1.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(marketingPermissionId, text, enabled);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketingPermission1 {\n");
    
    sb.append("    marketingPermissionId: ").append(toIndentedString(marketingPermissionId)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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
