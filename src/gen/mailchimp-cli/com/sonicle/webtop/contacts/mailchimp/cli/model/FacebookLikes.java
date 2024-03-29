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
 * An object describing campaign engagement on Facebook.
 */
@Schema(description = "An object describing campaign engagement on Facebook.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class FacebookLikes {
  @JsonProperty("recipient_likes")
  private Integer recipientLikes = null;

  @JsonProperty("unique_likes")
  private Integer uniqueLikes = null;

  @JsonProperty("facebook_likes")
  private Integer facebookLikes = null;

  public FacebookLikes recipientLikes(Integer recipientLikes) {
    this.recipientLikes = recipientLikes;
    return this;
  }

   /**
   * The number of recipients who liked the campaign on Facebook.
   * @return recipientLikes
  **/
  @Schema(description = "The number of recipients who liked the campaign on Facebook.")
  public Integer getRecipientLikes() {
    return recipientLikes;
  }

  public void setRecipientLikes(Integer recipientLikes) {
    this.recipientLikes = recipientLikes;
  }

  public FacebookLikes uniqueLikes(Integer uniqueLikes) {
    this.uniqueLikes = uniqueLikes;
    return this;
  }

   /**
   * The number of unique likes.
   * @return uniqueLikes
  **/
  @Schema(description = "The number of unique likes.")
  public Integer getUniqueLikes() {
    return uniqueLikes;
  }

  public void setUniqueLikes(Integer uniqueLikes) {
    this.uniqueLikes = uniqueLikes;
  }

  public FacebookLikes facebookLikes(Integer facebookLikes) {
    this.facebookLikes = facebookLikes;
    return this;
  }

   /**
   * The number of Facebook likes for the campaign.
   * @return facebookLikes
  **/
  @Schema(description = "The number of Facebook likes for the campaign.")
  public Integer getFacebookLikes() {
    return facebookLikes;
  }

  public void setFacebookLikes(Integer facebookLikes) {
    this.facebookLikes = facebookLikes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FacebookLikes facebookLikes = (FacebookLikes) o;
    return Objects.equals(this.recipientLikes, facebookLikes.recipientLikes) &&
        Objects.equals(this.uniqueLikes, facebookLikes.uniqueLikes) &&
        Objects.equals(this.facebookLikes, facebookLikes.facebookLikes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipientLikes, uniqueLikes, facebookLikes);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FacebookLikes {\n");
    
    sb.append("    recipientLikes: ").append(toIndentedString(recipientLikes)).append("\n");
    sb.append("    uniqueLikes: ").append(toIndentedString(uniqueLikes)).append("\n");
    sb.append("    facebookLikes: ").append(toIndentedString(facebookLikes)).append("\n");
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
