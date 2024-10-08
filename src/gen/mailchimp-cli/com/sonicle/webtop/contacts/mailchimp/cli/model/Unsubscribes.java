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
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Unsubscribes2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * A list of members who have unsubscribed from a specific campaign.
 */
@Schema(description = "A list of members who have unsubscribed from a specific campaign.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class Unsubscribes {
  @JsonProperty("unsubscribes")
  private List<Unsubscribes2> unsubscribes = null;

  @JsonProperty("campaign_id")
  private String campaignId = null;

  @JsonProperty("total_items")
  private Integer totalItems = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  public Unsubscribes unsubscribes(List<Unsubscribes2> unsubscribes) {
    this.unsubscribes = unsubscribes;
    return this;
  }

  public Unsubscribes addUnsubscribesItem(Unsubscribes2 unsubscribesItem) {
    if (this.unsubscribes == null) {
      this.unsubscribes = new ArrayList<>();
    }
    this.unsubscribes.add(unsubscribesItem);
    return this;
  }

   /**
   * An array of objects, each representing a member who unsubscribed from a campaign.
   * @return unsubscribes
  **/
  @Schema(description = "An array of objects, each representing a member who unsubscribed from a campaign.")
  public List<Unsubscribes2> getUnsubscribes() {
    return unsubscribes;
  }

  public void setUnsubscribes(List<Unsubscribes2> unsubscribes) {
    this.unsubscribes = unsubscribes;
  }

   /**
   * The campaign id.
   * @return campaignId
  **/
  @Schema(description = "The campaign id.")
  public String getCampaignId() {
    return campaignId;
  }

   /**
   * The total number of items matching the query regardless of pagination.
   * @return totalItems
  **/
  @Schema(description = "The total number of items matching the query regardless of pagination.")
  public Integer getTotalItems() {
    return totalItems;
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
    Unsubscribes unsubscribes = (Unsubscribes) o;
    return Objects.equals(this.unsubscribes, unsubscribes.unsubscribes) &&
        Objects.equals(this.campaignId, unsubscribes.campaignId) &&
        Objects.equals(this.totalItems, unsubscribes.totalItems) &&
        Objects.equals(this._links, unsubscribes._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unsubscribes, campaignId, totalItems, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Unsubscribes {\n");
    
    sb.append("    unsubscribes: ").append(toIndentedString(unsubscribes)).append("\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
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
