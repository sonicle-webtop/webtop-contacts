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
 * CollectionOfTagsTags
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class CollectionOfTagsTags {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("date_added")
  private DateTime dateAdded = null;

   /**
   * The unique id for the tag.
   * @return id
  **/
  @Schema(description = "The unique id for the tag.")
  public Integer getId() {
    return id;
  }

  public CollectionOfTagsTags name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the tag.
   * @return name
  **/
  @Schema(description = "The name of the tag.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

   /**
   * The date and time the tag was added to the list member in ISO 8601 format.
   * @return dateAdded
  **/
  @Schema(description = "The date and time the tag was added to the list member in ISO 8601 format.")
  public DateTime getDateAdded() {
    return dateAdded;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionOfTagsTags collectionOfTagsTags = (CollectionOfTagsTags) o;
    return Objects.equals(this.id, collectionOfTagsTags.id) &&
        Objects.equals(this.name, collectionOfTagsTags.name) &&
        Objects.equals(this.dateAdded, collectionOfTagsTags.dateAdded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, dateAdded);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionOfTagsTags {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dateAdded: ").append(toIndentedString(dateAdded)).append("\n");
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