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
import org.joda.time.DateTime;
/**
 * A Chimp Chatter message
 */
@Schema(description = "A Chimp Chatter message")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class ChimpChatter {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("message")
  private String message = null;

  /**
   * The type of activity
   */
  public enum TypeEnum {
    LISTS_NEW_SUBSCRIBER("lists:new-subscriber"),
    LISTS_UNSUBSCRIBES("lists:unsubscribes"),
    LISTS_PROFILE_UPDATES("lists:profile-updates"),
    CAMPAIGNS_FACEBOOK_LIKES("campaigns:facebook-likes"),
    CAMPAIGNS_FORWARD_TO_FRIEND("campaigns:forward-to-friend"),
    LISTS_IMPORTS("lists:imports");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("update_time")
  private DateTime updateTime = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("list_id")
  private String listId = null;

  @JsonProperty("campaign_id")
  private String campaignId = null;

   /**
   * Get title
   * @return title
  **/
  @Schema(example = "1 new subscriber to Your New Campaign!", description = "")
  public String getTitle() {
    return title;
  }

   /**
   * Get message
   * @return message
  **/
  @Schema(example = "People are telling their friends about your campaign!", description = "")
  public String getMessage() {
    return message;
  }

   /**
   * The type of activity
   * @return type
  **/
  @Schema(example = "campaigns:forward-to-friend", description = "The type of activity")
  public TypeEnum getType() {
    return type;
  }

   /**
   * The date and time this activity was updated.
   * @return updateTime
  **/
  @Schema(example = "2017-08-04T11:09:01Z", description = "The date and time this activity was updated.")
  public DateTime getUpdateTime() {
    return updateTime;
  }

   /**
   * URL to a report that includes this activity
   * @return url
  **/
  @Schema(example = "http://dev.mailchimp.com/reports/summary?id=1", description = "URL to a report that includes this activity")
  public String getUrl() {
    return url;
  }

   /**
   * If it exists, list ID for the associated list
   * @return listId
  **/
  @Schema(example = "2017-08-04T11:09:01+00:00", description = "If it exists, list ID for the associated list")
  public String getListId() {
    return listId;
  }

   /**
   * If it exists, campaign ID for the associated campaign
   * @return campaignId
  **/
  @Schema(example = "2017-08-04T11:09:01+00:00", description = "If it exists, campaign ID for the associated campaign")
  public String getCampaignId() {
    return campaignId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChimpChatter chimpChatter = (ChimpChatter) o;
    return Objects.equals(this.title, chimpChatter.title) &&
        Objects.equals(this.message, chimpChatter.message) &&
        Objects.equals(this.type, chimpChatter.type) &&
        Objects.equals(this.updateTime, chimpChatter.updateTime) &&
        Objects.equals(this.url, chimpChatter.url) &&
        Objects.equals(this.listId, chimpChatter.listId) &&
        Objects.equals(this.campaignId, chimpChatter.campaignId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, message, type, updateTime, url, listId, campaignId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChimpChatter {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
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