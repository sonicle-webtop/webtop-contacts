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
import com.sonicle.webtop.contacts.mailchimp.cli.model.OpenLocationsLocations;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * Top open locations for a specific campaign.
 */
@Schema(description = "Top open locations for a specific campaign.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class OpenLocations {
  @JsonProperty("locations")
  private List<OpenLocationsLocations> locations = null;

  @JsonProperty("campaign_id")
  private String campaignId = null;

  @JsonProperty("total_items")
  private Integer totalItems = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  public OpenLocations locations(List<OpenLocationsLocations> locations) {
    this.locations = locations;
    return this;
  }

  public OpenLocations addLocationsItem(OpenLocationsLocations locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

   /**
   * An array of objects, each representing a top location for opens.
   * @return locations
  **/
  @Schema(description = "An array of objects, each representing a top location for opens.")
  public List<OpenLocationsLocations> getLocations() {
    return locations;
  }

  public void setLocations(List<OpenLocationsLocations> locations) {
    this.locations = locations;
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
    OpenLocations openLocations = (OpenLocations) o;
    return Objects.equals(this.locations, openLocations.locations) &&
        Objects.equals(this.campaignId, openLocations.campaignId) &&
        Objects.equals(this.totalItems, openLocations.totalItems) &&
        Objects.equals(this._links, openLocations._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locations, campaignId, totalItems, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OpenLocations {\n");
    
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
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
