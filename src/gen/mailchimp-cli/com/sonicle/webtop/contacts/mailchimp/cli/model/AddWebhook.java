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
import com.sonicle.webtop.contacts.mailchimp.cli.model.Events1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Sources;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Configure a webhook for the given list.
 */
@Schema(description = "Configure a webhook for the given list.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class AddWebhook {
  @JsonProperty("url")
  private String url = null;

  @JsonProperty("events")
  private Events1 events = null;

  @JsonProperty("sources")
  private Sources sources = null;

  public AddWebhook url(String url) {
    this.url = url;
    return this;
  }

   /**
   * A valid URL for the Webhook.
   * @return url
  **/
  @Schema(example = "http://yourdomain.com/webhook", description = "A valid URL for the Webhook.")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public AddWebhook events(Events1 events) {
    this.events = events;
    return this;
  }

   /**
   * Get events
   * @return events
  **/
  @Schema(description = "")
  public Events1 getEvents() {
    return events;
  }

  public void setEvents(Events1 events) {
    this.events = events;
  }

  public AddWebhook sources(Sources sources) {
    this.sources = sources;
    return this;
  }

   /**
   * Get sources
   * @return sources
  **/
  @Schema(description = "")
  public Sources getSources() {
    return sources;
  }

  public void setSources(Sources sources) {
    this.sources = sources;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddWebhook addWebhook = (AddWebhook) o;
    return Objects.equals(this.url, addWebhook.url) &&
        Objects.equals(this.events, addWebhook.events) &&
        Objects.equals(this.sources, addWebhook.sources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, events, sources);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddWebhook {\n");
    
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    sources: ").append(toIndentedString(sources)).append("\n");
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