package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContact;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represent a response object that returns a collection of contacts.
 **/
@ApiModel(description = "Represent a response object that returns a collection of contacts.")
@JsonTypeName("ContactsResult")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-06T10:04:09.242+01:00[Europe/Berlin]")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiContactsResult   {
  private @Valid Integer totalCount;
  private @Valid List<ApiContact> items = new ArrayList<>();

  /**
   * Items total count, usually used for pagination.
   **/
  public ApiContactsResult totalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  
  @ApiModelProperty(value = "Items total count, usually used for pagination.")
  @JsonProperty("totalCount")
  public Integer getTotalCount() {
    return totalCount;
  }

  @JsonProperty("totalCount")
  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  /**
   **/
  public ApiContactsResult items(List<ApiContact> items) {
    this.items = items;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("items")
  @NotNull
  public List<ApiContact> getItems() {
    return items;
  }

  @JsonProperty("items")
  public void setItems(List<ApiContact> items) {
    this.items = items;
  }

  public ApiContactsResult addItemsItem(ApiContact itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }

    this.items.add(itemsItem);
    return this;
  }

  public ApiContactsResult removeItemsItem(ApiContact itemsItem) {
    if (itemsItem != null && this.items != null) {
      this.items.remove(itemsItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiContactsResult contactsResult = (ApiContactsResult) o;
    return Objects.equals(this.totalCount, contactsResult.totalCount) &&
        Objects.equals(this.items, contactsResult.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalCount, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiContactsResult {\n");
    
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}
