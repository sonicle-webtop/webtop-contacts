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
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionAuthorization;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList3;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * A collection of subscriber lists for this account. Lists contain subscribers who have opted-in to receive correspondence from you or your organization.
 */
@Schema(description = "A collection of subscriber lists for this account. Lists contain subscribers who have opted-in to receive correspondence from you or your organization.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class SubscriberLists {
  @JsonProperty("lists")
  private List<SubscriberList3> lists = new ArrayList<>();

  @JsonProperty("total_items")
  private Integer totalItems = null;

  @JsonProperty("constraints")
  private CollectionAuthorization constraints = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  public SubscriberLists lists(List<SubscriberList3> lists) {
    this.lists = lists;
    return this;
  }

  public SubscriberLists addListsItem(SubscriberList3 listsItem) {
    this.lists.add(listsItem);
    return this;
  }

   /**
   * An array of objects, each representing a list.
   * @return lists
  **/
  @Schema(required = true, description = "An array of objects, each representing a list.")
  public List<SubscriberList3> getLists() {
    return lists;
  }

  public void setLists(List<SubscriberList3> lists) {
    this.lists = lists;
  }

   /**
   * The total number of items matching the query regardless of pagination.
   * @return totalItems
  **/
  @Schema(description = "The total number of items matching the query regardless of pagination.")
  public Integer getTotalItems() {
    return totalItems;
  }

  public SubscriberLists constraints(CollectionAuthorization constraints) {
    this.constraints = constraints;
    return this;
  }

   /**
   * Get constraints
   * @return constraints
  **/
  @Schema(description = "")
  public CollectionAuthorization getConstraints() {
    return constraints;
  }

  public void setConstraints(CollectionAuthorization constraints) {
    this.constraints = constraints;
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
    SubscriberLists subscriberLists = (SubscriberLists) o;
    return Objects.equals(this.lists, subscriberLists.lists) &&
        Objects.equals(this.totalItems, subscriberLists.totalItems) &&
        Objects.equals(this.constraints, subscriberLists.constraints) &&
        Objects.equals(this._links, subscriberLists._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lists, totalItems, constraints, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriberLists {\n");
    
    sb.append("    lists: ").append(toIndentedString(lists)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
    sb.append("    constraints: ").append(toIndentedString(constraints)).append("\n");
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