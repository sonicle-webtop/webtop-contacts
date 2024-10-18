package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCardChanged;
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
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Bean for carry card collection changes
 **/
@ApiModel(description = "Bean for carry card collection changes")
@JsonTypeName("CardsChanges")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-10-14T17:59:50.160+02:00[Europe/Berlin]")
public class ApiCardsChanges   {
  private @Valid String syncToken;
  private @Valid List<ApiCardChanged> inserted = new ArrayList<>();
  private @Valid List<ApiCardChanged> updated = new ArrayList<>();
  private @Valid List<ApiCardChanged> deleted = new ArrayList<>();

  /**
   * Current sync token
   **/
  public ApiCardsChanges syncToken(String syncToken) {
    this.syncToken = syncToken;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Current sync token")
  @JsonProperty("syncToken")
  @NotNull
  public String getSyncToken() {
    return syncToken;
  }

  @JsonProperty("syncToken")
  public void setSyncToken(String syncToken) {
    this.syncToken = syncToken;
  }

  /**
   * Items that have been inserted
   **/
  public ApiCardsChanges inserted(List<ApiCardChanged> inserted) {
    this.inserted = inserted;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been inserted")
  @JsonProperty("inserted")
  @NotNull
  public List<ApiCardChanged> getInserted() {
    return inserted;
  }

  @JsonProperty("inserted")
  public void setInserted(List<ApiCardChanged> inserted) {
    this.inserted = inserted;
  }

  public ApiCardsChanges addInsertedItem(ApiCardChanged insertedItem) {
    if (this.inserted == null) {
      this.inserted = new ArrayList<>();
    }

    this.inserted.add(insertedItem);
    return this;
  }

  public ApiCardsChanges removeInsertedItem(ApiCardChanged insertedItem) {
    if (insertedItem != null && this.inserted != null) {
      this.inserted.remove(insertedItem);
    }

    return this;
  }
  /**
   * Items that have been updated
   **/
  public ApiCardsChanges updated(List<ApiCardChanged> updated) {
    this.updated = updated;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been updated")
  @JsonProperty("updated")
  @NotNull
  public List<ApiCardChanged> getUpdated() {
    return updated;
  }

  @JsonProperty("updated")
  public void setUpdated(List<ApiCardChanged> updated) {
    this.updated = updated;
  }

  public ApiCardsChanges addUpdatedItem(ApiCardChanged updatedItem) {
    if (this.updated == null) {
      this.updated = new ArrayList<>();
    }

    this.updated.add(updatedItem);
    return this;
  }

  public ApiCardsChanges removeUpdatedItem(ApiCardChanged updatedItem) {
    if (updatedItem != null && this.updated != null) {
      this.updated.remove(updatedItem);
    }

    return this;
  }
  /**
   * Items that have been deleted
   **/
  public ApiCardsChanges deleted(List<ApiCardChanged> deleted) {
    this.deleted = deleted;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been deleted")
  @JsonProperty("deleted")
  @NotNull
  public List<ApiCardChanged> getDeleted() {
    return deleted;
  }

  @JsonProperty("deleted")
  public void setDeleted(List<ApiCardChanged> deleted) {
    this.deleted = deleted;
  }

  public ApiCardsChanges addDeletedItem(ApiCardChanged deletedItem) {
    if (this.deleted == null) {
      this.deleted = new ArrayList<>();
    }

    this.deleted.add(deletedItem);
    return this;
  }

  public ApiCardsChanges removeDeletedItem(ApiCardChanged deletedItem) {
    if (deletedItem != null && this.deleted != null) {
      this.deleted.remove(deletedItem);
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
    ApiCardsChanges cardsChanges = (ApiCardsChanges) o;
    return Objects.equals(this.syncToken, cardsChanges.syncToken) &&
        Objects.equals(this.inserted, cardsChanges.inserted) &&
        Objects.equals(this.updated, cardsChanges.updated) &&
        Objects.equals(this.deleted, cardsChanges.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(syncToken, inserted, updated, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCardsChanges {\n");
    
    sb.append("    syncToken: ").append(toIndentedString(syncToken)).append("\n");
    sb.append("    inserted: ").append(toIndentedString(inserted)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

