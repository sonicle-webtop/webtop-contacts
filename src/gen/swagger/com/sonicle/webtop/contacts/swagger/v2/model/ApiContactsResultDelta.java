package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactChanged;
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
 * Represent a response object that returns a collection of changed contacts between an instant in past.
 **/
@ApiModel(description = "Represent a response object that returns a collection of changed contacts between an instant in past.")
@JsonTypeName("ContactsResultDelta")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-05-30T11:31:06.563+02:00[Europe/Berlin]")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiContactsResultDelta   {
  private @Valid String nextSyncToken;
  private @Valid List<ApiContactChanged> items = new ArrayList<>();

  /**
   * The syncToken that identifies the instant in past since to get changes.
   **/
  public ApiContactsResultDelta nextSyncToken(String nextSyncToken) {
    this.nextSyncToken = nextSyncToken;
    return this;
  }

  
  @ApiModelProperty(value = "The syncToken that identifies the instant in past since to get changes.")
  @JsonProperty("nextSyncToken")
  public String getNextSyncToken() {
    return nextSyncToken;
  }

  @JsonProperty("nextSyncToken")
  public void setNextSyncToken(String nextSyncToken) {
    this.nextSyncToken = nextSyncToken;
  }

  /**
   **/
  public ApiContactsResultDelta items(List<ApiContactChanged> items) {
    this.items = items;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("items")
  @NotNull
  public List<ApiContactChanged> getItems() {
    return items;
  }

  @JsonProperty("items")
  public void setItems(List<ApiContactChanged> items) {
    this.items = items;
  }

  public ApiContactsResultDelta addItemsItem(ApiContactChanged itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }

    this.items.add(itemsItem);
    return this;
  }

  public ApiContactsResultDelta removeItemsItem(ApiContactChanged itemsItem) {
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
    ApiContactsResultDelta contactsResultDelta = (ApiContactsResultDelta) o;
    return Objects.equals(this.nextSyncToken, contactsResultDelta.nextSyncToken) &&
        Objects.equals(this.items, contactsResultDelta.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextSyncToken, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiContactsResultDelta {\n");
    
    sb.append("    nextSyncToken: ").append(toIndentedString(nextSyncToken)).append("\n");
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
