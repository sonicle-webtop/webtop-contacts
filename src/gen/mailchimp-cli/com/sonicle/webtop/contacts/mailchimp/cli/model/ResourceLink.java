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
 * This object represents a link from the resource where it is found to another resource or action that may be performed.
 */
@Schema(description = "This object represents a link from the resource where it is found to another resource or action that may be performed.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class ResourceLink {
  @JsonProperty("rel")
  private String rel = null;

  @JsonProperty("href")
  private String href = null;

  /**
   * The HTTP method that should be used when accessing the URL defined in &#x27;href&#x27;.
   */
  public enum MethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD");

    private String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String text) {
      for (MethodEnum b : MethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("method")
  private MethodEnum method = null;

  @JsonProperty("targetSchema")
  private String targetSchema = null;

  @JsonProperty("schema")
  private String schema = null;

   /**
   * As with an HTML &#x27;rel&#x27; attribute, this describes the type of link.
   * @return rel
  **/
  @Schema(description = "As with an HTML 'rel' attribute, this describes the type of link.")
  public String getRel() {
    return rel;
  }

   /**
   * This property contains a fully-qualified URL that can be called to retrieve the linked resource or perform the linked action.
   * @return href
  **/
  @Schema(description = "This property contains a fully-qualified URL that can be called to retrieve the linked resource or perform the linked action.")
  public String getHref() {
    return href;
  }

   /**
   * The HTTP method that should be used when accessing the URL defined in &#x27;href&#x27;.
   * @return method
  **/
  @Schema(description = "The HTTP method that should be used when accessing the URL defined in 'href'.")
  public MethodEnum getMethod() {
    return method;
  }

   /**
   * For GETs, this is a URL representing the schema that the response should conform to.
   * @return targetSchema
  **/
  @Schema(description = "For GETs, this is a URL representing the schema that the response should conform to.")
  public String getTargetSchema() {
    return targetSchema;
  }

   /**
   * For HTTP methods that can receive bodies (POST and PUT), this is a URL representing the schema that the body should conform to.
   * @return schema
  **/
  @Schema(description = "For HTTP methods that can receive bodies (POST and PUT), this is a URL representing the schema that the body should conform to.")
  public String getSchema() {
    return schema;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceLink resourceLink = (ResourceLink) o;
    return Objects.equals(this.rel, resourceLink.rel) &&
        Objects.equals(this.href, resourceLink.href) &&
        Objects.equals(this.method, resourceLink.method) &&
        Objects.equals(this.targetSchema, resourceLink.targetSchema) &&
        Objects.equals(this.schema, resourceLink.schema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rel, href, method, targetSchema, schema);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResourceLink {\n");
    
    sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    targetSchema: ").append(toIndentedString(targetSchema)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
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
