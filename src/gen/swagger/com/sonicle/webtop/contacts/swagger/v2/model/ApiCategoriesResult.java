package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategory;
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
 * Represents a response object for a collection of categories.
 **/
@ApiModel(description = "Represents a response object for a collection of categories.")
@JsonTypeName("CategoriesResult")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-01-31T17:20:03.694+01:00[Europe/Berlin]")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiCategoriesResult   {
  private @Valid Integer totalCount;
  private @Valid List<ApiCategory> items;

  /**
   * Items total count, usually used for pagination.
   **/
  public ApiCategoriesResult totalCount(Integer totalCount) {
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
  public ApiCategoriesResult items(List<ApiCategory> items) {
    this.items = items;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("items")
  public List<ApiCategory> getItems() {
    return items;
  }

  @JsonProperty("items")
  public void setItems(List<ApiCategory> items) {
    this.items = items;
  }

  public ApiCategoriesResult addItemsItem(ApiCategory itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }

    this.items.add(itemsItem);
    return this;
  }

  public ApiCategoriesResult removeItemsItem(ApiCategory itemsItem) {
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
    ApiCategoriesResult categoriesResult = (ApiCategoriesResult) o;
    return Objects.equals(this.totalCount, categoriesResult.totalCount) &&
        Objects.equals(this.items, categoriesResult.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalCount, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCategoriesResult {\n");
    
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
