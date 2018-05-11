package com.sonicle.webtop.contacts.swagger.v1.model;

import com.sonicle.webtop.contacts.swagger.v1.model.CardChanged;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;


/**
 * Bean for carry card collection changes
 **/
import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
@ApiModel(description = "Bean for carry card collection changes")

public class CardsChanges   {
  
  private @Valid String syncToken = null;
  private @Valid List<CardChanged> inserted = new ArrayList<CardChanged>();
  private @Valid List<CardChanged> updated = new ArrayList<CardChanged>();
  private @Valid List<CardChanged> deleted = new ArrayList<CardChanged>();

  /**
   * Current sync token
   **/
  public CardsChanges syncToken(String syncToken) {
    this.syncToken = syncToken;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Current sync token")
  @JsonProperty("syncToken")
  @NotNull
  public String getSyncToken() {
    return syncToken;
  }
  public void setSyncToken(String syncToken) {
    this.syncToken = syncToken;
  }

  /**
   * Items that have been inserted
   **/
  public CardsChanges inserted(List<CardChanged> inserted) {
    this.inserted = inserted;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been inserted")
  @JsonProperty("inserted")
  @NotNull
  public List<CardChanged> getInserted() {
    return inserted;
  }
  public void setInserted(List<CardChanged> inserted) {
    this.inserted = inserted;
  }

  /**
   * Items that have been updated
   **/
  public CardsChanges updated(List<CardChanged> updated) {
    this.updated = updated;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been updated")
  @JsonProperty("updated")
  @NotNull
  public List<CardChanged> getUpdated() {
    return updated;
  }
  public void setUpdated(List<CardChanged> updated) {
    this.updated = updated;
  }

  /**
   * Items that have been deleted
   **/
  public CardsChanges deleted(List<CardChanged> deleted) {
    this.deleted = deleted;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Items that have been deleted")
  @JsonProperty("deleted")
  @NotNull
  public List<CardChanged> getDeleted() {
    return deleted;
  }
  public void setDeleted(List<CardChanged> deleted) {
    this.deleted = deleted;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardsChanges cardsChanges = (CardsChanges) o;
    return Objects.equals(syncToken, cardsChanges.syncToken) &&
        Objects.equals(inserted, cardsChanges.inserted) &&
        Objects.equals(updated, cardsChanges.updated) &&
        Objects.equals(deleted, cardsChanges.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(syncToken, inserted, updated, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CardsChanges {\n");
    
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

