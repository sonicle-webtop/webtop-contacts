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
import com.sonicle.webtop.contacts.mailchimp.cli.model.EmailClient;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * The top email clients based on user-agent strings.
 */
@Schema(description = "The top email clients based on user-agent strings.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EmailClients {
  @JsonProperty("clients")
  private List<EmailClient> clients = null;

  @JsonProperty("list_id")
  private String listId = null;

  @JsonProperty("total_items")
  private Integer totalItems = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  public EmailClients clients(List<EmailClient> clients) {
    this.clients = clients;
    return this;
  }

  public EmailClients addClientsItem(EmailClient clientsItem) {
    if (this.clients == null) {
      this.clients = new ArrayList<>();
    }
    this.clients.add(clientsItem);
    return this;
  }

   /**
   * An array of top email clients.
   * @return clients
  **/
  @Schema(description = "An array of top email clients.")
  public List<EmailClient> getClients() {
    return clients;
  }

  public void setClients(List<EmailClient> clients) {
    this.clients = clients;
  }

   /**
   * The list id.
   * @return listId
  **/
  @Schema(description = "The list id.")
  public String getListId() {
    return listId;
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
    EmailClients emailClients = (EmailClients) o;
    return Objects.equals(this.clients, emailClients.clients) &&
        Objects.equals(this.listId, emailClients.listId) &&
        Objects.equals(this.totalItems, emailClients.totalItems) &&
        Objects.equals(this._links, emailClients._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clients, listId, totalItems, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmailClients {\n");
    
    sb.append("    clients: ").append(toIndentedString(clients)).append("\n");
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
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