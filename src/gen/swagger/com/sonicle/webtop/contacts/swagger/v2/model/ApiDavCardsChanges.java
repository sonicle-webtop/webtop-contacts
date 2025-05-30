package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavCardChanged;
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
 * Bean for carry card collection changes
 **/
@ApiModel(description = "Bean for carry card collection changes")
@JsonTypeName("DavCardsChanges")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-05-30T11:31:06.563+02:00[Europe/Berlin]")
public class ApiDavCardsChanges   {
  private @Valid String syncToken;
  private @Valid List<ApiDavCardChanged> inserted = new ArrayList<>();
  private @Valid List<ApiDavCardChanged> updated = new ArrayList<>();
  private @Valid List<ApiDavCardChanged> deleted = new ArrayList<>();

  /**
   * Current sync token
   **/
  public ApiDavCardsChanges syncToken(String syncToken) {
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
  public ApiDavCardsChanges inserted(List<ApiDavCardChanged> inserted) {
    this.inserted = inserted;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been inserted")
  @JsonProperty("inserted")
  @NotNull
  public List<ApiDavCardChanged> getInserted() {
    return inserted;
  }

  @JsonProperty("inserted")
  public void setInserted(List<ApiDavCardChanged> inserted) {
    this.inserted = inserted;
  }

  public ApiDavCardsChanges addInsertedItem(ApiDavCardChanged insertedItem) {
    if (this.inserted == null) {
      this.inserted = new ArrayList<>();
    }

    this.inserted.add(insertedItem);
    return this;
  }

  public ApiDavCardsChanges removeInsertedItem(ApiDavCardChanged insertedItem) {
    if (insertedItem != null && this.inserted != null) {
      this.inserted.remove(insertedItem);
    }

    return this;
  }
  /**
   * Items that have been updated
   **/
  public ApiDavCardsChanges updated(List<ApiDavCardChanged> updated) {
    this.updated = updated;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been updated")
  @JsonProperty("updated")
  @NotNull
  public List<ApiDavCardChanged> getUpdated() {
    return updated;
  }

  @JsonProperty("updated")
  public void setUpdated(List<ApiDavCardChanged> updated) {
    this.updated = updated;
  }

  public ApiDavCardsChanges addUpdatedItem(ApiDavCardChanged updatedItem) {
    if (this.updated == null) {
      this.updated = new ArrayList<>();
    }

    this.updated.add(updatedItem);
    return this;
  }

  public ApiDavCardsChanges removeUpdatedItem(ApiDavCardChanged updatedItem) {
    if (updatedItem != null && this.updated != null) {
      this.updated.remove(updatedItem);
    }

    return this;
  }
  /**
   * Items that have been deleted
   **/
  public ApiDavCardsChanges deleted(List<ApiDavCardChanged> deleted) {
    this.deleted = deleted;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been deleted")
  @JsonProperty("deleted")
  @NotNull
  public List<ApiDavCardChanged> getDeleted() {
    return deleted;
  }

  @JsonProperty("deleted")
  public void setDeleted(List<ApiDavCardChanged> deleted) {
    this.deleted = deleted;
  }

  public ApiDavCardsChanges addDeletedItem(ApiDavCardChanged deletedItem) {
    if (this.deleted == null) {
      this.deleted = new ArrayList<>();
    }

    this.deleted.add(deletedItem);
    return this;
  }

  public ApiDavCardsChanges removeDeletedItem(ApiDavCardChanged deletedItem) {
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
    ApiDavCardsChanges davCardsChanges = (ApiDavCardsChanges) o;
    return Objects.equals(this.syncToken, davCardsChanges.syncToken) &&
        Objects.equals(this.inserted, davCardsChanges.inserted) &&
        Objects.equals(this.updated, davCardsChanges.updated) &&
        Objects.equals(this.deleted, davCardsChanges.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(syncToken, inserted, updated, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiDavCardsChanges {\n");
    
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
