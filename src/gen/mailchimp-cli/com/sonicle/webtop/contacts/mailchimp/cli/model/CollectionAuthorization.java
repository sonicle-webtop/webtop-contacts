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
 * Do particular authorization constraints around this collection limit creation of new instances?
 */
@Schema(description = "Do particular authorization constraints around this collection limit creation of new instances?")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class CollectionAuthorization {
  @JsonProperty("may_create")
  private Boolean mayCreate = null;

  @JsonProperty("max_instances")
  private Integer maxInstances = null;

  @JsonProperty("current_total_instances")
  private Integer currentTotalInstances = null;

  public CollectionAuthorization mayCreate(Boolean mayCreate) {
    this.mayCreate = mayCreate;
    return this;
  }

   /**
   * May the user create additional instances of this resource?
   * @return mayCreate
  **/
  @Schema(required = true, description = "May the user create additional instances of this resource?")
  public Boolean isMayCreate() {
    return mayCreate;
  }

  public void setMayCreate(Boolean mayCreate) {
    this.mayCreate = mayCreate;
  }

  public CollectionAuthorization maxInstances(Integer maxInstances) {
    this.maxInstances = maxInstances;
    return this;
  }

   /**
   * How many total instances of this resource are allowed? This is independent of any filter conditions applied to the query. As a special case, -1 indicates unlimited.
   * @return maxInstances
  **/
  @Schema(required = true, description = "How many total instances of this resource are allowed? This is independent of any filter conditions applied to the query. As a special case, -1 indicates unlimited.")
  public Integer getMaxInstances() {
    return maxInstances;
  }

  public void setMaxInstances(Integer maxInstances) {
    this.maxInstances = maxInstances;
  }

  public CollectionAuthorization currentTotalInstances(Integer currentTotalInstances) {
    this.currentTotalInstances = currentTotalInstances;
    return this;
  }

   /**
   * How many total instances of this resource are already in use? This is independent of any filter conditions applied to the query. Value may be larger than max_instances. As a special case, -1 is returned when access is unlimited.
   * @return currentTotalInstances
  **/
  @Schema(description = "How many total instances of this resource are already in use? This is independent of any filter conditions applied to the query. Value may be larger than max_instances. As a special case, -1 is returned when access is unlimited.")
  public Integer getCurrentTotalInstances() {
    return currentTotalInstances;
  }

  public void setCurrentTotalInstances(Integer currentTotalInstances) {
    this.currentTotalInstances = currentTotalInstances;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionAuthorization collectionAuthorization = (CollectionAuthorization) o;
    return Objects.equals(this.mayCreate, collectionAuthorization.mayCreate) &&
        Objects.equals(this.maxInstances, collectionAuthorization.maxInstances) &&
        Objects.equals(this.currentTotalInstances, collectionAuthorization.currentTotalInstances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mayCreate, maxInstances, currentTotalInstances);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionAuthorization {\n");
    
    sb.append("    mayCreate: ").append(toIndentedString(mayCreate)).append("\n");
    sb.append("    maxInstances: ").append(toIndentedString(maxInstances)).append("\n");
    sb.append("    currentTotalInstances: ").append(toIndentedString(currentTotalInstances)).append("\n");
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