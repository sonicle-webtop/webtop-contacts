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
import com.sonicle.webtop.contacts.mailchimp.cli.model.SegmentOptions2;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * List settings for the Automation.
 */
@Schema(description = "List settings for the Automation.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class List9 {
  @JsonProperty("list_id")
  private String listId = null;

  @JsonProperty("list_is_active")
  private Boolean listIsActive = null;

  @JsonProperty("list_name")
  private String listName = null;

  @JsonProperty("segment_opts")
  private SegmentOptions2 segmentOpts = null;

  @JsonProperty("store_id")
  private String storeId = null;

  public List9 listId(String listId) {
    this.listId = listId;
    return this;
  }

   /**
   * The unique list id.
   * @return listId
  **/
  @Schema(description = "The unique list id.")
  public String getListId() {
    return listId;
  }

  public void setListId(String listId) {
    this.listId = listId;
  }

   /**
   * The status of the list used, namely if it&#x27;s deleted or disabled.
   * @return listIsActive
  **/
  @Schema(description = "The status of the list used, namely if it's deleted or disabled.")
  public Boolean isListIsActive() {
    return listIsActive;
  }

  public List9 listName(String listName) {
    this.listName = listName;
    return this;
  }

   /**
   * List Name.
   * @return listName
  **/
  @Schema(description = "List Name.")
  public String getListName() {
    return listName;
  }

  public void setListName(String listName) {
    this.listName = listName;
  }

  public List9 segmentOpts(SegmentOptions2 segmentOpts) {
    this.segmentOpts = segmentOpts;
    return this;
  }

   /**
   * Get segmentOpts
   * @return segmentOpts
  **/
  @Schema(description = "")
  public SegmentOptions2 getSegmentOpts() {
    return segmentOpts;
  }

  public void setSegmentOpts(SegmentOptions2 segmentOpts) {
    this.segmentOpts = segmentOpts;
  }

  public List9 storeId(String storeId) {
    this.storeId = storeId;
    return this;
  }

   /**
   * The id of the store.
   * @return storeId
  **/
  @Schema(example = "1a2df69xxx", description = "The id of the store.")
  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    List9 list9 = (List9) o;
    return Objects.equals(this.listId, list9.listId) &&
        Objects.equals(this.listIsActive, list9.listIsActive) &&
        Objects.equals(this.listName, list9.listName) &&
        Objects.equals(this.segmentOpts, list9.segmentOpts) &&
        Objects.equals(this.storeId, list9.storeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(listId, listIsActive, listName, segmentOpts, storeId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class List9 {\n");
    
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
    sb.append("    listIsActive: ").append(toIndentedString(listIsActive)).append("\n");
    sb.append("    listName: ").append(toIndentedString(listName)).append("\n");
    sb.append("    segmentOpts: ").append(toIndentedString(segmentOpts)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
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