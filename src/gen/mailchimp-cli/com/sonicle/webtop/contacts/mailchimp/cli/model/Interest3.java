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
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * Assign subscribers to interests to group them together. Interests are referred to as &#x27;group names&#x27; in the Mailchimp application.
 */
@Schema(description = "Assign subscribers to interests to group them together. Interests are referred to as 'group names' in the Mailchimp application.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class Interest3 {
  @JsonProperty("category_id")
  private String categoryId = null;

  @JsonProperty("list_id")
  private String listId = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("subscriber_count")
  private String subscriberCount = null;

  @JsonProperty("display_order")
  private Integer displayOrder = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * The id for the interest category.
   * @return categoryId
  **/
  @Schema(description = "The id for the interest category.")
  public String getCategoryId() {
    return categoryId;
  }

   /**
   * The ID for the list that this interest belongs to.
   * @return listId
  **/
  @Schema(description = "The ID for the list that this interest belongs to.")
  public String getListId() {
    return listId;
  }

   /**
   * The ID for the interest.
   * @return id
  **/
  @Schema(description = "The ID for the interest.")
  public String getId() {
    return id;
  }

  public Interest3 name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the interest. This can be shown publicly on a subscription form.
   * @return name
  **/
  @Schema(description = "The name of the interest. This can be shown publicly on a subscription form.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

   /**
   * The number of subscribers associated with this interest.
   * @return subscriberCount
  **/
  @Schema(description = "The number of subscribers associated with this interest.")
  public String getSubscriberCount() {
    return subscriberCount;
  }

  public Interest3 displayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

   /**
   * The display order for interests.
   * @return displayOrder
  **/
  @Schema(description = "The display order for interests.")
  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
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
    Interest3 interest3 = (Interest3) o;
    return Objects.equals(this.categoryId, interest3.categoryId) &&
        Objects.equals(this.listId, interest3.listId) &&
        Objects.equals(this.id, interest3.id) &&
        Objects.equals(this.name, interest3.name) &&
        Objects.equals(this.subscriberCount, interest3.subscriberCount) &&
        Objects.equals(this.displayOrder, interest3.displayOrder) &&
        Objects.equals(this._links, interest3._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryId, listId, id, name, subscriberCount, displayOrder, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Interest3 {\n");
    
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    subscriberCount: ").append(toIndentedString(subscriberCount)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
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
