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
 * The most recent message in the conversation.
 */
@Schema(description = "The most recent message in the conversation.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class LastMessage {
  @JsonProperty("from_label")
  private String fromLabel = null;

  @JsonProperty("from_email")
  private String fromEmail = null;

  @JsonProperty("subject")
  private String subject = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("read")
  private Boolean read = null;

  @JsonProperty("timestamp")
  private DateTime timestamp = null;

   /**
   * A label representing the sender of this message.
   * @return fromLabel
  **/
  @Schema(description = "A label representing the sender of this message.")
  public String getFromLabel() {
    return fromLabel;
  }

   /**
   * A label representing the email of the sender of this message.
   * @return fromEmail
  **/
  @Schema(description = "A label representing the email of the sender of this message.")
  public String getFromEmail() {
    return fromEmail;
  }

   /**
   * The subject of this message.
   * @return subject
  **/
  @Schema(description = "The subject of this message.")
  public String getSubject() {
    return subject;
  }

   /**
   * The plain-text content of the message.
   * @return message
  **/
  @Schema(description = "The plain-text content of the message.")
  public String getMessage() {
    return message;
  }

  public LastMessage read(Boolean read) {
    this.read = read;
    return this;
  }

   /**
   * Whether this message has been marked as read.
   * @return read
  **/
  @Schema(description = "Whether this message has been marked as read.")
  public Boolean isRead() {
    return read;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

   /**
   * The date and time the message was either sent or received in ISO 8601 format.
   * @return timestamp
  **/
  @Schema(description = "The date and time the message was either sent or received in ISO 8601 format.")
  public DateTime getTimestamp() {
    return timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LastMessage lastMessage = (LastMessage) o;
    return Objects.equals(this.fromLabel, lastMessage.fromLabel) &&
        Objects.equals(this.fromEmail, lastMessage.fromEmail) &&
        Objects.equals(this.subject, lastMessage.subject) &&
        Objects.equals(this.message, lastMessage.message) &&
        Objects.equals(this.read, lastMessage.read) &&
        Objects.equals(this.timestamp, lastMessage.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromLabel, fromEmail, subject, message, read, timestamp);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LastMessage {\n");
    
    sb.append("    fromLabel: ").append(toIndentedString(fromLabel)).append("\n");
    sb.append("    fromEmail: ").append(toIndentedString(fromEmail)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    read: ").append(toIndentedString(read)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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