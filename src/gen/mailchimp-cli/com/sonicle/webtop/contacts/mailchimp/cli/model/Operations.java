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
 * Operations
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class Operations {
  /**
   * The HTTP method to use for the operation.
   */
  public enum MethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH");

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

  @JsonProperty("path")
  private String path = null;

  @JsonProperty("params")
  private Object params = null;

  @JsonProperty("body")
  private String body = null;

  @JsonProperty("operation_id")
  private String operationId = null;

  public Operations method(MethodEnum method) {
    this.method = method;
    return this;
  }

   /**
   * The HTTP method to use for the operation.
   * @return method
  **/
  @Schema(example = "POST", required = true, description = "The HTTP method to use for the operation.")
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  public Operations path(String path) {
    this.path = path;
    return this;
  }

   /**
   * The relative path to use for the operation.
   * @return path
  **/
  @Schema(example = "/lists", required = true, description = "The relative path to use for the operation.")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Operations params(Object params) {
    this.params = params;
    return this;
  }

   /**
   * Any request query parameters. Example parameters: {\&quot;count\&quot;:10, \&quot;offset\&quot;:0}
   * @return params
  **/
  @Schema(example = "{\"count\":10,\"offset\":0}", description = "Any request query parameters. Example parameters: {\"count\":10, \"offset\":0}")
  public Object getParams() {
    return params;
  }

  public void setParams(Object params) {
    this.params = params;
  }

  public Operations body(String body) {
    this.body = body;
    return this;
  }

   /**
   * A string containing the JSON body to use with the request.
   * @return body
  **/
  @Schema(example = "{\"title\":\"Test\"}", description = "A string containing the JSON body to use with the request.")
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Operations operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

   /**
   * An optional client-supplied id returned with the operation results.
   * @return operationId
  **/
  @Schema(example = "my-id-123", description = "An optional client-supplied id returned with the operation results.")
  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Operations operations = (Operations) o;
    return Objects.equals(this.method, operations.method) &&
        Objects.equals(this.path, operations.path) &&
        Objects.equals(this.params, operations.params) &&
        Objects.equals(this.body, operations.body) &&
        Objects.equals(this.operationId, operations.operationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, path, params, body, operationId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operations {\n");
    
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    params: ").append(toIndentedString(params)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
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