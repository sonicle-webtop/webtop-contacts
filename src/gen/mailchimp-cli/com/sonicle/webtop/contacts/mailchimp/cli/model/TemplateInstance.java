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
 * Information about a specific template.
 */
@Schema(description = "Information about a specific template.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class TemplateInstance {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("folder_id")
  private String folderId = null;

  @JsonProperty("html")
  private String html = null;

  public TemplateInstance name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the template.
   * @return name
  **/
  @Schema(example = "Freddie's Jokes", required = true, description = "The name of the template.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TemplateInstance folderId(String folderId) {
    this.folderId = folderId;
    return this;
  }

   /**
   * The id of the folder the template is currently in.
   * @return folderId
  **/
  @Schema(example = "a4b830b", description = "The id of the folder the template is currently in.")
  public String getFolderId() {
    return folderId;
  }

  public void setFolderId(String folderId) {
    this.folderId = folderId;
  }

  public TemplateInstance html(String html) {
    this.html = html;
    return this;
  }

   /**
   * The raw HTML for the template. We  support the Mailchimp [Template Language](https://mailchimp.com/help/getting-started-with-mailchimps-template-language/) in any HTML code passed via the API.
   * @return html
  **/
  @Schema(required = true, description = "The raw HTML for the template. We  support the Mailchimp [Template Language](https://mailchimp.com/help/getting-started-with-mailchimps-template-language/) in any HTML code passed via the API.")
  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateInstance templateInstance = (TemplateInstance) o;
    return Objects.equals(this.name, templateInstance.name) &&
        Objects.equals(this.folderId, templateInstance.folderId) &&
        Objects.equals(this.html, templateInstance.html);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, folderId, html);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateInstance {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    folderId: ").append(toIndentedString(folderId)).append("\n");
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
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