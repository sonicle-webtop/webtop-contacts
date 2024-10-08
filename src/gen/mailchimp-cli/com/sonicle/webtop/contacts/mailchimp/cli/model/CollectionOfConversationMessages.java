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
import com.sonicle.webtop.contacts.mailchimp.cli.model.ConversationMessage2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * Messages from a specific conversation.
 */
@Schema(description = "Messages from a specific conversation.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class CollectionOfConversationMessages {
  @JsonProperty("conversation_messages")
  private List<ConversationMessage2> conversationMessages = null;

  @JsonProperty("conversation_id")
  private String conversationId = null;

  @JsonProperty("total_items")
  private Integer totalItems = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  public CollectionOfConversationMessages conversationMessages(List<ConversationMessage2> conversationMessages) {
    this.conversationMessages = conversationMessages;
    return this;
  }

  public CollectionOfConversationMessages addConversationMessagesItem(ConversationMessage2 conversationMessagesItem) {
    if (this.conversationMessages == null) {
      this.conversationMessages = new ArrayList<>();
    }
    this.conversationMessages.add(conversationMessagesItem);
    return this;
  }

   /**
   * An array of objects, each representing a conversation messages resources.
   * @return conversationMessages
  **/
  @Schema(description = "An array of objects, each representing a conversation messages resources.")
  public List<ConversationMessage2> getConversationMessages() {
    return conversationMessages;
  }

  public void setConversationMessages(List<ConversationMessage2> conversationMessages) {
    this.conversationMessages = conversationMessages;
  }

  public CollectionOfConversationMessages conversationId(String conversationId) {
    this.conversationId = conversationId;
    return this;
  }

   /**
   * A string that identifies this conversation.
   * @return conversationId
  **/
  @Schema(description = "A string that identifies this conversation.")
  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
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
    CollectionOfConversationMessages collectionOfConversationMessages = (CollectionOfConversationMessages) o;
    return Objects.equals(this.conversationMessages, collectionOfConversationMessages.conversationMessages) &&
        Objects.equals(this.conversationId, collectionOfConversationMessages.conversationId) &&
        Objects.equals(this.totalItems, collectionOfConversationMessages.totalItems) &&
        Objects.equals(this._links, collectionOfConversationMessages._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conversationMessages, conversationId, totalItems, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionOfConversationMessages {\n");
    
    sb.append("    conversationMessages: ").append(toIndentedString(conversationMessages)).append("\n");
    sb.append("    conversationId: ").append(toIndentedString(conversationId)).append("\n");
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
